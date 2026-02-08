package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

@TeleOp(group="Testing")
public class PosePositioner extends LinearOpMode {
    /*
     * ROBUSTNESS IMPROVEMENTS:
     * 1. Starting position selection requires A button confirmation before Start
     * 2. Reflection uses class reference (refactoring-safe, not hardcoded string)
     * 3. Heading error uses angle normalization to handle wrap-around (359° to 1° issue)
     * 4. moveToPos() called only once per movement to prevent path reset/stuttering
     * 5. Removed sleep() from main loop for responsive controls
     * 6. Position tolerance (1") and heading tolerance (2°) prevent wiggling at target
     */

    enum State {
        SELECT_STARTING_POS,
        SELECT_POSE,
        MOVING_TO_POSE,
        AT_POSE
    }

    private State state = State.SELECT_STARTING_POS;
    private State previousState = null;

    private int selectedIndex = 0;
    private int previousSelectedIndex = -1;

    private int scrollOffset = 0;
    private int previousScrollOffset = -1;

    private static final int POSES_PER_PAGE = 12;

    private boolean telemetryNeedsUpdate = true;

    // For MOVING_TO_POSE state - update periodically
    private long lastMovingUpdateTime = 0;
    private static final long MOVING_UPDATE_INTERVAL_MS = 250; // Update every 250ms while moving

    // Position tolerance - stop moving when within this distance
    private static final double POSITION_TOLERANCE = 1.0; // inches
    private static final double HEADING_TOLERANCE = Math.toRadians(2); // 2 degrees in radians
    private boolean isWithinTolerance = false;
    private boolean hasStartedMoving = false; // Track if moveToPos has been called

    GamepadManager GPM = null;
    PedroTeleOpManager PTM = null;

    ArrayList<String> poseNames = new ArrayList<>();
    ArrayList<Pose> poses = new ArrayList<>();

    ArrayList<String> startingPoseNames = new ArrayList<>();
    ArrayList<Pose> startingPoses = new ArrayList<>();

    int selectedStartingIndex = 0;
    int previousStartingIndex = -1;

    Pose targetPose = null;

    @Override
    public void runOpMode() {
        GPM = new GamepadManager(gamepad1);

        // Load starting positions
        startingPoseNames.add("Red Start");
        startingPoses.add(RedStartPos);
        startingPoseNames.add("Blue Start");
        startingPoses.add(BlueStartPos);
        startingPoseNames.add("Red Anchor");
        startingPoses.add(RedAnchorPoint);
        startingPoseNames.add("Blue Anchor");
        startingPoses.add(BlueAnchorPoint);

        // Load all poses from PositionsAndPaths
        telemetry.addLine("Loading poses...");
        loadPoses();
        telemetry.addData("Poses Found", poseNames.size());
        telemetry.addLine("");
        telemetry.addLine("=== SELECT STARTING POSITION ===");
        telemetry.addLine("Use D-Pad Up/Down to scroll");
        telemetry.addLine("Press A to select");

        for (int i = 0; i < startingPoseNames.size(); i++) {
            if (i == selectedStartingIndex) {
                telemetry.addData(">", startingPoseNames.get(i));
            } else {
                telemetry.addData(" ", startingPoseNames.get(i));
            }
        }
        telemetry.update();

        // Wait for starting position selection
        boolean selectionConfirmed = false;
        while (!isStarted() && !isStopRequested()) {
            boolean needsUpdate = false;

            if (gamepad1.dpad_down) {
                selectedStartingIndex = (selectedStartingIndex + 1) % startingPoses.size();
                needsUpdate = true;
                sleep(150); // Reduced from 200 for better responsiveness
            }
            if (gamepad1.dpad_up) {
                selectedStartingIndex = (selectedStartingIndex - 1 + startingPoses.size()) % startingPoses.size();
                needsUpdate = true;
                sleep(150);
            }
            if (gamepad1.a) {
                selectionConfirmed = true;
                needsUpdate = true;
            }

            // Only update telemetry if selection changed
            if (needsUpdate || selectedStartingIndex != previousStartingIndex) {
                telemetry.clear();
                telemetry.addLine("=== SELECT STARTING POSITION ===");
                telemetry.addLine("Use D-Pad Up/Down to scroll");
                telemetry.addLine(selectionConfirmed ? "Press START to begin" : "Press A to confirm, then START");
                telemetry.addLine("");

                for (int i = 0; i < startingPoseNames.size(); i++) {
                    String prefix = (i == selectedStartingIndex) ? (selectionConfirmed ? "✓ " : "> ") : "  ";
                    telemetry.addData(prefix, startingPoseNames.get(i));
                }
                telemetry.update();
                previousStartingIndex = selectedStartingIndex;
            }
        }

        waitForStart();

        // Initialize PTM with selected starting pose
        PTM = new PedroTeleOpManager(hardwareMap, startingPoses.get(selectedStartingIndex));
        state = State.SELECT_POSE;
        selectedIndex = 0;
        scrollOffset = 0;
        telemetryNeedsUpdate = true;

        while (opModeIsActive()) {
            try {
                // Update gamepad state FIRST before any input checks
                GPM.update();

                GUIControl();

                // Check if we need to update telemetry
                checkTelemetryUpdate();

                if (telemetryNeedsUpdate) {
                    telemetry.clear();
                    printStateData(telemetry);
                    telemetry.update();
                    telemetryNeedsUpdate = false;

                    // Update tracking variables
                    previousState = state;
                    previousSelectedIndex = selectedIndex;
                    previousScrollOffset = scrollOffset;
                }

                switch (state) {
                    case SELECT_POSE:
                        PTM.updatePos();
                        break;

                    case MOVING_TO_POSE:
                        // Call moveToPos only once when first entering this state
                        if (!hasStartedMoving) {
                            PTM.moveToPos(targetPose);
                            hasStartedMoving = true;
                        }

                        // Update position and motors every loop
                        PTM.updatePosAndMotors();

                        // Calculate distance to target with proper angle normalization
                        double distanceToTarget = PTM.robotDistanceFromPos(targetPose);
                        double headingError = normalizeAngleDifference(
                                PTM.getCurrPos().getHeading(),
                                targetPose.getHeading()
                        );

                        // Check if within tolerance
                        if (distanceToTarget <= POSITION_TOLERANCE && headingError <= HEADING_TOLERANCE) {
                            if (!isWithinTolerance) {
                                // Just entered tolerance zone - stop the robot
                                PTM.breakFollower();
                                isWithinTolerance = true;
                            }
                            // Stay stopped and transition to AT_POSE
                            state = State.AT_POSE;
                            telemetryNeedsUpdate = true;
                        } else {
                            isWithinTolerance = false;
                        }
                        break;

                    case AT_POSE:
                        PTM.updatePos();
                        break;
                }

            } catch (Exception e) {
                telemetry.clear();
                telemetry.addData("ERROR", "Problem in state %s", state);
                telemetry.addData("MSG", e.getMessage());
                telemetry.update();
            }
        }
    }

    private void checkTelemetryUpdate() {
        // State changed
        if (state != previousState) {
            telemetryNeedsUpdate = true;
            return;
        }

        // Selection changed in SELECT_POSE
        if (state == State.SELECT_POSE &&
                (selectedIndex != previousSelectedIndex || scrollOffset != previousScrollOffset)) {
            telemetryNeedsUpdate = true;
            return;
        }

        // Periodic update while moving
        if (state == State.MOVING_TO_POSE) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMovingUpdateTime >= MOVING_UPDATE_INTERVAL_MS) {
                telemetryNeedsUpdate = true;
                lastMovingUpdateTime = currentTime;
            }
        }
    }

    public void loadPoses() {
        // Use reflection to get all static Pose fields from PositionsAndPaths
        try {
            // Use class reference instead of hardcoded string - refactoring-safe!
            Class<?> clazz = PositionsAndPaths.class;
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.getType().equals(Pose.class) &&
                        java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    try {
                        Pose pose = (Pose) field.get(null);
                        poseNames.add(field.getName());
                        poses.add(pose);
                    } catch (IllegalAccessException e) {
                        telemetry.addLine("Could not access: " + field.getName());
                    }
                }
            }
        } catch (Exception e) {
            telemetry.addLine("Error loading poses: " + e.getMessage());
        }
    }

    /**
     * Normalize angle difference to range [-PI, PI] to handle wrap-around
     * This prevents the robot from spinning the "long way" around
     */
    private double normalizeAngleDifference(double angle1, double angle2) {
        double diff = angle1 - angle2;
        while (diff > Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;
        return Math.abs(diff);
    }

    public void printStateData(Telemetry telemetry) {
        switch (state) {
            case SELECT_POSE:
                telemetry.addLine("=== SELECT A POSE ===");
                telemetry.addLine("D-Pad Up/Down: Scroll one");
                telemetry.addLine("D-Pad Left/Right: Scroll page");
                telemetry.addLine("Press A to move to pose");
                telemetry.addData("Selected Index", "%d/%d", selectedIndex + 1, poseNames.size());
                telemetry.addData("Page", "%d/%d",
                        (scrollOffset / POSES_PER_PAGE) + 1,
                        (poseNames.size() + POSES_PER_PAGE - 1) / POSES_PER_PAGE);
                telemetry.addLine("");
                listPoses();
                break;

            case MOVING_TO_POSE:
                telemetry.addLine("=== MOVING TO POSE ===");
                telemetry.addData("Target Pose", poseNames.get(selectedIndex));
                telemetry.addData("Target X", "%.2f", targetPose.getX());
                telemetry.addData("Target Y", "%.2f", targetPose.getY());
                telemetry.addData("Target Heading", "%.2f°", Math.toDegrees(targetPose.getHeading()));
                telemetry.addLine("");
                telemetry.addData("Current X", "%.2f", PTM.getCurrPos().getX());
                telemetry.addData("Current Y", "%.2f", PTM.getCurrPos().getY());
                telemetry.addData("Current Heading", "%.2f°", Math.toDegrees(PTM.getCurrPos().getHeading()));
                telemetry.addLine("");
                telemetry.addData("Distance", "%.2f", PTM.robotDistanceFromPos(targetPose));
                telemetry.addLine("Press X to cancel");
                break;

            case AT_POSE:
                telemetry.addLine("=== AT POSE ===");
                telemetry.addData("Pose", poseNames.get(selectedIndex));
                telemetry.addData("X", "%.2f", PTM.getCurrPos().getX());
                telemetry.addData("Y", "%.2f", PTM.getCurrPos().getY());
                telemetry.addData("Heading", "%.2f°", Math.toDegrees(PTM.getCurrPos().getHeading()));
                telemetry.addLine("");
                telemetry.addLine("B: Return to selection");
                break;
        }
    }

    public void listPoses() {
        int startIndex = scrollOffset;
        int endIndex = Math.min(scrollOffset + POSES_PER_PAGE, poseNames.size());

        for (int i = startIndex; i < endIndex; i++) {
            if (i == selectedIndex) {
                telemetry.addData(">", poseNames.get(i));
            } else {
                telemetry.addData(" ", poseNames.get(i));
            }
        }

        if (endIndex < poseNames.size()) {
            telemetry.addLine("... (more poses below)");
        }
        if (startIndex > 0) {
            telemetry.addLine("(more poses above)");
        }
    }

    public void GUIControl() {
        int arraySize = poses.size();

        // Scrolling logic
        if (state == State.SELECT_POSE) {
            // Scroll one pose at a time
            if (GPM.isPressed(Button.DPAD_DOWN) && arraySize > 0) {
                selectedIndex = (selectedIndex + 1) % arraySize;

                // Auto-scroll down if needed
                if (selectedIndex >= scrollOffset + POSES_PER_PAGE) {
                    scrollOffset = selectedIndex - POSES_PER_PAGE + 1;
                }
                // Wrap around to top
                if (selectedIndex == 0) {
                    scrollOffset = 0;
                }
            }

            if (GPM.isPressed(Button.DPAD_UP) && arraySize > 0) {
                selectedIndex = (selectedIndex - 1 + arraySize) % arraySize;

                // Auto-scroll up if needed
                if (selectedIndex < scrollOffset) {
                    scrollOffset = selectedIndex;
                }
                // Wrap around to bottom
                if (selectedIndex == arraySize - 1) {
                    scrollOffset = Math.max(0, arraySize - POSES_PER_PAGE);
                }
            }

            // Scroll full page with left/right
            if (GPM.isPressed(Button.DPAD_RIGHT) && arraySize > 0) {
                scrollOffset = Math.min(scrollOffset + POSES_PER_PAGE,
                        Math.max(0, arraySize - POSES_PER_PAGE));
                selectedIndex = Math.min(scrollOffset, arraySize - 1);
            }

            if (GPM.isPressed(Button.DPAD_LEFT) && arraySize > 0) {
                scrollOffset = Math.max(scrollOffset - POSES_PER_PAGE, 0);
                selectedIndex = scrollOffset;
            }
        }

        // State transitions
        if (GPM.isPressed(Button.A)) {
            if (state == State.SELECT_POSE) {
                targetPose = poses.get(selectedIndex);
                state = State.MOVING_TO_POSE;
                lastMovingUpdateTime = System.currentTimeMillis();
                isWithinTolerance = false; // Reset tolerance flag
                hasStartedMoving = false; // Reset movement flag
                telemetryNeedsUpdate = true;
            }
        }

        if (GPM.isPressed(Button.B)) {
            if (state == State.AT_POSE || state == State.MOVING_TO_POSE) {
                PTM.breakFollower();
                state = State.SELECT_POSE;
                hasStartedMoving = false; // Reset movement flag
                telemetryNeedsUpdate = true;
            }
        }

        if (GPM.isPressed(Button.X)) {
            if (state == State.MOVING_TO_POSE) {
                PTM.breakFollower();
                state = State.SELECT_POSE;
                hasStartedMoving = false; // Reset movement flag
                telemetryNeedsUpdate = true;
            }
        }
    }
}
package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

@TeleOp(group="Testing")
public class PosePositioner extends LinearOpMode {
    enum State {
        SELECT_STARTING_POS,
        SELECT_POSE,
        MOVING_TO_POSE,
        AT_POSE
    }

    private State state = State.SELECT_STARTING_POS;
    private int selectedIndex = 0;
    private int scrollOffset = 0;
    private static final int POSES_PER_PAGE = 12;

    GamepadManager GPM = null;
    PedroTeleOpManager PTM = null;

    ArrayList<String> poseNames = new ArrayList<>();
    ArrayList<Pose> poses = new ArrayList<>();

    ArrayList<String> startingPoseNames = new ArrayList<>();
    ArrayList<Pose> startingPoses = new ArrayList<>();
    int selectedStartingIndex = 0;

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
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.dpad_down) {
                selectedStartingIndex = (selectedStartingIndex + 1) % startingPoses.size();
                sleep(200);
            }
            if (gamepad1.dpad_up) {
                selectedStartingIndex = (selectedStartingIndex - 1 + startingPoses.size()) % startingPoses.size();
                sleep(200);
            }
            if (gamepad1.a) {
                break;
            }

            telemetry.clear();
            telemetry.addLine("=== SELECT STARTING POSITION ===");
            telemetry.addLine("Use D-Pad Up/Down to scroll");
            telemetry.addLine("Press A to select");
            telemetry.addLine("Press START to begin");
            telemetry.addLine("");
            for (int i = 0; i < startingPoseNames.size(); i++) {
                if (i == selectedStartingIndex) {
                    telemetry.addData(">", startingPoseNames.get(i));
                } else {
                    telemetry.addData(" ", startingPoseNames.get(i));
                }
            }
            telemetry.update();
        }

        waitForStart();

        // Initialize PTM with selected starting pose
        PTM = new PedroTeleOpManager(hardwareMap, startingPoses.get(selectedStartingIndex));
        state = State.SELECT_POSE;
        selectedIndex = 0;
        scrollOffset = 0;

        while (opModeIsActive()) {
            try {
                GUIControl();
                printStateData(telemetry);

                switch (state) {
                    case SELECT_POSE:
                        PTM.updatePos();
                        break;
                    case MOVING_TO_POSE:
                        PTM.moveToPos(targetPose);
                        PTM.updatePosAndMotors();
                        // Check if reached target
                        if (!PTM.isBusy()) {
                            state = State.AT_POSE;
                        }
                        break;
                    case AT_POSE:
                        PTM.updatePos();
                        break;
                }
            } catch (Exception e) {
                telemetry.addData("ERROR", "Problem in state %s", state);
                telemetry.addData("MSG", e.getMessage());
            }

            GPM.update();
            telemetry.update();
            sleep(1);
        }
    }

    public void loadPoses() {
        // Use reflection to get all static Pose fields from PositionsAndPaths
        try {
            Class<?> clazz = Class.forName("org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths");
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
        } catch (ClassNotFoundException e) {
            telemetry.addLine("Error: Could not find PositionsAndPaths class");
        }
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
            }
        }

        if (GPM.isPressed(Button.B)) {
            if (state == State.AT_POSE || state == State.MOVING_TO_POSE) {
                PTM.breakFollower();
                state = State.SELECT_POSE;
            }
        }

        if (GPM.isPressed(Button.X)) {
            if (state == State.MOVING_TO_POSE) {
                PTM.breakFollower();
                state = State.SELECT_POSE;
            }
        }
    }
}
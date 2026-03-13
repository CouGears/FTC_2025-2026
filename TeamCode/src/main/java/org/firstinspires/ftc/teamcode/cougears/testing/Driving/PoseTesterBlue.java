package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "PosTesterBlue", group = "Testing")
public class PoseTesterBlue extends LinearOpMode {

    public static class NamedPose {
        String name;
        Pose pose;

        public NamedPose(String name, Pose pose) {
            this.name = name;
            this.pose = pose;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize your Robot wrapper class
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();
        bot.openBlocker();
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap, BlueStartPos);

        List<NamedPose> targetPositions = new ArrayList<>();

        // --- Blue Poses ---
        targetPositions.add(new NamedPose("Blue Start Pos", BlueStartPos));
        targetPositions.add(new NamedPose("Blue Anchor", BlueAnchorPoint));
        targetPositions.add(new NamedPose("Blue Far Start", BlueStartPosFar));

        targetPositions.add(new NamedPose("Blue Shoot Wall", BlueShootWall));
        targetPositions.add(new NamedPose("Blue Center Zone", BlueShootCenterZone));
        targetPositions.add(new NamedPose("Blue Triangle Tip", BlueShootTriangleTip));
        targetPositions.add(new NamedPose("Blue Triangle Close", BlueShootTriangleClose));
        targetPositions.add(new NamedPose("Blue Shoot Far", BlueShootFar));

        targetPositions.add(new NamedPose("Blue BD1 Start", BlueBallDepotStart1));
        targetPositions.add(new NamedPose("Blue BD2 Start", BlueBallDepotStart2));
        targetPositions.add(new NamedPose("Blue BD3 Start", BlueBallDepotStart3));
        targetPositions.add(new NamedPose("Blue BD1 End", BlueBallDepotEnd1));
        targetPositions.add(new NamedPose("Blue BD2 End", BlueBallDepotEnd2));
        targetPositions.add(new NamedPose("Blue BD3 End", BlueBallDepotEnd3));

        targetPositions.add(new NamedPose("Blue Gate Init (Auton)", BlueGateInit));
        targetPositions.add(new NamedPose("Blue Gate Init (Driver)", Driver_BlueGateInit));
        targetPositions.add(new NamedPose("Blue Gate Open (Auton)", BlueGateOpen));
        targetPositions.add(new NamedPose("Blue Gate Open (Driver)", Driver_BlueGateOpen));

        targetPositions.add(new NamedPose("Blue Park", BluePark));
        targetPositions.add(new NamedPose("Blue Basic End Close", BlueBasicEndClose));
        targetPositions.add(new NamedPose("Blue Basic End Close", BlueBasicEndFar));


        // --- Blue Poses ---
        targetPositions.add(new NamedPose("Blue Start Pos", BlueStartPos));
        targetPositions.add(new NamedPose("Blue Shoot Wall", BlueShootWall));
        targetPositions.add(new NamedPose("Blue Center Zone", BlueShootCenterZone));
        targetPositions.add(new NamedPose("Blue Triangle Tip", BlueShootTriangleTip));
        targetPositions.add(new NamedPose("Blue Triangle Close", BlueShootTriangleClose));
        targetPositions.add(new NamedPose("Blue Gate Init", BlueGateInit));
        targetPositions.add(new NamedPose("Blue Park", BluePark));

        // State variables for the menu and control
        int selectedIndex = 0;
        boolean isAutoDriving = false;

        telemetry.addLine("Robot Initialized.");
        telemetry.addLine("Use D-Pad Up/Down to scroll through poses.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpadUpWasPressed()) {
                selectedIndex--;
                if (selectedIndex < 0) {
                    selectedIndex = targetPositions.size() - 1; // Wrap around to bottom
                }
            }
            if (gamepad1.dpadDownWasPressed()) {
                selectedIndex++;
                if (selectedIndex >= targetPositions.size()) {
                    selectedIndex = 0; // Wrap around to top
                }
            }

            NamedPose currentSelection = targetPositions.get(selectedIndex);

            if (gamepad1.a) {
                isAutoDriving = true;
                PTM.moveToPos(currentSelection.pose);
            }

            if (gamepad1.b || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
                isAutoDriving = false;
                PTM.breakFollower();
            }

            if (bot.isHeld(1, Button.R_TRIGGER)){
                bot.FWSpinTo(PTM.getClosestShootingPosition().getShootingVelocity());
            } else {
                bot.killFW();
            }
            if (bot.isHeld(1, GamepadManager.Button.R_BUMPER)){
                bot.startTransfer();
                bot.startIntakeSlow();
            } else {
                bot.killTransfer();
                bot.killIntake();
            }

            // --- Execution ---
            if (isAutoDriving) {
                PTM.updatePosAndMotors();
                telemetry.addLine(">>> STATUS: AUTO DRIVING TO " + currentSelection.name.toUpperCase() + " <<<");
                telemetry.addLine("Press 'B' or touch joysticks to CANCEL.");
            } else {
                bot.RafiDrive(gamepad1, false);
                telemetry.addLine("STATUS: MANUAL DRIVE");
                PTM.updatePos();
            }

            telemetry.addLine("\n=============================");
            telemetry.addLine("     POSITION SELECTOR");
            telemetry.addLine(" Scroll Menu");
            telemetry.addLine(" Move to Selected");
            telemetry.addLine("=============================\n");

            for (int i = 0; i < targetPositions.size(); i++) {
                if (i == selectedIndex) {
                    telemetry.addLine(" >>  " + targetPositions.get(i).name + "  << ");
                }
                else if (Math.abs(i - selectedIndex) <= 2) {
                    telemetry.addLine("      " + targetPositions.get(i).name);
                }
            }
            telemetry.update();
        }
    }
}
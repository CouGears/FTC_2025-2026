package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;


import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "PosTester", group = "Testing")
public class PoseTester extends LinearOpMode {

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
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap, RedStartPos);

        List<NamedPose> targetPositions = new ArrayList<>();

        // --- Red Poses ---
        targetPositions.add(new NamedPose("Red Start Pos", RedStartPos));
        targetPositions.add(new NamedPose("Red Anchor", RedAnchorPoint));
        targetPositions.add(new NamedPose("Red Far Start", RedStartPosFar));

        targetPositions.add(new NamedPose("Red Shoot Wall", RedShootWall));
        targetPositions.add(new NamedPose("Red Center Zone", RedShootCenterZone));
        targetPositions.add(new NamedPose("Red Triangle Tip", RedShootTriangleTip));
        targetPositions.add(new NamedPose("Red Triangle Close", RedShootTriangleClose));
        targetPositions.add(new NamedPose("Red Shoot Far", RedShootFar));

        targetPositions.add(new NamedPose("Red BD1 Start", RedBallDepotStart1));
        targetPositions.add(new NamedPose("Red BD2 Start", RedBallDepotStart2));
        targetPositions.add(new NamedPose("Red BD3 Start", RedBallDepotStart3));
        targetPositions.add(new NamedPose("Red BD1 End", RedBallDepotEnd1));
        targetPositions.add(new NamedPose("Red BD2 End", RedBallDepotEnd2));
        targetPositions.add(new NamedPose("Red BD3 End", RedBallDepotEnd3));

        targetPositions.add(new NamedPose("Red Gate Init (Auton)", RedGateInit));
        targetPositions.add(new NamedPose("Red Gate Init (Driver)", Driver_RedGateInit));
        targetPositions.add(new NamedPose("Red Gate Open (Auton)", RedGateOpen));
        targetPositions.add(new NamedPose("Red Gate Open (Driver)", Driver_RedGateOpen));

        targetPositions.add(new NamedPose("Red Park", RedPark));
        targetPositions.add(new NamedPose("Red Basic End Close", RedBasicEndClose));
        targetPositions.add(new NamedPose("Red Basic End Close", RedBasicEndFar));


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
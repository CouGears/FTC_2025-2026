package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.PanelsFeatures;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
import org.firstinspires.ftc.teamcode.cougears.util.SensorFusionManager;


@TeleOp(name="V3Teleop", group="Drive")

public class V3TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();

        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap);

        AprilTagBase ATB = new AprilTagBase(hardwareMap, telemetry);
        ATB.initAprilTag();

        SensorFusionManager SFM = new SensorFusionManager(hardwareMap, telemetry, ATB, bot);

        PanelsFeatures panels = new PanelsFeatures(PTM.follower, telemetry);
        panels.startCameraStream(ATB.visionPortal);
        bot.setTelemetry(panels.getTelemetry());
        telemetry = panels.getTelemetry();

        boolean resetGoToShootingPos = true;
        boolean alignedRobot = false;

        while (!opModeIsActive()) {
            telemetry.addData("Status", "Initialized");
            telemetry.addData("Switched Joysticks", "%s", Drive_switchedJoysticks);
            telemetry.addLine("Press X to change");
            if (gamepad1.xWasPressed())
                Drive_switchedJoysticks = !Drive_switchedJoysticks;
            telemetry.update();
        }

        while (opModeIsActive()) {
            //****** DRIVE (Controller 1)******
            PTM.updateStoragePosition();
            //SensorFusionManager.ballState currentBallState = SFM.ballInPosition();
            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (bot.GPM_1.joystickInputFound()){
                if (PTM.isBusy()) {
                    PTM.breakFollower();
                }
                resetGoToShootingPos = true;
                alignedRobot = false;
                SFM.resetStep();
            }
            if (!PTM.isBusy()) {
                PTM.updatePos(); // Update w/o motors
                if (bot.isPressed(1, Button.B)) {
                    bot.toggleSlow();
                }
                bot.RafiDrive(gamepad1, Drive_switchedJoysticks);
                telemetry.addData("Slowed", "%b", bot.slowed);
            }

            telemetry.addData("Assigned Goal", "%s", PTM.getGoal());

            //****** AUTON MOVING ******
            if (bot.isPressed(1, Button.DPAD_DOWN) && !PTM.isBusy()){
                resetGoToShootingPos = false;
            }
            if (!resetGoToShootingPos){
                alignedRobot = SFM.handFullShootPosAlignSequence(PTM);
            }

            if (bot.isHeld(1, Button.DPAD_RIGHT) && !PTM.isBusy()){
                PTM.parkRobot();
            }
            if (bot.isHeld(1, Button.DPAD_LEFT) && !PTM.isBusy()){
                PTM.openGate();
            }
            if (bot.isHeld(1, Button.DPAD_UP) && !PTM.isBusy()){
                PTM.goToHumanLoadZone();
            }
            if (PTM.isBusy()){
                PTM.updatePosAndMotors();
            }
            telemetry.addData("BotPos", "Positioned at X %.2f", PTM.getCurrPos().getX());
            telemetry.addData("BotPos", "Positioned at Y %.2f", PTM.getCurrPos().getY());
            telemetry.addData("BotPos", "Positioned at Heading %.2f", Math.toDegrees(PTM.getCurrPos().getHeading()));


            //****** INTAKE ******
            if (bot.isPressed(1, Button.X)) {
                bot.deleteTimer("RejectIntake");
                if (!bot.IntakeSpinning) {
                    bot.startTransfer();
                    bot.startIntake();
                } else {
                    bot.killTransfer();
                    bot.killIntake();
                }
            }

            //****** FLYWHEEL (Controller 2)******
            // BUTTONS: L_TRIGGER, L_BUMPER, R_TRIGGER, R_BUMPER
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.FWSpinTo(PTM.getClosestShootingPosition().getShootingVelocity());
            }
            else if (bot.isHeld(2, Button.L_BUMPER)) {
                bot.ejectFW();
                bot.ejectTransfer();
                bot.ejectIntake();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", FW_ejectionVel);
                bot.createTimer("RejectIntake");
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());
            telemetry.addData("Flywheel", "AIMING FOR  vel %d", PTM.getClosestShootingPosition().getShootingVelocity());

            if (bot.timerExpired_Seconds("RejectIntake", 2)){
                bot.startTransfer();
                bot.startIntake();
                bot.deleteTimer("RejectIntake");
            }

            //****** SHOOT SEQUENCE (Controller 2)******
            if (bot.isHeld(2, Button.R_TRIGGER)) {
                if (!bot.isHeld(2, Button.R_BUMPER)) {bot.killTransfer();}
                bot.openBlocker();
            } else {
                bot.closeBlocker();
            }


            if (bot.isHeld(2, Button.R_BUMPER)){
                if (PTM.getClosestShootingPosition().equals(redShootingPosHashMap.get("RedFar"))){
                    bot.startTransferFar();
                    bot.startIntake();
                } else {
                    bot.startTransfer();
                    bot.startIntake();
                }
            } else if (!bot.IntakeSpinning) {
                bot.killTransfer();
            }


            bot.update();
            panels.update();
            sleep(10);
            SFM.handleLEDS(PTM, alignedRobot);
        }
        bot.endTeleOp();
    }
}

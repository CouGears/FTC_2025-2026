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
        boolean intakeOn = false;

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
            SensorFusionManager.ballState currentBallState = SFM.ballInPosition();
            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (bot.GPM_1 != null && bot.GPM_1.joystickInputFound()){
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
                if (alignedRobot) {
                    SFM.resetStep();
                    resetGoToShootingPos = true;
                }
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


            //****** INTAKE & TRANSFER SENSORS ******
            boolean ballAtTop = SFM.sensorDetectingBall(1);
            boolean isShooting = bot.isHeld(2, Button.R_BUMPER);
            boolean isEjecting = bot.isHeld(2, Button.L_BUMPER);

            // Toggle Intake state with X
            if (bot.isPressed(1, Button.X)) {
                bot.deleteTimer("RejectIntake");
                intakeOn = !intakeOn;
            }
            if (bot.isPressed(1, Button.Y)) {
                PTM.handleGateIntake();
            }

            //****** FLYWHEEL (Controller 2) ******
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.FWSpinTo(PTM.getClosestShootingPosition().getShootingVelocity());
            } else if (!isEjecting) {
                bot.killFW();
            }
            // Flywheel Blocker / Gate
            if (bot.isHeld(2, Button.R_TRIGGER)) {
                bot.openBlocker();
            } else {
                bot.closeBlocker();
            }

            //****** CONSOLIDATED INTAKE & TRANSFER PRIORITY LOGIC ******
            /*
            4 "States" listed by priority:
            1. Ejecting (controlled via L bumper and set to turn off via a timer when let go)
            2. Shooting (controlled via R bumper. Will always turn on intake & transfer)
            3. Normally Intaking (Controlled via X, turns on intake and trnasfer (as long as no ball at top where it turns transfer off))
            4. All off
             */
            if (isEjecting) {
                intakeOn = false;
                bot.ejectFW();
                bot.ejectTransfer();
                bot.ejectIntake();
                bot.createTimer("RejectIntake");
                telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_ejectionVel);
            } else {
                if (bot.timerExpired_Seconds("RejectIntake", 2)) {
                    intakeOn = true;
                    bot.deleteTimer("RejectIntake");
                }
                if (isShooting) {
                    bot.startIntakeFast();
                    if (PTM.getClosestShootingPosition().equals(redShootingPosHashMap.get("RedFar"))) {
                        bot.startTransferFar();
                    } else {
                        bot.startTransfer();
                    }
                } else if (intakeOn) {
                    if (SFM.sensorDetectingBall(2)) {
                        bot.startIntakeSlow();
                    } else {
                        bot.startIntakeFast();
                    }
                    if (ballAtTop) {
                        bot.killTransfer();
                    } else {
                        bot.startTransfer();
                    }
                } else {
                    bot.killIntake();
                    bot.killTransfer();
                }
            }

            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());
            telemetry.addData("Flywheel", "AIMING FOR vel %d", PTM.getClosestShootingPosition().getShootingVelocity());

            bot.update();
            panels.update();
            sleep(10);
            SFM.handleLEDS(PTM); // (Make sure parameters match your latest SFM.java)
        }
        bot.endTeleOp();
    }
}


package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(name="V3Teleop", group="Drive")

public class V3TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap);
        // Initialize motors
        bot.botInit();
        // Wait for the game to start (driver presses PLAY)
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
            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (bot.GPM_1.joystickInputFound() && PTM.isBusy()){
                PTM.breakFollower();
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
                PTM.moveToPos(PTM.getClosestShootingPosition().getShootingPose());
                telemetry.addData("BotPos", "Traveling to X %.2f", PTM.getClosestShootingPosition().getShootingPose().getX());
                telemetry.addData("BotPos", "Traveling to Y %.2f", PTM.getClosestShootingPosition().getShootingPose().getY());
                telemetry.addData("BotPos", "Traveling to Heading %.2f", PTM.getClosestShootingPosition().getShootingPose().getY());


            }
            if (bot.isHeld(1, Button.DPAD_RIGHT) && !PTM.isBusy()){
                if (PTM.getGoal().equals("Red")){
                    PTM.moveToPos(RedPark);
                    telemetry.addData("BotPos", "Traveling to X %.2f", RedPark.getX());
                    telemetry.addData("BotPos", "Traveling to Y %.2f", RedPark.getY());

                } else {
                    PTM.moveToPos(BluePark);
                    telemetry.addData("BotPos", "Traveling to X %.2f", BluePark.getX());
                    telemetry.addData("BotPos", "Traveling to Y %.2f", BluePark.getY());
                }
            }
            if (bot.isHeld(1, Button.DPAD_LEFT) && !PTM.isBusy()){
                PTM.openGate();
            }
            if (PTM.isBusy()){
                PTM.updatePosAndMotors();
            }
            telemetry.addData("BotPos", "Positioned at X %.2f", PTM.getCurrPos().getX());
            telemetry.addData("BotPos", "Positioned at Y %.2f", PTM.getCurrPos().getY());
            telemetry.addData("BotPos", "Positioned at Heading %.2f", PTM.getCurrPos().getY());


            //****** INTAKE ******
            if (bot.isPressed(1, Button.X)) {
                bot.deleteTimer("RejectIntake");
                if (!bot.IntakeSpinning) {
                    bot.startTransferSlow();
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
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());
            telemetry.addData("Flywheel", "AIMING FOR  vel %d", PTM.getClosestShootingPosition().getShootingVelocity());



            //****** SHOOT SEQUENCE (Controller 2)******
            if (bot.isHeld(2, Button.R_TRIGGER)) {
                if (!bot.isHeld(2, Button.R_BUMPER)) {bot.killTransfer();}
                bot.openBlocker();
            } else {
                bot.closeBlocker();
            }

            if (bot.isHeld(2, Button.R_BUMPER)){
                bot.startTransfer();
                bot.startIntake();
            } else if (!bot.IntakeSpinning) {
                bot.killTransfer();
            }


            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

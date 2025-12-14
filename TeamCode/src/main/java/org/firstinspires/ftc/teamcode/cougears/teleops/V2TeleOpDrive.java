package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="V2Teleop", group="Drive")

public class V2TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        V2TeleOpBase bot = new V2TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        AprilTagManager ATM = new AprilTagManager(hardwareMap, telemetry, bot);
        // Initialize motors
        bot.botInit();
        ATM.initAprilTag();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();


        while (opModeIsActive()) {
            //****** DRIVE (Controller 1)******
            // BUTTONS: L-Joystick, R-Joystick, B
            if (bot.isPressed(1, Button.B)) {
                bot.toggleSlow();
            }
            bot.RafiDrive(gamepad1);
            telemetry.addData("Slowed", "%b", bot.slowed);

            //****** ATM (Controller 1)******
            // BUTTONS: Y, DPAD_UP, DPAD_DOWN
            if (bot.isHeld(1, Button.Y)) {
                ATM.alignToAT(redTag);
                ATM.alignToAT(blueTag);
            }
            if (bot.isHeld(1, Button.DPAD_DOWN)) {
                ATM.FullAutoMove(redTag);
                ATM.FullAutoMove(blueTag);
            }
            if (bot.isHeld(1, Button.DPAD_UP)) {
                ATM.alignTurretToAT(redTag);
                ATM.alignTurretToAT(blueTag);
            }

            //****** INTAKE (Controller 1 & 2)******
            // BUTTONS: X
            if (bot.isPressed(1, Button.X) || bot.isPressed(2, Button.X)) {
                bot.deleteTimer("RejectIntake"); // If we rejecting, stop it
                if (!bot.IntakeSpinning)
                    bot.startIntake();
                else
                    bot.killIntake();
            }

            //****** FLYWHEEL (Controller 2)******
            // BUTTONS: L_TRIGGER, L_BUMPER, R_TRIGGER, R_BUMPER
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.spinUpClose();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVel);
            }
            else if (bot.isHeld(2, Button.L_BUMPER)) {
                bot.spinUpFar();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVelFar);
            } else if (bot.isHeld(2, Button.R_BUMPER)) {
                bot.ejectFW();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", ejectionVel);
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** TURRET (Controller 2) ******
            // BUTTONS: A, DPAD_RIGHT, DPAD_LEFT
            if (bot.isPressed(2, Button.A))
                bot.resetTurret();
            else if (bot.isPressed(2, Button.DPAD_RIGHT))
                bot.moveTurretR();
            else if (bot.isPressed(2, Button.DPAD_LEFT))
                bot.moveTurretL();

            //****** SHOOT SEQUENCE (Controller 2)******
            // BUTTONS: R_TRIGGER
            if (bot.isPressed(2, Button.R_TRIGGER)) {
                if (bot.timers.get("ShootSequence") == null) { // Not in the middle of a sequence
                    bot.blockerOpen();
                    bot.createTimer("ShootSequence");
                }
                else {
                    bot.createTimer("ShootSequence", (long) gateWait);
                }
            }
            if (bot.timerExpired_MSeconds("ShootSequence", gateWait+shootSequenceWait)){
                bot.killFeeder();
                bot.transferArmDown(); // Start moving arm down
                bot.blockerClose();
            } else if (bot.timerExpired_MSeconds("ShootSequence", gateWait)){
                bot.spinFeeder();
                bot.transferArmUp();
                bot.killIntake(); // Dont was ball to move below the arm while its up
            }
            if (bot.timerExpired_MSeconds("ShootSequence", gateWait+1250)){
                bot.startIntake(); // Turn intake back on
                bot.deleteTimer("ShootSequence");
            }

            //****** EJECT BALLS (Controller 1 & 2)******
            // R_STICKPRESS
            if (bot.isPressed(2, Button.R_STICKPRESS) || bot.isPressed(1, Button.R_STICKPRESS)) {
                bot.ejectFeeder();
                bot.ejectIntake();
                bot.createTimer("Eject");
            }
            if (bot.timerExpired_MSeconds("Eject", 1500)){
                bot.killFeeder();
                bot.startIntake();
                bot.deleteTimer("Eject");
            }


            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.AprilTagManager;
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
            //****** DRIVE ******
            if (bot.isPressed(1, Button.B)) {
                bot.toggleSlow();
            }
            bot.RafiDrive(gamepad1);
            telemetry.addData("Slowed", "%b", bot.slowed);

            //****** ATM ******
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

            //****** INTAKE ******
            if (bot.isPressed(1, Button.X)) {
                bot.deleteTimer("RejectIntake");
                if (!bot.IntakeSpinning)
                    bot.startIntake();
                else
                    bot.killIntake();
            }

            if (bot.isPressed(1, Button.R_STICKPRESS)) {
                bot.rejectIntake();
                bot.createTimer("RejectIntake");
            }
            if (bot.timerExpired_MSeconds("RejectIntake", 1500)){
                bot.startIntake();
                bot.deleteTimer("RejectIntake");
            }

            //****** FLYWHEEL ******
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.spinUpClose();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVel);
            }
            else if (bot.isHeld(2, Button.L_BUMPER)) {
                bot.spinUpFar();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVelFar);
            } else if (bot.isHeld(2, Button.L_STICKPRESS)) {
                bot.spinBack();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", ejectionVel);
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** TURRET and HOOD ******
            if (bot.isPressed(2, Button.A))
                bot.resetTurret();
            else if (bot.isPressed(2, Button.DPAD_RIGHT))
                bot.moveTurretR();
            else if (bot.isPressed(2, Button.DPAD_LEFT))
                bot.moveTurretL();

            //****** SERVOS ******
            if (bot.isPressed(2, Button.R_TRIGGER)) {
                bot.spinFeeder();
                bot.createTimer("FeedServo");
            }
            if (bot.timerExpired_MSeconds("FeedServo", 1500)){
                bot.killFeeder();
                bot.deleteTimer("FeedServo");
            }

            if (bot.isPressed(2, Button.R_BUMPER)) {
                bot.ejectFeeder();
                bot.createTimer("FeedServoEject");
            }
            if (bot.timerExpired_MSeconds("FeedServoEject", 1500)){
                bot.killFeeder();
                bot.deleteTimer("FeedServoEject");
            }

            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

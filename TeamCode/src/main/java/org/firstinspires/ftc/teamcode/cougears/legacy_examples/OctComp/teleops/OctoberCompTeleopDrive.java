package org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.teleops;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.OldPresetConstants.*;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagManager;
@TeleOp(name="OctoberCompTeleop", group="Drive")
@Disabled
public class OctoberCompTeleopDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        boolean slowed = false;

        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
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
                slowed = !slowed;
            }
            if (!slowed) {
                bot.RafiDrive(gamepad1, 1);
            } else {
                bot.RafiDrive(gamepad1, slowMultiplier);
            }
            telemetry.addData("Slowed", "%b", slowed);

            //****** ATM ******
            if (bot.isHeld(1, Button.Y)) {
                ATM.alignToAT(redTag);
                ATM.alignToAT(blueTag);
            }

            //****** FLYWHEEL ******
            /* Toggle FW. May cause loss of points
            if (bot.isPressed(1, Button.L_TRIGGER)) {
                if (bot.FWSpinning) {
                    bot.spinUp();
                } else {
                    bot.spinDown();
                }
            }
            */
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.spinUp();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVel);
            }
            else if (bot.isHeld(2, Button.L_BUMPER)) {
                bot.spinUpFar();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVelFar);
            } else if (bot.isHeld(2, Button.R_BUMPER)) {
                bot.FW.setVelocity(1000);
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVelFar);
            } else {
                bot.spinDown();
            }


            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** SERVOS ******
//            if (bot.isPressed(2, Button.R_TRIGGER)) {
//                bot.GateServoUp = !bot.GateServoUp;
//                if (bot.GateServoUp) {
//                    bot.GateServoPush();
//                } else {
//                    bot.GateServoReset();
//                }
//            }
            if (bot.isPressed(2, Button.R_TRIGGER)) {
                bot.GateServoPush();
                bot.createTimer("GateServo");
            }
            if (bot.timerExpired_MSeconds("GateServo", 750)){
                bot.GateServoReset();
                bot.deleteTimer("GateServo");
            }
//            if (bot.isPressed(2, Button.X)) {
//                bot.PushServoUp = !bot.PushServoUp;
//                if (bot.PushServoUp) {
//                    bot.PushServoPush();
//                } else {
//                    bot.PushServoReset();
//                }
//            }
            if (bot.isPressed(2, Button.X)) {
                bot.PushServoPush();
                bot.createTimer("PushServo");
            }
            if (bot.timerExpired_MSeconds("PushServo", 250)){
                bot.PushServoReset();
                bot.deleteTimer("PushServo");
            }


            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

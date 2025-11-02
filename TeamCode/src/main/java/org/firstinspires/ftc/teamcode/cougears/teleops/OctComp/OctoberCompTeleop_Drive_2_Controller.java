package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.DC_ATM;
@TeleOp(name="OctoberCompTeleop_Drive", group="Drive")

public class OctoberCompTeleop_Drive_2_Controller extends LinearOpMode {

    @Override
    public void runOpMode() {
        boolean slowed = false;

        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        DC_ATM ATM = new DC_ATM(hardwareMap, telemetry, bot);
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
                bot.RafiDrive(gamepad1);
            } else {
                bot.SlowRafiDrive(gamepad1);
            }
            telemetry.addData("Slowed", "%b", slowed);
            telemetry.addData("Distance - blue", "%.2f", ATM.ATDist(blueTag));
            telemetry.addData("Distance - red", "%.2f", ATM.ATDist(redTag));


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
            }
            else
                bot.spinDown();

            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** SERVOS ******
            if (bot.isPressed(2, Button.R_TRIGGER)) {
                bot.GateServoUp = !bot.GateServoUp;
                if (bot.GateServoUp) {
                    bot.GateServoPush();
                } else {
                    bot.GateServoReset();
                }
            }
            if (bot.isPressed(2, Button.X)) {
                bot.PushServoUp = !bot.PushServoUp;
                if (bot.PushServoUp) {
                    bot.PushServoPush();
                } else {
                    bot.PushServoReset();
                }
            }


            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

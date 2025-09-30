package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="OctoberCompTeleop_Drive", group="Drive")

public class OctoberCompTeleop_Drive extends LinearOpMode {

    boolean spinOn = false;
    boolean intakeIsOn = false;
    @Override
    public void runOpMode() {
        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        // Initialize motors
        bot.botInit();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();


        while (opModeIsActive()) {
            bot.botDrive(gamepad1);

            //****** FLYWHEEL ******
            if(bot.isPressed(1, Button.X)) {
                spinOn = !spinOn;
                if(spinOn)
                    bot.spinUp();
                else
                    bot.spinDown();
            }
            //****** SERVOS ******
            if (gamepad1.left_bumper) {
                bot.FServoPush(); }
            else {
                bot.FServoReset();
            }
            if (gamepad1.right_bumper) {
                bot.BServoPush(); }
            else {
                bot.BServoReset();
            }

            telemetry.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

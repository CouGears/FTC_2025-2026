package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="OctoberCompTeleop_Drive", group="Drive")

public class OctoberCompTeleop_Drive extends LinearOpMode {

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
            bot.drive(gamepad1);

            //****** FLYWHEEL ******
            if (bot.isPressed(1, Button.X)) {
                bot.FWSpinning = !bot.FWSpinning;
                if (bot.FWSpinning)
                    bot.spinUp();
                else
                    bot.spinDown();
            }
            //****** SERVOS ******
            if (bot.isPressed(1, Button.L_BUMPER)) {
                bot.GateServoUp = !bot.GateServoUp;
                if (bot.GateServoUp) {
                    bot.GateServoPush();
                } else {
                    bot.GateServoReset();
                }
            }

            telemetry.update();
            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

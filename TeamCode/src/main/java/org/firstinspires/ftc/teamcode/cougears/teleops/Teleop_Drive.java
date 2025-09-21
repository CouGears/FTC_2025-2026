package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Teleop_Drive", group="Drive")

public class Teleop_Drive extends LinearOpMode {
    DC_Teleopbase bot = new DC_Teleopbase(hardwareMap, telemetry);

    @Override
    public void runOpMode() {
        // Initialize motors
        bot.botInit();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        boolean aIsPressed = false;
        boolean bIsPressed = false;
        boolean xIsPressed = false;
        boolean yIsPressed = false;
        boolean spinOn = false;
        boolean intakeIsOn = false;


        while (opModeIsActive()) {
            bot.botDrive(gamepad1);
            if(!xIsPressed && gamepad1.x) {
                spinOn = !spinOn;
                if(spinOn)
                    bot.spinUp();
                else
                    bot.spinDown();
            }
            if(!yIsPressed && gamepad1.y) {
                intakeIsOn = !intakeIsOn;
                if(intakeIsOn)
                    bot.intakeOn();
                else
                    bot.intakeOff();
            }

            aIsPressed = gamepad1.a;
            bIsPressed = gamepad1.b;
            xIsPressed = gamepad1.x;
            yIsPressed = gamepad1.y;
        }

        bot.endTeleOp();
    }
}

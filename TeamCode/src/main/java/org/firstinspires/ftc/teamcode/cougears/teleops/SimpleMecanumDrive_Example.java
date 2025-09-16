package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

@TeleOp(name="Simple Mecanum Drive Example", group="Drive")
public class SimpleMecanumDrive_Example extends LinearOpMode {

    BotBase bot = new BotBase(hardwareMap, telemetry);

    @Override
    public void runOpMode() {
        // Initialize motors
        bot.botInit();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            bot.botDrive(gamepad1);
        }

        bot.endTeleOp();
    }
}
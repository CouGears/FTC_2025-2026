package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

@TeleOp(name="DriveTrainTest", group="Testing")
public class DriveTrainTest extends LinearOpMode {

    // Define the different states our OpMode can be in. This makes the code much clearer.

    @Override
    public void runOpMode() {
        BotBase bot = new BotBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();
        telemetry.addData(">", "Ready to start.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            bot.motorFR.setPower(1);
            bot.motorBL.setPower(0);
            telemetry.addData(">", "FR");
            telemetry.update();
            sleep(1000);

            bot.motorFL.setPower(1);
            bot.motorFR.setPower(0);
            telemetry.addData(">", "FL");
            telemetry.update();
            sleep(1000);

            bot.motorBR.setPower(1);
            bot.motorFL.setPower(0);
            telemetry.addData(">", "BR");
            telemetry.update();
            sleep(1000);

            bot.motorBL.setPower(1);
            bot.motorBR.setPower(0);
            telemetry.addData(">", "BL");
            telemetry.update();
            sleep(1000);
        }
        bot.endTeleOp();
    }
}
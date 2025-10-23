package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "FlywheelServoTest", group = "testing")
public class FlywheelServoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, "motor1");

        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        boolean motorOn = false;
        boolean directionReversed = false;
        double powerPercent = 50; // start at 50%
        double powerStep = 3;     // each press changes by 3%

        telemetry.addLine("Ready to start: Motor1PowerControl");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Toggle motor on/off
            if (gamepad1.a) {
                motorOn = !motorOn;
                sleep(250);
            }

            // Toggle direction
            if (gamepad1.b) {
                directionReversed = !directionReversed;
                sleep(250);
            }

            // Increase power
            if (gamepad1.x) {
                powerPercent += powerStep;
                sleep(200);
            }

            // Decrease power
            if (gamepad1.y) {
                powerPercent -= powerStep;
                sleep(200);
            }

            // Clamp power between 0% and 100%
            powerPercent = Math.max(0, Math.min(powerPercent, 100));

            // Compute final power
            double power = (powerPercent / 100.0);
            if (directionReversed) power *= -1;

            // Apply or stop
            if (motorOn) {
                motor1.setPower(power);
            } else {
                motor1.setPower(0);
            }

            telemetry.addLine("Motor1 Power Test");
            telemetry.addData("Motor On", motorOn);
            telemetry.addData("Direction Reversed", directionReversed);
            telemetry.addData("Power (%)", powerPercent);
            telemetry.addData("Applied Power", motorOn ? power : 0);
            telemetry.update();
        }
    }
}

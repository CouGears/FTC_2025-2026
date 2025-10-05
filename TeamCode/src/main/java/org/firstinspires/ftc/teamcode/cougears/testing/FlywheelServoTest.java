package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


/*
MADE WITH AI
NEEDS TESTING
 */
@TeleOp(name = "FlywheelServoTest", group = "testing")
public class FlywheelServoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // --- Hardware mapping ---
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor motor2 = hardwareMap.get(DcMotor.class, "motor2");
        Servo servo1 = hardwareMap.get(Servo.class, "servo1");
        CRServo servo2 = hardwareMap.get(CRServo.class, "servo2");

        // --- Initial states ---
        double flywheelPower = 0.0;
        boolean flywheelsOn = false;
        boolean directionReversed = false;
        int speedPercent = 50; // start at 50%

        // Servo1 positions
        double servo1OpenPos = 0.5;  // neutral (open)
        double servo1ClosedPos = 1.0; // 90° right (closed)
        servo1.setPosition(servo1OpenPos);

        waitForStart();

        while (opModeIsActive()) {
            // --- Flywheel controls ---
            // A -> toggle flywheels on/off
            if (gamepad1.a) {
                flywheelsOn = !flywheelsOn;
                sleep(250); // debounce
            }

            // B -> reverse direction
            if (gamepad1.b) {
                directionReversed = !directionReversed;
                sleep(250);
            }

            // X -> increase speed by 10%
            if (gamepad1.x) {
                speedPercent = Math.min(100, speedPercent + 10);
                sleep(200);
            }

            // Y -> decrease speed by 10%
            if (gamepad1.y) {
                speedPercent = Math.max(10, speedPercent - 10);
                sleep(200);
            }

            // Compute flywheel power
            flywheelPower = flywheelsOn ? (speedPercent / 100.0) : 0.0;

            // Apply opposite directions
            double power1 = directionReversed ? -flywheelPower : flywheelPower;
            double power2 = -power1;

            motor1.setPower(power1);
            motor2.setPower(power2);

            // --- Servo1 (gate) controls ---
            if (gamepad1.right_bumper) {
                servo1.setPosition(servo1ClosedPos); // closed
            } else if (gamepad1.right_trigger > 0.5) {
                servo1.setPosition(servo1OpenPos); // open
            }

            // --- Servo2 (pusher) controls ---
            if (gamepad1.left_bumper) {
                servo2.setPower(0.5); // rotate at 50%
            } else if (gamepad1.left_trigger > 0.5) {
                servo2.setPower(0.0); // stop
            }

            // --- Telemetry ---
            telemetry.addData("Flywheels On", flywheelsOn);
            telemetry.addData("Direction Reversed", directionReversed);
            telemetry.addData("Speed (%)", speedPercent);
            telemetry.addData("Servo1 Pos", servo1.getPosition());
            telemetry.update();
        }
    }
}

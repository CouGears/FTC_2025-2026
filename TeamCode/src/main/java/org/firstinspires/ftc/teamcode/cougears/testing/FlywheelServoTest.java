package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "FlywheelServoTest", group = "testing")
public class FlywheelServoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // --- TUNABLE VARIABLES ---
        int speedMin = 10;                // minimum flywheel speed percent
        int speedMax = 100;               // maximum flywheel speed percent
        double flywheelStep = 2;          // how much speed changes per loop (right stick)
        double servoStep = 0.02;          // how much servo max changes per loop (left stick)

        // --- Hardware mapping ---
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor motor2 = hardwareMap.get(DcMotor.class, "motor2");
        Servo servo1 = hardwareMap.get(Servo.class, "servo1");
        Servo servo2 = hardwareMap.get(Servo.class, "servo2");

        // --- Initial states ---
        double flywheelPower = 0.0;
        boolean flywheelsOn = false;
        boolean directionReversed = false;
        int speedPercent = 50; // start at 50%
        double servo2Min = 0.0;
        double servo2Max = 0.2; // initial max open position
        double servo2Pos = servo2Min;

        // Servo1 positions (gate)
        double servo1OpenPos = 0.5;
        double servo1ClosedPos = 1.0;

        servo1.setPosition(servo1OpenPos);
        servo2.setPosition(servo2Pos);

        telemetry.addLine("Ready to start: FlywheelServoTest");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // --- Flywheel toggle ---
            if (gamepad1.a) {
                flywheelsOn = !flywheelsOn;
                sleep(250); // debounce
            }
            if (gamepad1.b) {
                directionReversed = !directionReversed;
                sleep(250);
            }

            // --- Right stick controls flywheel speed ---
            double stickYRight = -gamepad1.right_stick_y; // up is positive
            if (Math.abs(stickYRight) > 0.1) {           // deadzone
                speedPercent += stickYRight * flywheelStep;
                speedPercent = (int) Math.max(speedMin, Math.min(speedPercent, speedMax));
            }

            // Compute flywheel power
            flywheelPower = flywheelsOn ? (speedPercent / 100.0) : 0.0;
            double power1 = directionReversed ? -flywheelPower : flywheelPower;
            double power2 = -power1;
            motor1.setPower(power1);
            motor2.setPower(power2);

            // --- Servo1 (gate) ---
            if (gamepad1.right_bumper) {
                servo1.setPosition(servo1ClosedPos);
            } else if (gamepad1.right_trigger > 0.5) {
                servo1.setPosition(servo1OpenPos);
            }

            // --- Left stick Y modifies servo2 max position ---
            double stickYLeft = -gamepad1.left_stick_y; // up is positive
            if (Math.abs(stickYLeft) > 0.1) {          // deadzone
                servo2Max += stickYLeft * servoStep;
                servo2Max = Math.max(0.0, Math.min(servo2Max, 1.0)); // clamp 0..1
            }

            // --- Left bumper/trigger sets servo2 open/close ---
            if (gamepad1.left_bumper) {
                servo2Pos = servo2Min; // closed
            } else if (gamepad1.left_trigger > 0.5) {
                servo2Pos = servo2Max; // open
            }
            servo2.setPosition(servo2Pos);

            // --- Telemetry ---
            telemetry.addLine("Flywheel + Servo Test");
            telemetry.addData("Flywheels On", flywheelsOn);
            telemetry.addData("Direction Reversed", directionReversed);
            telemetry.addData("Speed (%)", speedPercent);
            telemetry.addData("Servo1 Pos", servo1.getPosition());
            telemetry.addData("Servo2 Pos", servo2Pos);
            telemetry.addData("Servo2 Max", servo2Max);
            telemetry.update();
        }
    }
}

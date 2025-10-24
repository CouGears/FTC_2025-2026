package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "FlywheelServoTest", group = "Testing")
public class FlywheelServoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // === DRIVE MOTORS ===
        DcMotorEx motorFL = hardwareMap.get(DcMotorEx.class, "motorFL");
        DcMotorEx motorFR = hardwareMap.get(DcMotorEx.class, "motorFR");
        DcMotorEx motorBL = hardwareMap.get(DcMotorEx.class, "motorBL");
        DcMotorEx motorBR = hardwareMap.get(DcMotorEx.class, "motorBR");

        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBR.setDirection(DcMotorSimple.Direction.FORWARD);

        motorFL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // === FLYWHEEL + SERVO ===
        DcMotorEx flywheel = hardwareMap.get(DcMotorEx.class, "motor1");
        Servo gateServo = hardwareMap.get(Servo.class, "servo1");

        flywheel.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        boolean motorOn = false;
        boolean directionReversed = false;
        double powerPercent = 50;
        double powerStep = 3;

        double gateServoMinDeg = 120;
        double gateServoMaxDeg = 160;
        double gateServoMin = gateServoMinDeg / 180.0;
        double gateServoMax = gateServoMaxDeg / 180.0;
        double gateServoPos = gateServoMin;
        gateServo.setPosition(gateServoPos);

        telemetry.addLine("Ready to start: Drive + Flywheel + Servo");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // === DRIVE CONTROL ===
            double x = gamepad1.right_stick_x;   // strafe (X direction)
            double y = -gamepad1.right_stick_y;  // forward/back (Y direction)
            double yaw = gamepad1.left_stick_x;  // rotation (left stick X)

            // Mecanum drive formula
            double frontLeftPower  = y + x + yaw;
            double frontRightPower = y - x - yaw;
            double backLeftPower   = y - x + yaw;
            double backRightPower  = y + x - yaw;

            // Normalize to keep all powers <= 1.0
            double max = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                    Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));
            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            // Apply motor power
            motorFL.setPower(Range.clip(frontLeftPower, -1.0, 1.0));
            motorFR.setPower(Range.clip(frontRightPower, -1.0, 1.0));
            motorBL.setPower(Range.clip(backLeftPower, -1.0, 1.0));
            motorBR.setPower(Range.clip(backRightPower, -1.0, 1.0));

            // === FLYWHEEL LOGIC ===
            if (gamepad1.a) {
                motorOn = !motorOn;
                sleep(250);
            }
            if (gamepad1.b) {
                directionReversed = !directionReversed;
                sleep(250);
            }
            if (gamepad1.x) {
                powerPercent += powerStep;
                sleep(200);
            }
            if (gamepad1.y) {
                powerPercent -= powerStep;
                sleep(200);
            }

            powerPercent = Math.max(0, Math.min(powerPercent, 100));
            double power = powerPercent / 100.0;
            if (directionReversed) power *= -1;
            flywheel.setPower(motorOn ? power : 0);

            // === SERVO LOGIC ===
            if (gamepad1.left_bumper) {
                gateServoPos = gateServoMin;
            } else if (gamepad1.right_bumper) {
                gateServoPos = gateServoMax;
            }
            gateServo.setPosition(gateServoPos);

            // === TELEMETRY ===
            telemetry.addLine("Drive + Flywheel + Servo Active");
            telemetry.addData("Drive", "X: %.2f  Y: %.2f  Yaw: %.2f", x, y, yaw);
            telemetry.addData("Motor On", motorOn);
            telemetry.addData("Direction Reversed", directionReversed);
            telemetry.addData("Power (%)", powerPercent);
            telemetry.addData("Applied Power", motorOn ? power : 0);
            telemetry.addData("Gate Servo Pos", gateServoPos);
            telemetry.addData("Gate Servo Degrees", gateServoPos * 180);
            telemetry.update();
        }

        // Stop everything when done
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
        flywheel.setPower(0);
    }
}

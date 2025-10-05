package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp(name="Servo Initialization Example", group="Examples")
public class ServoInitExample extends LinearOpMode {

    // Declare the servo
    private Servo myServo;

    @Override
    public void runOpMode() {

        // Initialize the servo from the hardware map
        myServo = hardwareMap.get(Servo.class, "myServo");

        // Set the initial position of the servo (0.0 to 1.0 range)
        myServo.setPosition(0.5); // middle position

        telemetry.addLine("Servo initialized. Waiting for start...");
        telemetry.update();

        // Wait for the driver to press PLAY
        waitForStart();

        while (opModeIsActive()) {
            // Example: Move servo based on gamepad buttons
            if (gamepad1.a) {
                myServo.setPosition(0.0); // move to minimum
            } else if (gamepad1.b) {
                myServo.setPosition(1.0); // move to maximum
            }

            // Show servo position
            telemetry.addData("Servo Position", myServo.getPosition());
            telemetry.update();
        }
    }
}

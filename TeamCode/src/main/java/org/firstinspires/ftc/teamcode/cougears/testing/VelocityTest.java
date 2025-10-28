package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

@TeleOp(name = "Velocity Test", group = "Testing")
public class VelocityTest extends LinearOpMode {

    // --- IMPORTANT: CHANGE THIS VALUE ---
    /**
     * This is the number of encoder ticks for ONE revolution of the motor's output shaft.
     * Find this value on your motor's datasheet or product page.
     * Examples:
     *   - goBILDA 5203 series (28 ticks/rev motor * gearbox ratio):
     *     - 1:1 ratio -> 28.0
     *     - 1:3.7 ratio -> 103.8
     *     - 1:5.2 ratio -> 145.1
     *   - REV HD Hex Motor (28 ticks/rev motor * gearbox ratio):
     *     - 1:20 ratio -> 560
     */
    public static final double TICKS_PER_REVOLUTION = 60; // Example for a goBILDA 1:1.9 motor

    // The amount to increase/decrease velocity by with each D-pad press
    public static final double VELOCITY_STEP = 100; // Ticks per second

    private DcMotorEx motor;
    private double targetVelocity = 0;
    private ElapsedTime debounceTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        try {
            motor = hardwareMap.get(DcMotorEx.class, "FW");
        } catch (Exception e) {
            telemetry.addData("Error", "Could not find motor named 'FW'. Check your configuration.");
            telemetry.update();
            sleep(5000);
            return;
        }

        // Set up the motor for velocity control
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Optional: Set some basic PIDF values to prevent a runaway motor.
        // You will need to TUNE these for your specific motor and flywheel setup.
        // A good starting point is a small P and an F calculated from the motor's max speed.
        // This is just a safe default for testing.
        motor.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);


        telemetry.addData("Status", "Initialized");
        telemetry.addData(">", "Press Play to begin.");
        telemetry.addData(">", "Use D-Pad Up/Down to change target velocity.");
        telemetry.update();

        waitForStart();
        debounceTimer.reset();

        while (opModeIsActive()) {
            motor.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);
            // --- Gamepad Controls to Adjust Target Velocity ---
            if (gamepad1.dpad_up && debounceTimer.milliseconds() > 200) {
                targetVelocity += VELOCITY_STEP;
                debounceTimer.reset();
            }
            if (gamepad1.dpad_down && debounceTimer.milliseconds() > 200) {
                targetVelocity -= VELOCITY_STEP;
                if (targetVelocity < 0) {
                    targetVelocity = 0; // Prevent negative velocity
                }
                debounceTimer.reset();
            }

            // Command the motor to run at the target velocity
            motor.setVelocity(targetVelocity);

            // --- Telemetry - The Core of the Test ---
            double actualVelocity = motor.getVelocity();

            // Convert Ticks per Second to Revolutions Per Minute (RPM) for easy comparison
            double targetRPM = (targetVelocity / TICKS_PER_REVOLUTION) * 60;
            double actualRPM = (actualVelocity / TICKS_PER_REVOLUTION) * 60;

            telemetry.addData("--- Velocity Test ---", "");
            telemetry.addData("Target (Ticks/Sec)", "%.2f", targetVelocity);
            telemetry.addData("Actual (Ticks/Sec)", "%.2f", actualVelocity);
            telemetry.addData(" ", ""); // Spacer
            telemetry.addData("Target (RPM)", "%.2f", targetRPM);
            telemetry.addData("Actual (RPM)", "%.2f", actualRPM);
            telemetry.addData(" ", ""); // Spacer
            telemetry.addData("Instructions", "Use D-Pad Up/Down to adjust target.");
            telemetry.update();
        }

        // Stop the motor when the OpMode ends
        motor.setPower(0);
    }
}
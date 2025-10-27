package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Encoder Test", group = "Testing")
public class EncoderTest extends LinearOpMode {

    private DcMotorEx FWMotor;

    @Override
    public void runOpMode() {
        // Initialize the motor directly from the hardware map
        try {
            FWMotor = hardwareMap.get(DcMotorEx.class, "FW");
        } catch (Exception e) {
            telemetry.addData("Error", "Could not find 'FW' motor in hardware map.");
            telemetry.update();
            sleep(5000);
            return;
        }

        // IMPORTANT: We reset the encoder to a known state (0)
        FWMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // We will run WITHOUT the encoder controller first to test the raw readings.
        FWMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialization Complete");
        telemetry.addData(">", "Press Play to start the test.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Apply a small, constant power to the motor
            // This bypasses the PIDF controller completely.
            FWMotor.setPower(0.3); // Spin at 30% power

            // --- THIS IS THE CRITICAL PART ---
            // We are reading the raw encoder ticks.
            int currentPosition = FWMotor.getCurrentPosition();
            double currentVelocity = FWMotor.getVelocity();

            telemetry.addData("--- Encoder Test ---", "");
            telemetry.addData("Motor Power", FWMotor.getPower());
            telemetry.addData(">>> Encoder Position (Ticks)", currentPosition);
            telemetry.addData(">>> Calculated Velocity (Ticks/Sec)", currentVelocity);
            telemetry.addData(">", "If Encoder Position is not changing rapidly, the encoder is not working.");
            telemetry.addData(">", "Press STOP to end the test.");
            telemetry.update();
        }

        // Stop the motor when the OpMode is finished
        FWMotor.setPower(0);
    }
}
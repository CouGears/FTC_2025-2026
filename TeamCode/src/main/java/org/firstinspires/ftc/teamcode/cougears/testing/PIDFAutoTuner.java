package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp(name = "PIDF Auto Tuner", group = "Testing")
public class PIDFAutoTuner extends LinearOpMode {

    private DcMotorEx motor;

    // PIDF PRESETS TO TEST
    public static final double[][] PIDF_PRESETS = {
            {20, 0, 2, 16.5},
            {30, 0, 3, 16.5},
            {40, 0, 4, 17.0},
            {50, 0, 5, 17.5}
    };

    // TEST MODES
    private enum TestMode {
        FASTEST_RAMP,
        CONSISTENCY,
        STEADY_STATE_ERROR,
        OVERSHOOT_CONTROL
    }
    private TestMode mode = TestMode.FASTEST_RAMP;

    // Auto-calculated test target
    private double testVelocity;

    @Override
    public void runOpMode() {

        try {
            motor = hardwareMap.get(DcMotorEx.class, "FW");
        } catch (Exception e) {
            telemetry.addLine("Motor 'FW' not found.");
            telemetry.update();
            sleep(3000);
            return;
        }

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // AUTO-DETECT MOTOR MAX RPM
        double maxRpm = motor.getMotorType().getMaxRPM();
        double maxTicksPerSec = (maxRpm / 60.0) * motor.getMotorType().getTicksPerRev();

        // TEST AT 70% OF MAX SPEED
        testVelocity = 0.70 * maxTicksPerSec;

        telemetry.addLine("PIDF Auto Tuner");
        telemetry.addLine("D-pad Left/Right: Select Test Metric");
        telemetry.addLine("A: Run All PIDF Tests");
        telemetry.addLine("");
        telemetry.addData("Detected Max RPM", maxRpm);
        telemetry.addData("Test Velocity (tps)", testVelocity);
        telemetry.addData("Current Mode", mode);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Change test mode
            if (gamepad1.dpad_left) {
                mode = previousMode(mode);
                sleep(200);
            }
            if (gamepad1.dpad_right) {
                mode = nextMode(mode);
                sleep(200);
            }

            // Run full auto-test
            if (gamepad1.a) {
                runAllTests();
            }

            telemetry.addData("Current Mode", mode);
            telemetry.addLine("Press A to test all PIDFs.");
            telemetry.update();
        }
    }

    // Mode switching helpers
    private TestMode previousMode(TestMode m) {
        switch (m) {
            case FASTEST_RAMP: return TestMode.OVERSHOOT_CONTROL;
            case CONSISTENCY: return TestMode.FASTEST_RAMP;
            case STEADY_STATE_ERROR: return TestMode.CONSISTENCY;
            case OVERSHOOT_CONTROL: return TestMode.STEADY_STATE_ERROR;
        }
        return TestMode.FASTEST_RAMP;
    }

    private TestMode nextMode(TestMode m) {
        switch (m) {
            case FASTEST_RAMP: return TestMode.CONSISTENCY;
            case CONSISTENCY: return TestMode.STEADY_STATE_ERROR;
            case STEADY_STATE_ERROR: return TestMode.OVERSHOOT_CONTROL;
            case OVERSHOOT_CONTROL: return TestMode.FASTEST_RAMP;
        }
        return TestMode.FASTEST_RAMP;
    }

    // RUN TEST FOR A SINGLE PIDF PRESET
    private double scorePreset(double[] pidf, TestMode mode) {
        motor.setVelocityPIDFCoefficients(pidf[0], pidf[1], pidf[2], pidf[3]);
        motor.setVelocity(testVelocity);

        // Allow ramp time
        ElapsedTime timer = new ElapsedTime();
        double startTime = timer.milliseconds();

        double lastVel = 0;
        double overshoot = 0;
        double sum = 0;
        double sumSq = 0;
        int samples = 0;
        boolean reached = false;

        while (timer.seconds() < 2.0 && opModeIsActive()) {
            double v = motor.getVelocity();

            // FASTEST_RAMP
            if (!reached && v >= testVelocity * 0.98) {
                reached = true;
                if (mode == TestMode.FASTEST_RAMP)
                    return timer.milliseconds() - startTime;
            }

            // OVERSHOOT_CONTROL
            if (v > testVelocity)
                overshoot = Math.max(overshoot, v - testVelocity);

            // STATS FOR CONSISTENCY + STEADY STATE
            if (timer.seconds() >= 1.0) {
                double err = v - testVelocity;
                sum += err * err;
                sumSq += v;
                samples++;
            }

            lastVel = v;
        }

        // SCORE BASED ON MODE
        switch (mode) {
            case STEADY_STATE_ERROR:
                return Math.abs(sumSq / samples - testVelocity); // avg abs error
            case CONSISTENCY:
                double mean = sumSq / samples;
                double variance = (sum / samples);
                return variance;
            case OVERSHOOT_CONTROL:
                return overshoot;
            default:
                return 999999; // should not happen
        }
    }

    // RUN ALL PRESETS, PICK WINNER
    private void runAllTests() {

        telemetry.clearAll();
        telemetry.addLine("Testing all PIDF presets...");
        telemetry.update();

        double bestScore = Double.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < PIDF_PRESETS.length; i++) {
            double score = scorePreset(PIDF_PRESETS[i], mode);

            telemetry.addData("Preset " + i + " Score", score);
            telemetry.update();

            if (score < bestScore) {
                bestScore = score;
                bestIndex = i;
            }

            motor.setVelocity(0);
            sleep(300);
        }

        telemetry.addLine("");
        telemetry.addLine("=== BEST PRESET ===");
        telemetry.addData("Preset Index", bestIndex);
        telemetry.addData("Score", bestScore);
        telemetry.addData("PIDF", "%f %f %f %f",
                PIDF_PRESETS[bestIndex][0],
                PIDF_PRESETS[bestIndex][1],
                PIDF_PRESETS[bestIndex][2],
                PIDF_PRESETS[bestIndex][3]);
        telemetry.update();

        // Apply best preset
        double[] p = PIDF_PRESETS[bestIndex];
        motor.setVelocityPIDFCoefficients(p[0], p[1], p[2], p[3]);
    }
}

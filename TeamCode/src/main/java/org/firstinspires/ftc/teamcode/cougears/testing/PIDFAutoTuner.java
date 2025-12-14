package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@TeleOp(name="PIDF Auto Tuner", group="Testing")
public class PIDFAutoTuner extends LinearOpMode {

    private class MotorEntry {
        String name;
        DcMotorEx motor;
        double maxRpm;
        double ticksPerRev;

        MotorEntry(String name, DcMotorEx motor) {
            this.name = name;
            this.motor = motor;
            try {
                this.maxRpm = motor.getMotorType().getMaxRPM();
                this.ticksPerRev = motor.getMotorType().getTicksPerRev();
            } catch (Exception e) {
                this.maxRpm = 6000;
                this.ticksPerRev = 28;
            }
        }
    }

    private ArrayList<MotorEntry> motors = new ArrayList<>();
    private int motorIndex = 0;
    private DcMotorEx motor;
    private MotorEntry selectedMotor;

    private enum TestMode {
        FASTEST_RAMP, CONSISTENCY, OVERSHOOT_CONTROL, STEADY_STATE_ERROR, BALANCED
    }

    private TestMode mode = TestMode.FASTEST_RAMP;
    private double maxTicksPerSec;
    private double testVelocity;
    private static final int DEBOUNCE = 200;
    private boolean upPrev, downPrev, leftPrev, rightPrev;

    @Override
    public void runOpMode() {
        // Auto-detect motors - FIXED: Added DcMotorEx.class parameter
        for (String name : hardwareMap.getAllNames(DcMotorEx.class)) {
            try {
                DcMotorEx m = hardwareMap.get(DcMotorEx.class, name);
                if (m != null) motors.add(new MotorEntry(name, m));
            } catch (Exception ignored) {}
        }

        if (motors.isEmpty()) {
            telemetry.addLine("No DcMotorEx motors found!");
            telemetry.update();
            waitForStart();
            return;
        }

        waitForStart();

        // Motor selection menu
        while (opModeIsActive() && !gamepad1.a) {
            MotorEntry entry = motors.get(motorIndex);
            telemetry.clearAll();
            telemetry.addLine("=== Select Motor ===");
            telemetry.addLine("Dpad Up/Down: change motor");
            telemetry.addLine("A: select motor");
            telemetry.addData("Motor", entry.name);
            telemetry.addData("Max RPM", entry.maxRpm);
            telemetry.update();

            if (gamepad1.dpad_down) {
                motorIndex = (motorIndex + 1) % motors.size();
                sleep(DEBOUNCE);
            }
            if (gamepad1.dpad_up) {
                motorIndex = (motorIndex - 1 + motors.size()) % motors.size();
                sleep(DEBOUNCE);
            }
        }

        selectedMotor = motors.get(motorIndex);
        motor = selectedMotor.motor;
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        maxTicksPerSec = selectedMotor.maxRpm / 60.0 * selectedMotor.ticksPerRev;
        testVelocity = 0.7 * maxTicksPerSec;

        telemetry.clearAll();
        telemetry.addLine("Selected Motor: " + selectedMotor.name);
        telemetry.addData("Max RPM", selectedMotor.maxRpm);
        telemetry.update();
        sleep(500);

        while (opModeIsActive()) {
            handleModeAndVelocity();

            telemetry.clearAll();
            telemetry.addLine("=== PIDF Auto-Tuner ===");
            telemetry.addData("Motor", selectedMotor.name);
            telemetry.addData("Mode", mode);
            telemetry.addData("Velocity", "%.0f tps (%.0f%%)", testVelocity, 100 * testVelocity / maxTicksPerSec);
            telemetry.addLine("Dpad L/R: change mode, Up/Down: velocity");
            telemetry.addLine("Triggers: RT=increase, LT=decrease velocity");
            telemetry.addLine("Press A: auto-tune all | B: tune current mode only");
            telemetry.update();

            if (gamepad1.a) runAutoTune();
            if (gamepad1.b) runSingleModeTune();
        }
    }

    private void handleModeAndVelocity() {
        if (gamepad1.dpad_left && !leftPrev) mode = prevMode(mode);
        if (gamepad1.dpad_right && !rightPrev) mode = nextMode(mode);

        // Triggers for velocity control (more precise/continuous)
        if (gamepad1.right_trigger > 0.1) {
            testVelocity = Math.min(maxTicksPerSec, testVelocity + (gamepad1.right_trigger * 50));
        }
        if (gamepad1.left_trigger > 0.1) {
            testVelocity = Math.max(0.1 * maxTicksPerSec, testVelocity - (gamepad1.left_trigger * 50));
        }

        // Dpad for velocity control (step increments)
        if (gamepad1.dpad_up && !upPrev) {
            testVelocity = Math.min(maxTicksPerSec, testVelocity * 1.05);
        }
        if (gamepad1.dpad_down && !downPrev) {
            testVelocity = Math.max(0.1 * maxTicksPerSec, testVelocity * 0.95);
        }

        leftPrev = gamepad1.dpad_left;
        rightPrev = gamepad1.dpad_right;
        upPrev = gamepad1.dpad_up;
        downPrev = gamepad1.dpad_down;
    }

    private TestMode prevMode(TestMode m) {
        switch (m) {
            case FASTEST_RAMP: return TestMode.BALANCED;
            case CONSISTENCY: return TestMode.FASTEST_RAMP;
            case OVERSHOOT_CONTROL: return TestMode.CONSISTENCY;
            case STEADY_STATE_ERROR: return TestMode.OVERSHOOT_CONTROL;
            case BALANCED: return TestMode.STEADY_STATE_ERROR;
        }
        return m;
    }

    private TestMode nextMode(TestMode m) {
        switch (m) {
            case FASTEST_RAMP: return TestMode.CONSISTENCY;
            case CONSISTENCY: return TestMode.OVERSHOOT_CONTROL;
            case OVERSHOOT_CONTROL: return TestMode.STEADY_STATE_ERROR;
            case STEADY_STATE_ERROR: return TestMode.BALANCED;
            case BALANCED: return TestMode.FASTEST_RAMP;
        }
        return m;
    }

    private void runAutoTune() {
        telemetry.clearAll();
        telemetry.addLine("Running full auto-tune (all parameters)...");
        telemetry.addLine("Note: I is always kept at 0 for flywheels");
        telemetry.update();

        double P = 10;
        double I = 0; // I is always 0 for flywheels
        double D = 0;
        double F = 15;

        // Tune P, D, and F (I stays at 0)
        P = tuneParameter("P", P, 0.5, P, I, D, F);
        D = tuneParameter("D", D, 0.5, P, I, D, F);
        F = tuneParameter("F", F, 0.5, P, I, D, F);

        motor.setVelocityPIDFCoefficients(P, I, D, F);
        showFinalPIDF(P, I, D, F);
    }

    private void runSingleModeTune() {
        telemetry.clearAll();
        telemetry.addLine("Running single-mode tune for: " + mode);
        telemetry.addLine("This will only optimize for your selected mode");
        telemetry.addLine("Note: I is always kept at 0 for flywheels");
        telemetry.update();
        sleep(1000);

        double P = 10;
        double I = 0; // I is always 0 for flywheels
        double D = 0;
        double F = 15;

        // Tune P, D, and F (I stays at 0)
        P = tuneParameter("P", P, 0.5, P, I, D, F);
        D = tuneParameter("D", D, 0.5, P, I, D, F);
        F = tuneParameter("F", F, 0.5, P, I, D, F);

        motor.setVelocityPIDFCoefficients(P, I, D, F);
        showFinalPIDF(P, I, D, F);
    }

    private double tuneParameter(String name, double param, double step, double P, double I, double D, double F) {
        double bestParam = param;
        double bestScore = score(P, I, D, F);

        while (opModeIsActive()) {
            double upParam = bestParam + step;
            double downParam = Math.max(0, bestParam - step);
            double scoreUp = 999999, scoreDown = 999999;

            switch (name) {
                case "P":
                    scoreUp = score(upParam, I, D, F);
                    scoreDown = score(downParam, I, D, F);
                    break;
                case "D":
                    scoreUp = score(P, I, upParam, F);
                    scoreDown = score(P, I, downParam, F);
                    break;
                case "F":
                    scoreUp = score(P, I, D, upParam);
                    scoreDown = score(P, I, D, downParam);
                    break;
            }

            if (scoreUp < bestScore) {
                bestScore = scoreUp;
                bestParam = upParam;
            } else if (scoreDown < bestScore) {
                bestScore = scoreDown;
                bestParam = downParam;
            } else break;

            telemetry.clearAll();
            telemetry.addData("Tuning", name);
            telemetry.addData("Best", bestParam);
            telemetry.addData("Score", bestScore);
            telemetry.update();
        }

        return bestParam;
    }

    private double score(double P, double I, double D, double F) {
        // Reset motor state before each test
        motor.setVelocity(0);
        sleep(2000); // Wait 2000ms for motor to stop
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        sleep(100); // Brief stabilization

        motor.setVelocityPIDFCoefficients(P, I, D, F);
        motor.setVelocity(testVelocity);
        ElapsedTime t = new ElapsedTime();

        double overshoot = 0;
        double sumSqErr = 0;
        double steadyVelSum = 0;
        int steadySamples = 0;
        boolean reached = false;

        while (t.seconds() < 2 && opModeIsActive()) {
            double v = motor.getVelocity();

            if (!reached && v > testVelocity * 0.98) {
                reached = true;
                if (mode == TestMode.FASTEST_RAMP) return t.milliseconds();
            }

            if (v > testVelocity) overshoot = Math.max(overshoot, v - testVelocity);

            if (t.seconds() > 1.0) {
                double err = v - testVelocity;
                sumSqErr += err * err;
                steadyVelSum += v;
                steadySamples++;
            }
        }

        // Stop motor after test
        motor.setVelocity(0);

        switch (mode) {
            case STEADY_STATE_ERROR:
                return Math.abs((steadyVelSum / steadySamples) - testVelocity);
            case CONSISTENCY:
                return sumSqErr / steadySamples;
            case OVERSHOOT_CONTROL:
                return overshoot;
            case BALANCED:
                // Weighted combination: 40% speed, 30% overshoot control, 30% consistency
                double rampTime = reached ? t.milliseconds() : 2000;
                double overshootPenalty = overshoot * 2; // Penalize overshoot
                double consistencyScore = sumSqErr / Math.max(steadySamples, 1);
                return (rampTime * 0.4) + (overshootPenalty * 0.3) + (consistencyScore * 0.3);
            default:
                return 999999;
        }
    }

    private void showFinalPIDF(double P, double I, double D, double F) {
        telemetry.clearAll();
        telemetry.addLine("=== Auto Tune Complete ===");
        telemetry.addData("Motor", selectedMotor.name);
        telemetry.addLine("");
        telemetry.addData("P", P);
        telemetry.addData("I", I);
        telemetry.addData("D", D);
        telemetry.addData("F", F);
        telemetry.addLine("Press A to continue...");
        telemetry.update();

        while (opModeIsActive() && !gamepad1.a) {
            sleep(10);
        }
    }
}
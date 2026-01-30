package org.firstinspires.ftc.teamcode.cougears.testing.Components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.bylazar.configurables.annotations.Configurable;


import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.*;
@Configurable
@TeleOp(name = "PIDF Manual Tuner", group = "Testing")
public class PIDFManualTuner extends OpMode {

    private DcMotorEx FW;

    public static double highVel = 1800;
    public static double lowVel = 1620;
    private double targetVel = highVel;

    private double P = 0.0;
    private double F = 0.0;

    private final double[] increments = {10.0, 1.0, 0.1, 0.01};
    private int incIndex = 0;

    private static final double MAX_P = 300.0;
    private static final double MAX_F = 150.0;

    @Override
    public void init() {
        FW = hardwareMap.get(DcMotorEx.class, "FW");
        FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        FW.setDirection(DcMotor.Direction.REVERSE);
        applyPIDF();
    }

    @Override
    public void loop() {

        if (gamepad1.yWasPressed()) {
            targetVel = (targetVel == highVel) ? lowVel : highVel;
        }

        if (gamepad1.aWasPressed()) {
            incIndex = (incIndex + 1) % increments.length;
        }
        if (gamepad1.bWasPressed()) {
            incIndex = (incIndex - 1 + increments.length) % increments.length;
        }

        double step = increments[incIndex];

        if (gamepad1.dpadUpWasPressed()) {
            P += step;
        }
        if (gamepad1.dpadDownWasPressed()) {
            P -= step;
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += step;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            F -= step;
        }

        P = clamp(P, 0, MAX_P);
        F = clamp(F, 0, MAX_F);

        applyPIDF();
        FW.setVelocity(targetVel);

        double vel = FW.getVelocity();

        telemetry.addData("Target", "%.1f", targetVel);
        telemetry.addData("Velocity", "%.1f", vel);
        telemetry.addData("Error", "%.1f", targetVel - vel);
        telemetry.addData("P", "%.4f", P);
        telemetry.addData("F", "%.4f", F);
        telemetry.addData("Step", "%.2f", step);
        telemetry.update();
    }

    private void applyPIDF() {
        FW.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(P, 0, 0, F)
        );
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}

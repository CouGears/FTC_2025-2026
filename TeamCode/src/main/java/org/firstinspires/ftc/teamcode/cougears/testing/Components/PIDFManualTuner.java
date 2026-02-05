package org.firstinspires.ftc.teamcode.cougears.testing.Components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.bylazar.configurables.annotations.Configurable;

import static org.firstinspires.ftc.teamcode.cougears.testing.Components.VelocityTest.TICKS_PER_REVOLUTION;

@Configurable
@TeleOp(name = "PIDF Manual Tuner", group = "Testing")
public class PIDFManualTuner extends OpMode {

    private DcMotorEx FW, Intake, Transfer;

    public static double highVel = 1800;
    public static double lowVel = 1620;
    private double targetVel = highVel;

    public static double P = 0.0;
    public static double F = 0.0;

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

        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        Intake.setDirection(DcMotor.Direction.REVERSE);
        Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Transfer = hardwareMap.get(DcMotorEx.class, "Transfer");
        Transfer.setDirection(DcMotor.Direction.FORWARD);
        Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        Transfer.setPower(0.7);
        Intake.setPower(0.7);
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

        if (gamepad1.right_trigger > 0.5) {
            highVel += 10;
        }
        if (gamepad1.rightBumperWasPressed()) {
            highVel -= 10;
        }
        if (gamepad1.left_trigger > 0.5) {
            lowVel += 10;
        }
        if (gamepad1.leftBumperWasPressed()) {
            lowVel -= 10;
        }

        P = clamp(P, 0, MAX_P);
        F = clamp(F, 0, MAX_F);

        applyPIDF();
        FW.setVelocity(targetVel);

        double vel = FW.getVelocity();
        double targetRPM = (targetVel / TICKS_PER_REVOLUTION) * 60;
        double RPM = (vel / TICKS_PER_REVOLUTION) * 60;

        telemetry.addData("Target Vel", "%.1f", targetVel);
        telemetry.addData("Velocity", "%.1f", vel);
        telemetry.addData("Target RPM", "%.1f", targetRPM);
        telemetry.addData("RPM", "%.1f", RPM);
        telemetry.addData("Error", "%.1f", targetVel - vel);
        telemetry.addData("P", "%.4f", P);
        telemetry.addData("F", "%.4f", F);
        telemetry.addData("Step", "%.2f", step);
        telemetry.addData("High Vel", "%.1f", highVel);
        telemetry.addData("Low Vel", "%.1f", lowVel);
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

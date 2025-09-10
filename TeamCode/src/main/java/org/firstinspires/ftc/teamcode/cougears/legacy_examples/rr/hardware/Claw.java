package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cougears.PresetConstants;

public class Claw {
    private Servo claw;

    public Claw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        claw.setPosition(PresetConstants.clawPresets[1]);
    }

    public class ClawTo implements Action {
        private final double targetPos;
        public ClawTo(double targetPos) {
            this.targetPos = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            claw.setPosition(targetPos);
            return false;
        }
    }

    public Action clawTo(double targetPos) {
        return new ClawTo(targetPos);
    }

    public Action closeClaw() {
        return new ClawTo(0.26);
    }

    public Action openClaw() {
        return new ClawTo(.5);
    }
}

package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cougears.PresetConstants;

public class Axis1 {
    private Servo axis1;

    public Axis1(HardwareMap hardwareMap) {
        axis1 = hardwareMap.get(Servo.class, "axis1");
        axis1.setPosition(PresetConstants.axis1Presets[0]);
    }

    public class Axis1To implements Action {
        private double targetPos;
        public Axis1To(double targetPos) {
            this.targetPos = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            axis1.setPosition(targetPos);
            return false;
        }
    }

    public Action axis1To(double targetPos) {
        return new Axis1To(targetPos);
    }

    public Action axis1ToLevel(int level) {
        return new Axis1To(PresetConstants.axis1Presets[level]);
    }
}

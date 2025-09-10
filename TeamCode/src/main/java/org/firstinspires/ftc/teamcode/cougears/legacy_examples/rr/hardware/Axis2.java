package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cougears.PresetConstants;

public class Axis2 {
    private Servo axis2;

    public Axis2(HardwareMap hardwareMap) {
        axis2 = hardwareMap.get(Servo.class, "axis2");
        axis2.setPosition(PresetConstants.axis2Presets[0]);
    }

    public class Axis2To implements Action {
        private double targetPos;
        public Axis2To(double targetPos) {
            this.targetPos = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            axis2.setPosition(targetPos);
            return false;
        }
    }

    public Action axis2To(double targetPos) {
        return new Axis2To(targetPos);
    }

    public Action axis2ToPreset(int preset) {
        return new Axis2To(PresetConstants.axis2Presets[preset]);
    }
}

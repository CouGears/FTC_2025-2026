package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.cougears.PresetConstants;

public class Arm {
    private DcMotor arm;

    public Arm(HardwareMap hardwareMap) {
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(1);
    }

    public class ArmToPatient implements Action {
        private boolean initialized = false;
        private int targetPos = 0;

        public ArmToPatient(int targetPos) {
            this.targetPos = targetPos;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            // powers on motor, if it is not on
            if (!initialized) {
                arm.setPower(0.2);
                arm.setTargetPosition(targetPos);
                initialized = true;
            }

            // checks lift's current position
            double pos = arm.getCurrentPosition();
            packet.put("liftPos", pos);
            if (Math.abs(pos - targetPos) >= 5) {
                // true causes the action to rerun
                return true;
            } else {
                // false stops action rerun
                arm.setPower(0.2);
                return false;
            }
        }
    }

    public Action armToPatient(int targetPos) {
        return new ArmToPatient(targetPos);
    }

    public class ArmToPatientCustomSpeed implements Action {
        private boolean initialized = false;
        private final int targetPos;

        private final double pwr;

        public ArmToPatientCustomSpeed(int targetPos, double pwr) {
            this.targetPos = targetPos;
            this.pwr = pwr;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            // powers on motor, if it is not on
            if (!initialized) {
                arm.setPower(pwr);
                arm.setTargetPosition(targetPos);
                initialized = true;
            }

            // checks lift's current position
            double pos = arm.getCurrentPosition();
            packet.put("liftPos", pos);
            return (Math.abs(pos - targetPos) >= 5);
        }
    }

    public Action armToPatientCustomSpeed(int targetPos, double pwr) {
        return new ArmToPatientCustomSpeed(targetPos, pwr);
    }

    public Action armToPatientSlow(int targetPos) {
        return new ArmToPatientCustomSpeed(targetPos, 0.05);
    }

    public class ArmToImpatient implements Action {
        private final int targetPos;

        public ArmToImpatient(int targetPos) {
            this.targetPos = targetPos;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm.setPower(1);
            arm.setTargetPosition(targetPos);
            return false;
        }
    }

    public Action armToImpatient(int targetPos) {
        return new ArmToImpatient(targetPos);
    }

    public class ArmToImpatientCustomSpeed implements Action {
        private final int targetPos;
        private final double pwr;

        public ArmToImpatientCustomSpeed(int targetPos, double pwr) {
            this.targetPos = targetPos;
            this.pwr = pwr;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm.setPower(pwr);
            arm.setTargetPosition(targetPos);
            return false;
        }
    }

    public Action armToImpatientCustomSpeed(int targetPos, double pwr) {
        return new ArmToImpatientCustomSpeed(targetPos, pwr);
    }

    public Action armToImpatientSlow(int targetPos) {
        return new ArmToImpatientCustomSpeed(targetPos, 0.05);
    }

    public Action armDownToLevelPatient(int level) {
        return new ArmToPatient(PresetConstants.armThetaPresets[level]);
    }

    public Action armUpToLevelPatient(int level) {
        if (level == 1) {
            return new ArmToPatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.1);
        }
        return new ArmToPatient(PresetConstants.armThetaPresets[level]);
    }

    public Action armDownToLevelImpatient(int level) {
        return new ArmToImpatient(PresetConstants.armThetaPresets[level]);
    }

    public Action armUpToLevelImpatient(int level) {
        if (level == 1) {
            return new ArmToImpatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.05);
        }
        return new ArmToImpatient(PresetConstants.armThetaPresets[level]);
    }

    public Action armToLevelPatientCustomSpeed(int level, double pwr) {
        return new ArmToPatientCustomSpeed(PresetConstants.armThetaPresets[level], pwr);
    }

    public Action armToLevelImpatientCustomSpeed(int level, double pwr) {
        return new ArmToImpatientCustomSpeed(PresetConstants.armThetaPresets[level], pwr);
    }

    public Action armToLevelPatientSlow(int level) {
        return new ArmToPatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.05);
    }

    public Action armToLevelImpatientSlow(int level) {
        return new ArmToImpatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.05);
    }

    public Action armToLevelPatientRegular(int level) {
        return new ArmToPatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.2);
    }

    public Action armToLevelImpatientRegular(int level) {
        return new ArmToImpatientCustomSpeed(PresetConstants.armThetaPresets[level], 0.2);
    }
}

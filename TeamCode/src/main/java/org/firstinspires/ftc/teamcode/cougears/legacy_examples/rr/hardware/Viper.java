package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.cougears.PresetConstants;

public class Viper {
    private final DcMotor slideLeft;
    private final DcMotor slideRight;

    public Viper(HardwareMap hardwareMap) {
        slideLeft = hardwareMap.get(DcMotor.class, "viperL");
        slideRight = hardwareMap.get(DcMotor.class, "viperR");
        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        slideRight.setDirection(DcMotorSimple.Direction.REVERSE);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(0);
        slideRight.setTargetPosition(0);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideLeft.setPower(1.0);
        slideRight.setPower(1.0);
    }

    public class ViperToPatient implements Action {
        private boolean initialized = false;
        private final int targetPos;

        public ViperToPatient(int targetPos) {
            this.targetPos = targetPos;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            // powers on motor, if it is not on
            if (!initialized) {
                slideLeft.setPower(1.0);
                slideRight.setPower(1.0);
                slideLeft.setTargetPosition(targetPos);
                slideRight.setTargetPosition(targetPos);
                initialized = true;
            }

            // checks lift's current position
            double pos = ((double) slideLeft.getCurrentPosition() +  (double) slideRight.getCurrentPosition())/2;
            packet.put("liftPos", pos);
            return (Math.abs(pos - targetPos) >= 10);
        }
    }

    public Action viperToPatient(int targetPos) {
        return new ViperToPatient(targetPos);
    }

    public class ViperToPatientCustomSpeed implements Action {
        private boolean initialized = false;
        private final int targetPos;

        private final double pwr;

        public ViperToPatientCustomSpeed(int targetPos, double pwr) {
            this.targetPos = targetPos;
            this.pwr = pwr;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            // powers on motor, if it is not on
            if (!initialized) {
                slideLeft.setPower(pwr);
                slideRight.setPower(pwr);
                slideLeft.setTargetPosition(targetPos);
                slideRight.setTargetPosition(targetPos);
                initialized = true;
            }

            // checks lift's current position
            double pos = ((double) slideLeft.getCurrentPosition() +  (double) slideRight.getCurrentPosition())/2;
            packet.put("liftPos", pos);
            return (Math.abs(pos - targetPos) >= 10);
        }
    }

    public Action viperToPatientCustomSpeed(int targetPos, double pwr) {
        return new ViperToPatientCustomSpeed(targetPos, pwr);
    }

    public class ViperToImpatient implements Action {
        private final int targetPos;

        public ViperToImpatient(int targetPos) {
            this.targetPos = targetPos;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            slideLeft.setPower(1);
            slideRight.setPower(1);
            slideLeft.setTargetPosition(targetPos);
            slideRight.setTargetPosition(targetPos);
            return false;
        }
    }

    public Action viperToImpatient(int targetPos) {
        return new ViperToImpatient(targetPos);
    }

    public class ViperToImpatientCustomSpeed implements Action {
        private final int targetPos;
        private final double pwr;

        public ViperToImpatientCustomSpeed(int targetPos, double pwr) {
            this.targetPos = targetPos;
            this.pwr = pwr;
        }

        // actions are formatted via telemetry packets as below
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            slideLeft.setPower(pwr);
            slideRight.setPower(pwr);
            slideLeft.setTargetPosition(targetPos);
            slideRight.setTargetPosition(targetPos);
            return false;
        }
    }

    public Action viperToLevelPatient(int level) {
        return new ViperToPatient(PresetConstants.slidePresets[level]);
    }

    public Action viperToLevelImpatient(int level) {
        return new ViperToImpatient(PresetConstants.slidePresets[level]);
    }

    public Action viperToLevelCustomSpeedPatient(int level, double pwr) {
        return new ViperToPatientCustomSpeed(PresetConstants.slidePresets[level], pwr);
    }

    public Action viperToLevelCustomSpeedImpatient(int level, double pwr) {
        return new ViperToImpatientCustomSpeed(PresetConstants.slidePresets[level], pwr);
    }
}

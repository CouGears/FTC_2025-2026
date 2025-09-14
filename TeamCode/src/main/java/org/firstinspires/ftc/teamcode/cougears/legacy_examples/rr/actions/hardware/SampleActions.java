package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.actions.hardware;

import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.actions.logic.Wait;
import org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware.*;

public class SampleActions {
    private final Viper viper;
    private final Arm arm;
    private final Claw claw;
    private final Axis1 axis1;
    private final Axis2 axis2;
//    private final MecanumDrive drive;
    private final Wait wait;

    public SampleActions(MecanumDrive drive, Viper viper, Arm arm, Claw claw, Axis1 axis1, Axis2 axis2) {
        this.viper = viper;
        this.arm = arm;
        this.claw = claw;
        this.axis1 = axis1;
        this.axis2 = axis2;
//        this.drive = drive;
        this.wait = new Wait(drive);
    }

    // Grab sample from field (center) (start and end at level 3)
    public SequentialAction grabSample() {
        return new SequentialAction(
                claw.openClaw(),
                axis1.axis1ToLevel(4),
                arm.armToLevelPatientCustomSpeed(4, 0.15),
                claw.closeClaw(),
                wait.waitSeconds(0.3),
                axis1.axis1ToLevel(3),
                arm.armUpToLevelImpatient(2)
        );
    }

    // Grab sample from field (angled) (start and end at level 3)
    // 0: center, 1: half left, 2: full left, 3: half right, 4: full right
    public SequentialAction grabSampleAtAngle(int angle) {
        return new SequentialAction(
                claw.openClaw(),
                axis2.axis2ToPreset(angle),
                axis1.axis1ToLevel(4),
                arm.armToLevelPatientCustomSpeed(4, 0.15),
                claw.closeClaw(),
                wait.waitSeconds(0.3),
                axis1.axis1ToLevel(3),
                axis2.axis2ToPreset(0),
                arm.armUpToLevelImpatient(2)
        );
    }

    // Drop sample in high bucket
    public SequentialAction dropSampleInHighBucket() {
        return new SequentialAction(
//                arm.armUpToLevelImpatient(2),
//                wait.waitMillieconds(250),
                axis1.axis1ToLevel(2),
                viper.viperToLevelPatient(2),
                axis1.axis1ToLevel(1),
                arm.armUpToLevelPatient(1),
                wait.waitSeconds(0.5),
                claw.openClaw(),
                wait.waitSeconds(0.5),
                arm.armDownToLevelImpatient(2),
                wait.waitMillieconds(250),
                viper.viperToPatient(3),
                arm.armDownToLevelImpatient(2),
                axis1.axis1ToLevel(2)
        );
    }

    // Drop sample in low bucket
    // Robot will be positioned on the field for high bucket
    // You may need to move forwards and then back or extend arm and axis1 all the way out to reach the bucket
    // TODO
    public SequentialAction dropSampleInLowBucket() {
        return new SequentialAction(
                wait.waitSeconds(0)
        );
    }
}

package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.actions.sequence;

import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.actions.drive.TAB;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.actions.hardware.SampleActions;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.Arm;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.Axis1;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.Axis2;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.Claw;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.Viper;

public class RBHardcoded {
    // Starts at RBI
    // Scores all nearby neutral field samples in high bucket
    // Parks in E6
    public static SequentialAction AutonRB1(MecanumDrive drive, Viper viper, Arm arm, Claw claw, Axis1 axis1, Axis2 axis2) {
        TAB tab = new TAB(drive);
        SampleActions sampleActions = new SampleActions(drive, viper, arm, claw, axis1, axis2);

        return new SequentialAction(
                tab.RBItoFS9().build(),
                sampleActions.grabSample(),
                tab.FS9toRB().build(),
                sampleActions.dropSampleInHighBucket(),
                tab.RBtoFS8().build(),
                sampleActions.grabSampleAtAngle(3),
                tab.FS8toRB().build(),
                sampleActions.dropSampleInHighBucket(),
                arm.armToPatient(0) // set arm up for teleop
//                tab.RBtoFS7().build(),
//                sampleActions.grabSampleAtAngle(2),
//                tab.FS7toRB().build(),
//                sampleActions.dropSampleInHighBucket(),
//                tab.RBtoPARK_E6().build()
        );
    }
}

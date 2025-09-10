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
public class ROHardcoded {
    // Starts at RBI
    // Scores all nearby neutral field samples in high bucket
    // Parks in E6
    public static SequentialAction AutonRB1(MecanumDrive drive, Viper viper, Arm arm, Claw claw, Axis1 axis1, Axis2 axis2) {
        TAB tab = new TAB(drive);
        SampleActions sampleActions = new SampleActions(drive, viper, arm, claw, axis1, axis2);

        return new SequentialAction(
                /*
            axis1.axis1To(0.8),
            arm.armToImpatient(300),
            tab.ROItoPrepose1().build(),
            arm.armToImpatient(400),
            wait.waitMillieconds(250),
            tab.RSubPrepose1toRSubPostpose1().build(),

            tab.RSubPostpose1toFS10().build(),
            sampleActions.grabSampleAtAngle(2),
            tab.FS10toRO().build(),
            specimenActions.placeSpecimen(),

            tab.ROtoFS11().build(),
            sampleActions.grabSampleAtAngle(2),
            tab.FS11toRO().build(),
            specimenActions.placeSpecimen(),

            tab.ROtoFS12().build(),
            sampleActions.grabSampleAtAngle(1),
            tab.FS12toRO().build(),
            specimenActions.placeSpecimen(),
            tab.ROtoE6().build(),

            // Find a way to wait

            specimenActions.grabPreparedSpecimen(),
            axis1.axis1To(0.8),
            arm.armToImpatient(300),
            tab.E6toRSubPreppose2().build(),
            arm.armToImpatient(400),
            wait.waitMillieconds(250),
           `tab.RSubPreppose2toRSubPostpose2().build(),
            tab.RSubPostpose2toE6().build(),

            specimenActions.grabPreparedSpecimen(),
            axis1.axis1To(0.8),
            arm.armToImpatient(300),
            tab.E6toRSubPreppose3().build(),
            arm.armToImpatient(400),
            wait.waitMillieconds(250),
            tab.RSubPreppose3toRSubPostpose3().build(),
            tab.RSubPostpose3toE6().build(),

            specimenActions.grabPreparedSpecimen(),
            axis1.axis1To(0.8),
            arm.armToImpatient(300),
            tab.E6toRSubPreppose4().build(),
            arm.armToImpatient(400),
            wait.waitMillieconds(250),
           `tab.RSubPreppose4toRSubPostpose4().build(),
            tab.RSubPostpose4toE6().build(),

            tab.RSubPostpose4toRO.build(); // Park?
            */
        );
    }
}
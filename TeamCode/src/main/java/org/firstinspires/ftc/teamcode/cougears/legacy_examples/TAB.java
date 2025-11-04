package org.firstinspires.ftc.teamcode.cougears.legacy_examples;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
public class TAB {
    private final MecanumDrive drive;

    // Init Poses
    // red bucket init
    public static final Pose2d RBIpose = new Pose2d(-33.0, -63.0, Math.toRadians(0.0));
    // blue bucket init
    public static final Pose2d BBIpose = new Pose2d(33.0, 63.0, Math.toRadians(180.0));

    // red observation zone init
    public static final Pose2d ROIpose = new Pose2d(9.0, -63.0, Math.toRadians(0.0));
    // blue observation zone init
    public static final Pose2d BOIpose = new Pose2d(-9.0, 63.0, Math.toRadians(180.0));


    // Bucket Poses
    public static final Pose2d RBpose = new Pose2d(-54.0, -54.0, Math.toRadians(135));
    public static final Pose2d BBpose = new Pose2d(50.0, 50.0, Math.toRadians(225));

    // Field Sample Poses
    public static final Pose2d FS1pose = new Pose2d(-62.0, 25.5, Math.toRadians(90));
    public static final Pose2d FS2pose = new Pose2d(-52.0, 30.0, Math.toRadians(135));
    public static final Pose2d FS3pose = new Pose2d(-48.0, 40.0, Math.toRadians(180));

    public static final Pose2d FS4pose = new Pose2d(48.0, 40.0, Math.toRadians(180));
    public static final Pose2d FS5pose = new Pose2d(52.0, 30.0, Math.toRadians(225));
    public static final Pose2d FS6pose = new Pose2d(62.0, 25.5, Math.toRadians(270));

    public static final Pose2d FS7pose = new Pose2d(-62.0, -25.5, Math.toRadians(90));
    public static final Pose2d FS8pose = new Pose2d(-47.0, -37.0, Math.toRadians(45));
    public static final Pose2d FS9pose = new Pose2d(-48.0, -40.0, Math.toRadians(0));

    public static final Pose2d FS10pose = new Pose2d(40.0, -40.0, Math.toRadians(315));
    public static final Pose2d FSX10pose = new Pose2d(40.0, -55, Math.toRadians(225));
    public static final Pose2d FSXX10pose = new Pose2d(28.5, -61.0, Math.toRadians(270));
    public static final Pose2d FS11pose = new Pose2d(52.0, -30.0, Math.toRadians(315));
    public static final Pose2d FS12pose = new Pose2d(60.0, -25.5, Math.toRadians(270));


    public static final Pose2d parkedE6pose = new Pose2d(36.0, -60.0, Math.toRadians(0));
    public static final Pose2d parkedF6pose = new Pose2d(60.0, -60.0, Math.toRadians(0));
    public static final Pose2d parkedA1pose = new Pose2d(-60.0, 60.0, Math.toRadians(180));
    public static final Pose2d parkedB1pose = new Pose2d(-36.0, 60.0, Math.toRadians(180));

    public static final Pose2d RSubPreppose1 = new Pose2d(0.0, -34.0, Math.toRadians(0));
    public static final Pose2d RSubPostpose1 = new Pose2d(0.0, -48.0, Math.toRadians(0));

    public static final Pose2d RSubPreppose2 = new Pose2d(3, -34.0, Math.toRadians(0));
    public static final Pose2d RSubPostpose2 = new Pose2d(3, -48.0, Math.toRadians(0));

    public static final Pose2d RSubPreppose3 = new Pose2d(9, -34.0, Math.toRadians(0));
    public static final Pose2d RSubPostpose3 = new Pose2d(9, -48.0, Math.toRadians(0));

    public static final Pose2d ROWaiting = new Pose2d(25.5, -60, Math.toRadians(270));

    public TAB(MecanumDrive drive) {
        this.drive = drive;
    }

    public TrajectoryActionBuilder ROItoRSubPreppose1() {
        return drive.actionBuilder(ROIpose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(RSubPreppose1, Math.toRadians(90));
    }

    public TrajectoryActionBuilder RSubPreppose1toRSubPostpose1() {
        return drive.actionBuilder(RSubPreppose1)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(RSubPostpose1, Math.toRadians(270));
    }
    public TrajectoryActionBuilder FSXX10toRSubPreppose2() {
        return drive.actionBuilder(FSXX10pose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(RSubPreppose2, Math.toRadians(90));
    }
    public TrajectoryActionBuilder RSubPreppose3toRSubPostpose3() {
        return drive.actionBuilder(RSubPreppose3)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(RSubPostpose3, Math.toRadians(270));
    }

    public TrajectoryActionBuilder FSXX10toRSubPreppose3() {
        return drive.actionBuilder(FSXX10pose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(RSubPreppose3, Math.toRadians(90));
    }
    public TrajectoryActionBuilder RSubPreppose2toRSubPostpose2() {
        return drive.actionBuilder(RSubPreppose3)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(RSubPostpose3, Math.toRadians(270));
    }
    public TrajectoryActionBuilder RSubPostpose1toFS10() {
        return drive.actionBuilder(RSubPostpose1)
                .setReversed(false)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(FS10pose, Math.toRadians(315));
    }

    public TrajectoryActionBuilder FS10toFSX10() {
        return drive.actionBuilder(FS10pose)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(FSX10pose, Math.toRadians(270));
    }

    public TrajectoryActionBuilder FSX10toFSXX10() {
        return drive.actionBuilder(FSX10pose)
                .setReversed(false)
                .setTangent(Math.toRadians(225))
                .splineToLinearHeading(ROWaiting, Math.toRadians(180))
                .setTangent(0)
                .splineToLinearHeading(FSXX10pose, Math.toRadians(0));
    }

    public TrajectoryActionBuilder RSubPostpose2toFSXX10() {
        return drive.actionBuilder(RSubPostpose2)
                .setReversed(false)
                .setTangent(Math.toRadians(225))
                .splineToLinearHeading(ROWaiting, Math.toRadians(180))
                .setTangent(0)
                .splineToLinearHeading(FSXX10pose, Math.toRadians(0));
    }

    public TrajectoryActionBuilder FSX10toROWaiting() {
        return drive.actionBuilder(FSX10pose)
                .setReversed(false)
                .setTangent(Math.toRadians(225))
                .splineToLinearHeading(ROWaiting, Math.toRadians(180));
    }

    public TrajectoryActionBuilder ROWaitingtoFSXX10() {
        return drive.actionBuilder(ROWaiting)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(FSXX10pose, Math.toRadians(0));
    }

    // Red Bucket Init to Field Sample 9
    public TrajectoryActionBuilder RBItoFS9() {
        return drive.actionBuilder(RBIpose)
                .setReversed(true)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(FS9pose, Math.toRadians(90));
    }

    // Field Sample 9 to Red Bucket
    public TrajectoryActionBuilder FS9toRB() {
        return drive.actionBuilder(FS9pose)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(RBpose, Math.toRadians(225));
    }

    // Red Bucket to Field Sample 8
    public TrajectoryActionBuilder RBtoFS8() {
        return drive.actionBuilder(RBpose)
                .setReversed(false)
                .setTangent(Math.toRadians(45))
                .splineToLinearHeading(FS8pose, Math.toRadians(135));
    }

    // Field Sample 8 to Red Bucket
    public TrajectoryActionBuilder FS8toRB() {
        return drive.actionBuilder(FS8pose)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(RBpose, Math.toRadians(225));
    }



    //*********NOT USED***********
    // Red Bucket to Field Sample 7
    public TrajectoryActionBuilder RBtoFS7() {
        return drive.actionBuilder(RBpose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(FS7pose, Math.toRadians(180));
    }

    // Field Sample 7 to Red Bucket
    public TrajectoryActionBuilder FS7toRB() {
        return drive.actionBuilder(FS7pose)
                .setReversed(false)
                .setTangent(Math.toRadians(315))
                .splineToLinearHeading(RBpose, Math.toRadians(225));
    }

    // Blue Bucket Init to Field Sample 4
    public TrajectoryActionBuilder BBItoFS4() {
        return drive.actionBuilder(BBIpose)
                .setReversed(true)
                .setTangent(270)
                .splineToLinearHeading(FS4pose, Math.toRadians(270));
    }

    // Field Sample 4 to Blue Bucket
    public TrajectoryActionBuilder FS4toBB() {
        return drive.actionBuilder(FS4pose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(BBpose, Math.toRadians(45));
    }

    // Blue Bucket to Field Sample 5
    public TrajectoryActionBuilder BBtoFS5() {
        return drive.actionBuilder(BBpose)
                .setReversed(false)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(FS5pose, Math.toRadians(315));
    }

    // Field Sample 5 to Blue Bucket
    public TrajectoryActionBuilder FS5toBB() {
        return drive.actionBuilder(FS5pose)
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(BBpose, Math.toRadians(45));
    }

    // Blue Bucket to Field Sample 6
    public TrajectoryActionBuilder BBtoFS6() {
        return drive.actionBuilder(BBpose)
                .setReversed(false)
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(FS6pose, Math.toRadians(0));
    }

    // Field Sample 6 to Blue Bucket
    public TrajectoryActionBuilder FS6toBB() {
        return drive.actionBuilder(FS6pose)
                .setReversed(false)
                .setTangent(Math.toRadians(135))
                .splineToLinearHeading(BBpose, Math.toRadians(45));
    }

    // Red Bucket to parked in E6
    public TrajectoryActionBuilder RBtoPARK_E6() {
        return drive.actionBuilder(RBpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedE6pose, Math.toRadians(0));
    }

    // Red Bucket to parked in F6
    public TrajectoryActionBuilder RBtoPARK_F6() {
        return drive.actionBuilder(RBpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedF6pose, Math.toRadians(0));
    }

    // Red Bucket Init to parked in E6
    public TrajectoryActionBuilder RBItoPARK_E6() {
        return drive.actionBuilder(RBIpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedE6pose, Math.toRadians(0));
    }

    // Red Bucket Init to parked in F6
    public TrajectoryActionBuilder RBItoPARK_F6() {
        return drive.actionBuilder(RBIpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedF6pose, Math.toRadians(0));
    }

    // Red Observation Zone Init to parked in E6
    public TrajectoryActionBuilder ROItoPARK_E6() {
        return drive.actionBuilder(ROIpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedE6pose, Math.toRadians(0));
    }

    // Red Observation Zone Init to parked in F6
    public TrajectoryActionBuilder ROItoPARK_F6() {
        return drive.actionBuilder(ROIpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedF6pose, Math.toRadians(0));
    }

    public TrajectoryActionBuilder ROItoRSub1() {
        return drive.actionBuilder(ROIpose)
                .setReversed(false)
                .setTangent(0)
                .splineToLinearHeading(parkedF6pose, Math.toRadians(0));
    }


}

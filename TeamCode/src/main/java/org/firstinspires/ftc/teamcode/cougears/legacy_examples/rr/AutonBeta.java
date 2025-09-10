package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.cougears.autonomous.rr.hardware.*;


@Disabled
@Autonomous(name="Auton Beta", group="Robot")
public class AutonBeta extends LinearOpMode {
    /* Declare OpMode members. */
    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;


    // slide, armtheta, axis1, axis2, claw
    int[] states = new int[5];

    int claw = 1;
    int level = 3;
    int clawLevel = 3;

    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;

    @Override
    public void runOpMode() {
        // device mapping
        motorFL = hardwareMap.get(DcMotor.class, "motorFL");
        motorFR = hardwareMap.get(DcMotor.class, "motorFR");
        motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        motorBR = hardwareMap.get(DcMotor.class, "motorBR");


        // device setup
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBR.setDirection(DcMotorSimple.Direction.FORWARD);

        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose2d beginPose = new Pose2d(-33.0, -63.0, 0.0);
        PinpointDrive drive = new PinpointDrive(hardwareMap, beginPose);
        Viper viper  = new Viper(hardwareMap);
        Arm arm = new Arm(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Axis1 axis1 = new Axis1(hardwareMap);
        Axis2 axis2 = new Axis2(hardwareMap);
//        SampleActions sampleActions = new SampleActions(drive, viper, arm, claw, axis1, axis2);

        // start pos to field sample 9
//        TrajectoryActionBuilder tab1 = drive.actionBuilder(beginPose)
//                .setReversed(true)
//                .setTangent(90)
//                .splineToConstantHeading(new Vector2d(-48.0, -40.0), Math.toRadians(90));
//        // field sample 9 to bucket
//        TrajectoryActionBuilder tab2 = tab1.endTrajectory().fresh()
//                .setReversed(false)
//                .setTangent(Math.toRadians(270))
//                .strafeTo(new Vector2d(-48.0, -48.0))
//                .turnTo(Math.toRadians(135))
//                .endTrajectory();
//        TrajectoryActionBuilder tab3 = tab2.endTrajectory().fresh()
//                .setTangent(0)
//                .splineToLinearHeading(new Pose2d(-50, -36, Math.toRadians(45)), Math.toRadians(135));
//
//        TrajectoryActionBuilder tab4 = tab3.endTrajectory().fresh()
//                .setTangent(0)
//                .splineToLinearHeading(new Pose2d(-56, -47, Math.toRadians(135)), Math.toRadians(225));
//
//        TrajectoryActionBuilder tab5 = tab4.endTrajectory().fresh()
//                .setTangent(0)
//                .splineToLinearHeading(new Pose2d(-65, -24, Math.toRadians(90)), Math.toRadians(180));
//
//        TrajectoryActionBuilder tab6 = tab5.endTrajectory().fresh()
//                .setReversed(true)
//                .setTangent(0)
//                .splineToLinearHeading(new Pose2d(-56, -47, Math.toRadians(135)), Math.toRadians(225));
//
//        Action tabsig = drive.actionBuilder(beginPose)
//                .turn(Math.toRadians(10800))
//                .build();
//
//        SequentialAction hardcodedAuton = new SequentialAction(
//                tab1.build(),
//                sampleActions.grabSample(),
//                tab2.build(),
//                sampleActions.dropSampleInHighBucket()
//        );



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();

//        Actions.runBlocking(NeutralFieldMarkersToHighBucket(drive, viper, arm, claw, axis1, axis2));
    }

    // slides, arm, axis1, axis2, claw
//    private void setStates(int slides, int arm, double axis1, double axis2, double claw) {
//
//            slideLeft.setTargetPosition(slides);
//            slideRight.setTargetPosition(slides);
//            slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            double power = 0.2;
//            armThetaDC.setTargetPosition(arm);
//            armThetaDC.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            armThetaDC.setPower(power);
//
//            clawAxis1Servo.setPosition(axis1);
//
//            clawAxis2Servo.setPosition(axis2);
//
//            clawGrabServo.setPosition(claw);
//    }
//
//    private void setStates(int slides, int arm, double axis1, double axis2, double claw, double pwr) {
//
//        slideLeft.setTargetPosition(slides);
//        slideRight.setTargetPosition(slides);
//        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        double power = 0.2;
//        armThetaDC.setTargetPosition(arm);
//        armThetaDC.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        armThetaDC.setPower(power);
//
//        clawAxis1Servo.setPosition(axis1);
//
//        clawAxis2Servo.setPosition(axis2);
//
//        clawGrabServo.setPosition(claw);
//    }
}

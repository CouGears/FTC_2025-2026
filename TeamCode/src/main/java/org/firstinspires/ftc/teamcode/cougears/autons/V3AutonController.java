package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V3AutonController {

    public DcMotorEx FW, Intake, Turret;
    public CRServo Transfer;
    public Servo TransferArm, Blocker;
    public boolean IntakeSpinning;

    HardwareMap HM;
    Telemetry tele;


    public V3AutonController(HardwareMap HardwareMap, Telemetry Telemetry) {
        HM = HardwareMap;
        tele = Telemetry;
    }

    public boolean botInit() {
        try {
            FW = HM.get(DcMotorEx.class, "FW");
            FW.setDirection(DcMotor.Direction.REVERSE);
            FW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);


            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Transfer = HM.get(CRServo.class, "Transfer");
            Transfer.setDirection(CRServo.Direction.FORWARD);

            Blocker = HM.get(Servo.class, "Blocker");
            Blocker.setPosition(Servo_blockerPos[0]);


        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }
        return true;
    }

    //****** FLYWHEELS ******
    public void spinUpClose() {
        FW.setVelocity(FW_shootVel);
    }
    public void spinUpFar() {
        FW.setVelocity(FW_shootVelFar);
    }
    public void killFW() {
        FW.setPower(0);
    }
    public void ejectFW() {
        FW.setPower(FW_ejectionVel);
    }

    //****** SERVOS ******
    public void spinFeeder(){
        Transfer.setPower(1);
    }
    public void ejectFeeder(){
        Transfer.setPower(-1);
    }
    public void killFeeder(){
        Transfer.setPower(0);
    }

    public void blockerOpen(){
        Blocker.setPosition(Servo_blockerPos[1]);
    }
    public void blockerClose(){
        Blocker.setPosition(Servo_blockerPos[0]);
    }


    //****** INTAKE ******
    public void startIntake() {
        Intake.setPower(Drive_intakePower);
        IntakeSpinning = true;
    }
    public void killIntake() {
        Intake.setPower(0);
        IntakeSpinning = false;
    }

    public void ejectIntake() {
        Intake.setPower(-1);
        IntakeSpinning = false; // So next time you press X it starts spinning in
    }

    //****** AUTON ******
    public void moveToPose(Follower f, Pose targetPose){
        f.followPath(
                f.pathBuilder()
                        .addPath(new BezierLine(f.getPose(), targetPose))
                        .setLinearHeadingInterpolation(f.getHeading(), targetPose.getHeading())
                        .build()
        );
    }

    //****** OTHER ******
    public void endAuton(Follower follower, String color){
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Turret.setPower(0);
        Storage.Storage_endOfAutonPose = follower.getPose();
        Storage.Storage_endOfAutonColor = color;
    }

    //****** PEDRO ******
}

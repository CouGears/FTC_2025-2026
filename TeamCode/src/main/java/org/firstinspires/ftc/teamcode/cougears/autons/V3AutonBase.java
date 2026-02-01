package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Drive_intakePower;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Drive_transferPower;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_ejectionVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Servo_blockerPos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V3AutonBase {
    //Initializing motors
    public DcMotorEx FW, Intake, Transfer;
    public Servo Blocker;
    //initializing toggles
    public boolean IntakeSpinning;

    HardwareMap HM;
    Telemetry tele;

    public V3AutonBase(HardwareMap HardwareMap, Telemetry Telemetry) {
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

            Transfer = HM.get(DcMotorEx.class, "Transfer");
            Transfer.setDirection(DcMotor.Direction.FORWARD);
            Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

    public void killFW() {
        FW.setPower(0);
    }
    public void ejectFW() {
        FW.setPower(FW_ejectionVel);
    }
    public boolean FWUpToSpeed (double speed) {
        return FW.getVelocity() >= speed;
    }
    public void FWSpinTo(double speed){
        FW.setVelocity(speed);
    }

    public void openBlocker(){
        Blocker.setPosition(Servo_blockerPos[1]);
    }
    public void closeBlocker(){
        Blocker.setPosition(Servo_blockerPos[0]);
    }
    public boolean blockerIsOpen() {return Blocker.getPosition() > Servo_blockerPos[1] - .05; }

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
    //****** Transfer ******
    public void startTransfer() { Transfer.setPower(Drive_transferPower);}
    public void killTransfer() { Transfer.setPower(0);}
    public void ejectTransfer() { Transfer.setPower(-1);}

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
        Storage.Storage_endOfAutonPose = follower.getPose();
        Storage.Storage_endOfAutonColor = color;
    }

}

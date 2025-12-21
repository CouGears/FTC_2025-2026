package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Servo_blockerPos;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_ejectionVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Drive_intakePower;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_shootVelFar;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Turret_ticksPerDeg;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Servo_transferArmPos;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Turret_turretLimits;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Turret_turretStep;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class V2AutonController {

    public DcMotorEx FW, Intake, Turret;
    public CRServo Transfer;
    public Servo TransferArm, Blocker;
    public boolean IntakeSpinning;

    HardwareMap HM;
    Telemetry tele;


    public V2AutonController(HardwareMap HardwareMap, Telemetry Telemetry) {
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

            Turret = HM.get(DcMotorEx.class, "TurretRotator");
            Turret.setDirection(DcMotor.Direction.REVERSE);
            Turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Turret.setTargetPosition(0);
            Turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Turret.setPower(1);

            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Transfer = HM.get(CRServo.class, "Transfer");
            Transfer.setDirection(CRServo.Direction.FORWARD);

            TransferArm = HM.get(Servo.class, "TransferArm");
            TransferArm.setPosition(Servo_transferArmPos[0]);

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

    //****** TURRET ******
    public void setTurretPosManual(int pos){
        pos = Range.clip(pos, Turret_turretLimits[0], Turret_turretLimits[1]);
        Turret.setTargetPosition(pos);
    }

    public void resetTurret(){
        Turret.setTargetPosition(0);
    }

    public void moveTurretL(){
        int newPos = Turret.getCurrentPosition() + Turret_turretStep;
        newPos = Math.min(newPos, Turret_turretLimits[1]);
        Turret.setTargetPosition(newPos);
    }
    public void moveTurretR(){
        int newPos = Turret.getCurrentPosition() - Turret_turretStep;
        newPos = Math.max(newPos, Turret_turretLimits[0]);
        Turret.setTargetPosition(newPos);
    }

    public void adjustTurret(double degAdjust) {
        int currentTicks = Turret.getCurrentPosition();
        int deltaTicks   = (int)(degAdjust * Turret_ticksPerDeg);
        int targetTicks  = currentTicks + deltaTicks;

        targetTicks = Range.clip(targetTicks, Turret_turretLimits[0], Turret_turretLimits[1]);
        Turret.setTargetPosition(targetTicks);
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

    public void transferArmUp(){
        TransferArm.setPosition(Servo_transferArmPos[1]);
    }
    public void transferArmDown(){
        TransferArm.setPosition(Servo_transferArmPos[0]);
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
    public void endAuton(){
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Turret.setPower(0);
    }

    //****** PEDRO ******
}

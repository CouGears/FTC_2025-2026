package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.blockerPos;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.ejectionVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.intakePower;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVelFar;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.ticksPerDeg;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.transferArmPos;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.turretLimits;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.turretStep;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
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
            TransferArm.setPosition(transferArmPos[0]);

            Blocker = HM.get(Servo.class, "Blocker");
            Blocker.setPosition(blockerPos[0]);


        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }
        return true;
    }

    //****** FLYWHEELS ******
    public void spinUpClose() {
        FW.setVelocity(shootVel);
    }
    public void spinUpFar() {
        FW.setVelocity(shootVelFar);
    }
    public void killFW() {
        FW.setPower(0);
    }
    public void ejectFW() {
        FW.setPower(ejectionVel);
    }

    //****** TURRET ******
    public void setTurretPosManual(int pos){
        pos = Range.clip(pos, turretLimits[0], turretLimits[1]);
        Turret.setTargetPosition(pos);
    }

    public void resetTurret(){
        Turret.setTargetPosition(0);
    }

    public void moveTurretL(){
        int newPos = Turret.getCurrentPosition() + turretStep;
        newPos = Math.min(newPos, turretLimits[1]);
        Turret.setTargetPosition(newPos);
    }
    public void moveTurretR(){
        int newPos = Turret.getCurrentPosition() - turretStep;
        newPos = Math.max(newPos, turretLimits[0]);
        Turret.setTargetPosition(newPos);
    }

    public void adjustTurret(double degAdjust) {
        int currentTicks = Turret.getCurrentPosition();
        int deltaTicks   = (int)(degAdjust * ticksPerDeg);
        int targetTicks  = currentTicks + deltaTicks;

        targetTicks = Range.clip(targetTicks, turretLimits[0], turretLimits[1]);
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
        TransferArm.setPosition(transferArmPos[1]);
    }
    public void transferArmDown(){
        TransferArm.setPosition(transferArmPos[0]);
    }

    public void blockerOpen(){
        Blocker.setPosition(blockerPos[1]);
    }
    public void blockerClose(){
        Blocker.setPosition(blockerPos[0]);
    }


    //****** INTAKE ******
    public void startIntake() {
        Intake.setPower(intakePower);
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

    //****** OTHER ******
    public void endAuton(){
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Turret.setPower(0);
    }

    //****** PEDRO ******
}

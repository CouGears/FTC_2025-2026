package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_ballTransferWait;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_firstShotExtraSpinupWait;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_gateWait;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_numberOfRepeatShots;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_pushNewBallWait;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_spinupWait;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Auton_transferResetWait;
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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V2AutonController {

    public DcMotorEx FW, Intake, Turret;
    public CRServo Transfer;
    public Servo TransferArm, Blocker;
    public boolean IntakeSpinning;
    public Follower follower;

    // Shoot sequence tracking
    public enum ShootSequence {
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL
    }
    private ShootSequence currentShootStep = ShootSequence.SPINUP;
    private int numShots = 0;
    private Timer shootTimer;

    HardwareMap HM;
    Telemetry tele;

    // Callback interface for when shooting is complete
    public interface ShootingCompleteCallback {
        void onShootingComplete();
    }
    private ShootingCompleteCallback shootingCompleteCallback;

    public V2AutonController(HardwareMap HardwareMap, Telemetry Telemetry) {
        HM = HardwareMap;
        tele = Telemetry;
        shootTimer = new Timer();
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
        IntakeSpinning = false;
    }

    //****** COMMON SHOOTING SEQUENCE ******
    /**
     * Start the shooting sequence from the beginning
     * @param callback Optional callback to be called when shooting is complete
     */
    public void startShootingSequence(ShootingCompleteCallback callback) {
        this.shootingCompleteCallback = callback;
        currentShootStep = ShootSequence.SPINUP;
        shootTimer.resetTimer();
    }

    /**
     * Reset the shot counter (call this when you want to start a new set of shots)
     */
    public void resetShotCounter() {
        numShots = 0;
    }

    /**
     * Get the current number of shots fired in this sequence
     */
    public int getNumShots() {
        return numShots;
    }

    /**
     * Update the shooting sequence - call this every loop iteration
     * @param follower The Pedro follower instance
     * @return true if shooting sequence is still running, false if complete
     */
    public boolean updateShootingSequence(Follower follower) {
        switch (currentShootStep) {
            case SPINUP:
                if (!follower.isBusy()) {
                    spinUpClose();
                    if (numShots == 0 && shootTimer.getElapsedTime() >= Auton_spinupWait + Auton_firstShotExtraSpinupWait) {
                        setShootStep(ShootSequence.OPEN);
                    } else if (numShots > 0 && shootTimer.getElapsedTime() >= Auton_spinupWait) {
                        setShootStep(ShootSequence.OPEN);
                    }
                }
                return true;

            case OPEN:
                blockerOpen();
                killIntake();
                if (shootTimer.getElapsedTime() >= Auton_gateWait) {
                    setShootStep(ShootSequence.SHOOT);
                }
                return true;

            case SHOOT:
                if (!follower.isBusy()) {
                    transferArmUp();
                    spinFeeder();
                    if (shootTimer.getElapsedTime() >= Auton_ballTransferWait) {
                        setShootStep(ShootSequence.CLOSE);
                        numShots++;
                    }
                }
                return true;

            case CLOSE:
                if (!follower.isBusy()) {
                    blockerClose();
                    transferArmDown();
                    killFeeder();
                    if (numShots >= Auton_numberOfRepeatShots) {
                        // Shooting sequence complete
                        if (shootingCompleteCallback != null) {
                            shootingCompleteCallback.onShootingComplete();
                        }
                        return false; // Sequence complete
                    } else if (shootTimer.getElapsedTime() >= Auton_transferResetWait) {
                        setShootStep(ShootSequence.PUSH_NEW_BALL);
                    }
                }
                return true;

            case PUSH_NEW_BALL:
                startIntake();
                if (shootTimer.getElapsedTime() >= Auton_pushNewBallWait) {
                    setShootStep(ShootSequence.SPINUP);
                }
                return true;

            default:
                return false;
        }
    }

    /**
     * Internal method to change shoot sequence steps
     */
    private void setShootStep(ShootSequence newStep) {
        currentShootStep = newStep;
        shootTimer.resetTimer();
    }

    /**
     * Get the current shoot sequence step (for telemetry/debugging)
     */
    public ShootSequence getCurrentShootStep() {
        return currentShootStep;
    }

    /**
     * Check if the shooting sequence is currently running
     */
    public boolean isShootingSequenceActive() {
        return currentShootStep != null;
    }

    //****** OTHER ******
    public void endAuton(String color){
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Turret.setPower(0);
        Storage.endOfAutonPose = follower.getPose();
        Storage.endOfAutonColor = color;
    }
}
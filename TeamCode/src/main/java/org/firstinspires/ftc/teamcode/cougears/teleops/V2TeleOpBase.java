package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVelFar;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

public class V2TeleOpBase extends BotBase {

    public DcMotorEx FW, Intake, TurretRotator, Hood;
    public CRServo Transfer;
    public boolean IntakeSpinning, FeedServoSpinning, slowed;
    public int currTurretPos = 0;

    public double speedMultiplier = 1;

    public V2TeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            FW = HM.get(DcMotorEx.class, "FW");
            FW.setDirection(DcMotor.Direction.REVERSE);
            FW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);

            TurretRotator = HM.get(DcMotorEx.class, "TurretRotator");
            TurretRotator.setDirection(DcMotor.Direction.REVERSE);
            TurretRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            TurretRotator.setTargetPosition(0);
            TurretRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            TurretRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            TurretRotator.setPower(1);

            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Transfer = HM.get(CRServo.class, "Transfer");
            Transfer.setDirection(CRServo.Direction.FORWARD);
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
    public void spinBack() {
        FW.setPower(ejectionVel);
    }

    //****** TURRET ******
    public void setTurretPosManual(int pos){
        pos = Range.clip(pos, turretLimits[0], turretLimits[1]);
        TurretRotator.setTargetPosition(pos);
    }

    public void moveTurretL(){
        int newPos = TurretRotator.getCurrentPosition() + turretStep;
        newPos = Math.min(newPos, turretLimits[1]);
        TurretRotator.setTargetPosition(newPos);
    }
    public void moveTurretR(){
        int newPos = TurretRotator.getCurrentPosition() - turretStep;
        newPos = Math.max(newPos, turretLimits[0]);
        TurretRotator.setTargetPosition(newPos);
    }

    public void adjustTurret(double targetDeg) { // TODO: Mkae sure bot dosent break itself going too far
        targetDeg = Range.clip(targetDeg, 0, 360);  // Or your physical limits

        double targetTicks = targetDeg * ticksPerDeg;
        targetTicks = Range.clip(targetTicks, turretLimits[0], turretLimits[1]);

        TurretRotator.setTargetPosition((int) targetTicks);
    }


    public void resetTurret(){
        TurretRotator.setTargetPosition(0);
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

    public void toggleFeedServo()  {
        FeedServoSpinning = !FeedServoSpinning;
        if (FeedServoSpinning){
            Transfer.setPower(1);
        } else {
            Transfer.setPower(0);
        }
    }


    //****** INTAKE ******
    public void toggleIntake() {
        IntakeSpinning = !IntakeSpinning;
        if (IntakeSpinning)
            Intake.setPower(1);
        else
            Intake.setPower(0);
    }
    public void startIntake() {
        Intake.setPower(1);
        IntakeSpinning = true;
    }
    public void killIntake() {
        Intake.setPower(0);
        IntakeSpinning = false;
    }

    public void rejectIntake() {
        Intake.setPower(-1);
        IntakeSpinning = false; // So next time you press X it starts spinning in
    }

    //****** OTHER ******
    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        TurretRotator.setPower(0);
//        Hood.setPower(0);
    }

    public void toggleSlow(){
        slowed = !slowed;
    }

    public void RafiDrive(Gamepad gamepad1) {
        if (!slowed){
            speedMultiplier = 1;
        } else {
            speedMultiplier = slowMultiplier;
        }
        speedMultiplier = -1*Range.clip(speedMultiplier,0, 1);

        tele.addData(">", "RUNNING RAFI DRIVE");
        double forward =  gamepad1.right_stick_y * speedMultiplier;
        double strafe  =  gamepad1.right_stick_x * speedMultiplier;
        double turn    =  gamepad1.left_stick_x * speedMultiplier;

        // Mecanum drive calculations for a LEFT-side motor reversal configuration.
        // These formulas are different from the standard right-side reversal.
        double frontLeftPower  = forward - strafe - turn;
        double frontRightPower = forward + strafe + turn;
        double backLeftPower   = forward + strafe - turn;
        double backRightPower  = forward - strafe + turn;

        // Normalize the motor powers to ensure no value exceeds 1.0
        double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower  /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower   /= maxPower;
            backRightPower  /= maxPower;
        }

        // Set the power for each motor
        motorFL.setPower(frontLeftPower);
        motorFR.setPower(frontRightPower);
        motorBL.setPower(backLeftPower);
        motorBR.setPower(backRightPower);
    }
}

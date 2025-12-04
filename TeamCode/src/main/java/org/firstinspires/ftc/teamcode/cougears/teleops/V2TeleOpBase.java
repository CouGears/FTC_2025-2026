package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVelFar;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

public class V2TeleOpBase extends BotBase {

    public DcMotorEx FW, Intake, TurretRotator, Hood;
    public CRServo Transfer1, Transfer2;
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
            FW.setDirection(DcMotor.Direction.FORWARD);
            FW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);

            Hood = HM.get(DcMotorEx.class, "HoodController");
            Hood.setDirection(DcMotor.Direction.REVERSE);
            Hood.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Hood.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Hood.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            TurretRotator = HM.get(DcMotorEx.class, "TurretRotator");
            TurretRotator.setDirection(DcMotor.Direction.REVERSE);
            TurretRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            TurretRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            TurretRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Transfer1 = HM.get(CRServo.class, "Transfer1");
            Transfer1.setDirection(CRServo.Direction.REVERSE);
            Transfer2 = HM.get(CRServo.class, "Transfer2");
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
        currTurretPos = -1;
    }

    public void moveTurretL(){
        int newPos = TurretRotator.getCurrentPosition() + turretStep;
        TurretRotator.setTargetPosition(newPos);
        currTurretPos = -1;
    }
    public void moveTurretR(){
        int newPos = TurretRotator.getCurrentPosition() - turretStep;
        TurretRotator.setTargetPosition(newPos);
        currTurretPos = -1;
    }

    public void setTurretPos(int posNumber){
        posNumber = Range.clip(posNumber, 0, 3);
        TurretRotator.setTargetPosition(turretPos[posNumber]);
        currTurretPos = posNumber;
    }

    public void resetTurret(){
        currTurretPos = 0;
        TurretRotator.setTargetPosition(turretPos[currTurretPos]);
    }

    //****** SERVOS ******
    public void spinFeeder(){
        Transfer1.setPower(1);
        Transfer2.setPower(1);
    }
    public void killFeeder(){
        Transfer1.setPower(0);
        Transfer2.setPower(0);
    }

    public void toggleFeedServo()  {
        FeedServoSpinning = !FeedServoSpinning;
        if (FeedServoSpinning){
            Transfer1.setPower(1);
            Transfer2.setPower(1);
        } else {
            Transfer1.setPower(0);
            Transfer2.setPower(0);
        }
    }


    //****** Intake ******
    public void toggleIntake() {
        IntakeSpinning = !IntakeSpinning;
        if (IntakeSpinning)
            Intake.setPower(1);
        else
            Intake.setPower(0);
    }
    public void rejectIntake() {
        IntakeSpinning = true;
        Intake.setPower(-1);
    }

    //****** OTHER ******
    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
        Intake.setPower(0);
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

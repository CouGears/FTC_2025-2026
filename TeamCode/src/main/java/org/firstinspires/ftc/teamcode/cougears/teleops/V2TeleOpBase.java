package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVelFar;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

public class V2TeleOpBase extends BotBase {

    public DcMotorEx FW, Intake, TurretRotator, Hood, FeedMotor;
    public boolean IntakeSpinning, FeedMotorSpinning, slowed;
    public int currTurretPos = 0;

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
            TurretRotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            TurretRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            FeedMotor = HM.get(DcMotorEx.class, "FeedMotor");
            FeedMotor.setDirection(DcMotor.Direction.REVERSE);
            FeedMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
    public void spinDown() {
        FW.setPower(0);
    }
    public void spinBack() {
        FW.setPower(ejectionVel);
    }

    //****** TURRET ******
    public void setTurretPos(int posNumber){
        posNumber = Range.clip(posNumber, 0, 3);
        TurretRotator.setTargetPosition(TurretPos[posNumber]);
        currTurretPos = posNumber;
    }
    public void nextTurretPos(){
        if (currTurretPos == 3)
            currTurretPos = 0;
        else
            currTurretPos++;
        TurretRotator.setTargetPosition(TurretPos[currTurretPos]);
    }

    //****** FEED MOTOR ******
    public void toggleFeedMotor() {
        FeedMotorSpinning = !FeedMotorSpinning;
        if (FeedMotorSpinning)
            FeedMotor.setPower(1);
        else
            FeedMotor.setPower(0);
    }

    //****** Intake ******
    public void toggleIntake() {
        IntakeSpinning = !IntakeSpinning;
        if (IntakeSpinning)
            Intake.setPower(1);
        else
            Intake.setPower(0);
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
        if (!slowed)
            RafiDrive(gamepad1, 1);
        else
            RafiDrive(gamepad1, slowMultiplier);
    }
    public void RafiDrive(Gamepad gamepad1, double speedMultiplier) {
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

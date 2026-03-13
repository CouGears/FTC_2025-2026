package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.DriveAdjustmentConstants.XMultiplier;
import static org.firstinspires.ftc.teamcode.cougears.util.DriveAdjustmentConstants.YMultiplier;
import static org.firstinspires.ftc.teamcode.cougears.util.DriveAdjustmentConstants.rotationMultiplier;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.cougears.util.SensorFusionManager;


public class V3TeleOpBase extends BotBase { //Initializing motors
    public DcMotorEx FW, Intake, Transfer;
    public Servo Blocker;


    //initializing toggles
    public boolean IntakeSpinning, slowed;
    //intializing speed multplier for slowdrive
    public double speedMultiplier = 1;
    //Initialize heading stuff
    private double targetHeadingDeg = 0.0;
    private boolean headingLocked = false;

    // Tunables
    private static final double HEADING_kP = 0.02;
    private static final double DRIVE_DEADBAND = 0.05;

    //initialize classes
    public GoBildaPinpointDriver pinpoint;

    public V3TeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public void setTelemetry(Telemetry t) {
        this.tele = t;
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

            Intake = HM.get(DcMotorEx.class, "Intake");
            Intake.setDirection(DcMotor.Direction.REVERSE);
            Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Transfer = HM.get(DcMotorEx.class, "Transfer");
            Transfer.setDirection(DcMotor.Direction.FORWARD);
            Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Blocker = HM.get(Servo.class, "Blocker");
            Blocker.setPosition(Servo_blockerPos[0]);

            pinpoint = HM.get(GoBildaPinpointDriver.class, "pinpoint");

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
    public void startIntakeSlow() {
        Intake.setPower(Drive_intakePowerSlow);
        IntakeSpinning = true;
    }
    public void startIntakeFast() {
        Intake.setPower(Drive_intakePowerFast);
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
    public void startTransferFar() { Transfer.setPower(Drive_transferPowerFar);}
    public void transferSmart(SensorFusionManager.ballState ballNum, Boolean toggleIntake) {
        if (toggleIntake) {
            if (!(ballNum == SensorFusionManager.ballState.NO_BALLS)) {
                startTransfer();
            } else {
                killTransfer();
            }
        }
    }
    ElapsedTime pulseTimer = new ElapsedTime();
    boolean shooting = false;
    public void pulseTransfer(double MSToCycle) {
        if (pulseTimer.milliseconds() >= Auton_transferPulseWaitMS){
            if (!shooting) {
                startTransfer();
                shooting = true;
            } else {
                killTransfer();
                shooting = false;
            }
            pulseTimer.reset();
        }
    }
    public void killTransfer() { Transfer.setPower(0);}
    public void ejectTransfer() { Transfer.setPower(-1);}

    //***SENSORs

            //****** OTHER ******
    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
        Intake.setPower(0);
    }

    public void toggleSlow(){
        slowed = !slowed;
    }
    public void RafiDrive(Gamepad gamepad1, boolean switchSticks) {

        // --- Speed scaling ---
        if (!slowed) {
            speedMultiplier = 1;
        } else {
            speedMultiplier = Drive_slowMultiplier;
        }
        speedMultiplier = -Range.clip(speedMultiplier, 0, 1);

        // --- Update odometry ---
        pinpoint.update();

        double forward, strafe, turnInput;
        // --- Driver inputs ---
        if (!switchSticks) {
            forward = gamepad1.right_stick_y * speedMultiplier * YMultiplier;
            strafe = gamepad1.right_stick_x * speedMultiplier * XMultiplier;
            turnInput = gamepad1.left_stick_x * speedMultiplier * rotationMultiplier;
        } else {
            forward = gamepad1.left_stick_y * speedMultiplier;
            strafe = gamepad1.left_stick_x * speedMultiplier;
            turnInput = gamepad1.right_stick_x * speedMultiplier;
        }

        double turn;

        double currentHeading =
                pinpoint.getPosition().getHeading(AngleUnit.DEGREES);

        // Are we actually translating?
        boolean driving =
                Math.abs(forward) > DRIVE_DEADBAND ||
                        Math.abs(strafe)  > DRIVE_DEADBAND;

        // --- Heading assist logic ---
        if (!driving) {
            // Idle → zero assist
            headingLocked = false;
            turn = turnInput;
        }
        else if (Math.abs(turnInput) > 0.05) {
            // Manual turn → no assist
            headingLocked = false;
            turn = turnInput;
            targetHeadingDeg = currentHeading;
        }
        else {
            // Driving straight/strafe → assist
            if (!headingLocked) {
                targetHeadingDeg = currentHeading;
                headingLocked = true;
            }

            double error = targetHeadingDeg - currentHeading;

            // Wrap [-180, 180]
            while (error > 180) error -= 360;
            while (error < -180) error += 360;

            turn = error * HEADING_kP;
        }

        // --- RAFI mecanum math (unchanged) ---
        double frontLeftPower  = forward - strafe - turn;
        double frontRightPower = forward + strafe + turn;
        double backLeftPower   = forward + strafe - turn;
        double backRightPower  = forward - strafe + turn;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower  /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower   /= maxPower;
            backRightPower  /= maxPower;
        }

        motorFL.setPower(frontLeftPower);
        motorFR.setPower(frontRightPower);
        motorBL.setPower(backLeftPower);
        motorBR.setPower(backRightPower);
    }
}

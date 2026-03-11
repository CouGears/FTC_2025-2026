package org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.teleops;

import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.PresetConstants.FW_shootVel;
import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.PresetConstants.FW_shootVelFar;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.goalUtils;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V2TeleOpBase extends BotBase {

    goalUtils goal;
    //Initializing motors
    public DcMotorEx FW, Intake, Turret;
    public CRServo Transfer;
    public Servo TransferArm, Blocker;
    //initializing toggles
    public boolean IntakeSpinning, FeedServoSpinning, slowed;
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

            pinpoint = HM.get(GoBildaPinpointDriver.class, "pinpoint");
            setPinpointPose();


        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }
        return true;
    }

    //****** AUTON MOVEMENT ******
    public void setPinpointPose(){
        Pose pedroPose = Storage.Storage_endOfAutonPose;
        pinpoint.setPosition(new Pose2D(
                DistanceUnit.INCH,
                pedroPose.getX(),
                pedroPose.getY(),
                AngleUnit.RADIANS,
                pedroPose.getHeading()
        ));
    }
    public Pose getPedroPose(){
        Pose2D p = pinpoint.getPosition();
        return new Pose (
                p.getX(DistanceUnit.INCH),
                p.getY(DistanceUnit.INCH),
                p.getHeading(AngleUnit.RADIANS)
                );
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
    //turret functions that actually make stuff move
    public void moveTurretL(){
        int tickAdjust = Turret.getCurrentPosition() + Turret_turretStep;
        adjustTurretTick(tickAdjust);
    }
    public void moveTurretR(){
        int tickAdjust = Turret.getCurrentPosition() - Turret_turretStep;
        adjustTurretTick(tickAdjust);
    }

    public void odoTurretAdjust() {
        double botX = pinpoint.getPosX(DistanceUnit.INCH);
        double botY = pinpoint.getPosY(DistanceUnit.INCH);

        double goalX, goalY;
        if (goal.getLockedTagIndex() == 0) { // RED
            goalX = Pedro_redGoalXPos;
            goalY = Pedro_redGoalYPos;
        } else { // BLUE
            goalX = Pedro_blueGoalXPos;
            goalY = Pedro_blueGoalYPos;
        }

        double dx = goalX - botX;
        double dy = goalY - botY;
        double targetFieldDeg = Math.toDegrees(Math.atan2(dy, dx));
        double turretFieldDeg = getTurretLocalizedDeg();
        double deltaDeg = targetFieldDeg - turretFieldDeg;

        while (deltaDeg > 180) deltaDeg -= 360;
        while (deltaDeg < -180) deltaDeg += 360;

        // --- Command turret ---
        adjustTurretDeg(deltaDeg);
    }


    //turret adjustements
    public void adjustTurretDeg(double degAdjust) {
        int currentTicks = Turret.getCurrentPosition();
        int deltaTicks   = (int)(degAdjust * Turret_ticksPerDeg);
        int targetTicks  = currentTicks + deltaTicks;
        targetTicks = turretResetBound(targetTicks);
        Turret.setTargetPosition(targetTicks);
    }

    public void adjustTurretTick(int tickAdjust) {
        int currentTicks = Turret.getCurrentPosition();
        int targetTicks  = currentTicks + tickAdjust;
        targetTicks = turretResetBound(targetTicks);
        Turret.setTargetPosition(targetTicks);
    }
    //turret helper functions
    public int turretResetBound(int targetTicks){
        while (targetTicks < Turret_turretLimits[0] || targetTicks > Turret_turretLimits[1]) {
            if (targetTicks < Turret_turretLimits[0]) {
                targetTicks += 360;
            } else if (targetTicks > Turret_turretLimits[0]){
                targetTicks -= 360;
            }
        }
        return targetTicks;
    }

    public double getTurretDeg(){
        return ((Turret.getCurrentPosition())/Turret_ticksPerDeg);
    }

    public double getTurretLocalizedDeg(){
        return pinpoint.getHeading(AngleUnit.DEGREES) + getTurretDeg();
    }
    public void resetTurret(){
        Turret.setTargetPosition(0);
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

    //****** SHOOTING ******
    public void handleShootSequence() {
        if (timerExpired_MSeconds("ShootSequence", Auton_gateWait + Auton_ballTransferWait)) {
            killFeeder();
            transferArmDown(); // Start moving arm down
            blockerClose();
        } else if (timerExpired_MSeconds("ShootSequence", Auton_gateWait)) {
            spinFeeder();
            transferArmUp();
            killIntake(); // Dont was ball to move below the arm while its up
        }
        if (timerExpired_MSeconds("ShootSequence", Auton_gateWait + Auton_ballTransferWait + Auton_transferResetWait)) {
            startIntake(); // Turn intake back on
            deleteTimer("ShootSequence");
        }
    }

    //****** OTHER ******
    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Turret.setPower(0);
    }

    public void toggleSlow(){
        slowed = !slowed;
    }

    public void RafiDrive(Gamepad gamepad1) {

        // --- Speed scaling ---
        if (!slowed) {
            speedMultiplier = 1;
        } else {
            speedMultiplier = Drive_slowMultiplier;
        }
        speedMultiplier = -Range.clip(speedMultiplier, 0, 1);

        // --- Update odometry ---
        pinpoint.update();

        // --- Driver inputs ---
        double forward = gamepad1.right_stick_y * speedMultiplier;
        double strafe  = gamepad1.right_stick_x * speedMultiplier;
        double turnInput = gamepad1.left_stick_x * speedMultiplier;

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

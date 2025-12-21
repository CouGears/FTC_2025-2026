package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_PIDF;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_shootVel;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.FW_shootVelFar;

import com.pedropathing.geometry.BezierLine;
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
import org.firstinspires.ftc.teamcode.cougears.util.goalUtils;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


public class V2TeleOpBase extends BotBase {
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
    GoBildaPinpointDriver pinpoint;
    goalUtils goal = new goalUtils();

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
        Pose pedroPose = Storage.endOfAutonPose;
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
        int newPos = Turret.getCurrentPosition() + Turret_turretStep;
        newPos = Math.min(newPos, Turret_turretLimits[1]);
        Turret.setTargetPosition(newPos);
    }

    public void moveTurretR(){
        int newPos = Turret.getCurrentPosition() - Turret_turretStep;
        newPos = Math.max(newPos, Turret_turretLimits[0]);
        Turret.setTargetPosition(newPos);
    }

    public void odoTurretAdjust() {
        // Get bot position in millimeters
        double botX = pinpoint.getPosX(DistanceUnit.MM);
        double botY = pinpoint.getPosY(DistanceUnit.MM);
        double botHeadingDeg = pinpoint.getHeading(AngleUnit.DEGREES);

        // Get goal position based on locked tag
        double goalX, goalY;
        if (goal.getLockedTagIndex() == 0) {
            goalX = redGoalXPos;
            goalY = redGoalYPos;
        } else {
            goalX = blueGoalXPos;
            goalY = blueGoalYPos;
        }

        // Calculate vector from bot to goal
        double dx = goalX - botX;
        double dy = goalY - botY;

        // Calculate angle to goal in field coordinates, use atan2 bc better??
        double targetFieldDeg = Math.toDegrees(Math.atan2(dy, dx));

        // Convert to turret-relative angle
        double turretTargetDeg = targetFieldDeg - botHeadingDeg;

        // Normalize to [-180, 180], i want to use adjust tick function but chat and glaude both told me to do this
        while (turretTargetDeg > 180) turretTargetDeg -= 360;
        while (turretTargetDeg < -180) turretTargetDeg += 360;

        // Convert to ticks and clip to limits
        int targetTicks = (int)(turretTargetDeg * Turret_ticksPerDeg);
        targetTicks = Range.clip(
                targetTicks,
                Turret_turretLimits[0],
                Turret_turretLimits[1]
        );

        Turret.setTargetPosition(targetTicks);
    }

    //turret adjustments
    public void adjustTurretDeg(double degAdjust) {
        int currentTicks = Turret.getCurrentPosition();
        int deltaTicks   = (int)(degAdjust * Turret_ticksPerDeg);
        int targetTicks  = currentTicks + deltaTicks;
        targetTicks = turretResetBound(targetTicks);
        Turret.setTargetPosition(targetTicks);
    }

    public void adjustTurretTick(int tickAdjust) {
        int targetTicks = Turret.getCurrentPosition() + tickAdjust;
        targetTicks = turretResetBound(targetTicks);
        Turret.setTargetPosition(targetTicks);
    }

    //turret helper functions
    public int turretResetBound(int targetTicks){
        // Wrap the turret position within valid bounds by rotating to the other side
        // If we go past +180°, wrap to -180° side (and vice versa)
        int fullRotationTicks = (int)(360 * Turret_ticksPerDeg);

        while (targetTicks < Turret_turretLimits[0]) {
            targetTicks += fullRotationTicks;
        }
        while (targetTicks > Turret_turretLimits[1]) {
            targetTicks -= fullRotationTicks;
        }

        return targetTicks;
    }

    public double getTurretDeg(){
        return (Turret.getCurrentPosition() / Turret_ticksPerDeg);
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

    public void RafiDrive(Gamepad gamepad1, boolean twoControllerMode) {
        // Update pinpoint odometry
        pinpoint.update();

        // --- Speed scaling ---
        if (!slowed) {
            speedMultiplier = 1;
        } else {
            speedMultiplier = Drive_slowMultiplier;
        }
        speedMultiplier = -Range.clip(speedMultiplier, 0, 1);

        // --- Driver inputs (depends on mode) ---
        double forward, strafe, turnInput;

        if (twoControllerMode) {
            // TWO CONTROLLER MODE: Split-stick control
            // Right stick Y = forward/backward ONLY
            // Left stick X = strafe left/right ONLY
            // Right trigger = turn right
            // Left trigger = turn left
            forward = gamepad1.right_stick_y * speedMultiplier;
            strafe  = gamepad1.left_stick_x * speedMultiplier;
            turnInput = (gamepad1.right_trigger - gamepad1.left_trigger) * speedMultiplier;
        } else {
            // ONE CONTROLLER MODE: Original control
            // Right stick = forward/backward AND strafe
            // Left stick X = turn
            forward = gamepad1.right_stick_y * speedMultiplier;
            strafe  = gamepad1.right_stick_x * speedMultiplier;
            turnInput = gamepad1.left_stick_x * speedMultiplier;
        }

        double turn;
        double currentHeading = pinpoint.getPosition().getHeading(AngleUnit.DEGREES);

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

        // --- RAFI mecanum math ---
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
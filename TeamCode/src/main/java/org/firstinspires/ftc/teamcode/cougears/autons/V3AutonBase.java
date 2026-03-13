package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.util.Timer;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierLine;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.PanelsFeatures;
import org.firstinspires.ftc.teamcode.cougears.util.SensorFusionManager;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V3AutonBase {
    //Initializing motors
    public DcMotorEx FW, Intake, Transfer;
    public Servo Blocker;
    //initializing toggles
    public boolean IntakeSpinning;

    public Timer blockerTimer, shootSequenceTimer, gateIntakeTimer, openGateTimer, farTimer, gateWaitTimer;

    HardwareMap HM;
    Telemetry tele;
    SensorFusionManager SFM;
    AprilTagBase ATB;
    PanelsFeatures panels;

    public V3AutonBase(HardwareMap HardwareMap, Telemetry Telemetry) {
        HM = HardwareMap;
        tele = Telemetry;
        blockerTimer = new Timer();
        shootSequenceTimer = new Timer();
        gateIntakeTimer = new Timer();
        openGateTimer = new Timer();
        farTimer = new Timer();
        gateWaitTimer = new Timer();
        ATB = new AprilTagBase(HM, tele);
        SFM = new SensorFusionManager(HM, tele, ATB);
        panels = new PanelsFeatures(tele);

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
            ATB.initAprilTag();

            panels.startCameraStream(ATB.visionPortal);


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
    public void startIntakeFast() {
        Intake.setPower(Drive_intakePowerFast);
        IntakeSpinning = true;
    }
    public void startIntakeSlow() {
        Intake.setPower(Drive_intakePowerSlow);
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

    public void killTransfer() { Transfer.setPower(0);}
    public void ejectTransfer() { Transfer.setPower(-1);}

    ElapsedTime pulseTimer = new ElapsedTime();
    boolean shooting = false;
    public void pulseTransfer() {
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

    //****** AUTON ******
    public void moveToPose(Follower f, Pose targetPose){
        f.followPath(
                f.pathBuilder()
                        .addPath(new BezierLine(f.getPose(), targetPose))
                        .setLinearHeadingInterpolation(f.getHeading(), targetPose.getHeading())
                        .build()
        );
    }
    public boolean isNotNear(Follower follower, Pose target) {
        double botX = follower.getPose().getX();
        double botY = follower.getPose().getY(); // Fixed: was getX()
        double distance = Math.hypot(target.getX() - botX, target.getY() - botY);
        return !(distance < xyPoseErrorPTM);
    }

    // CANNOT BE CALLED EVERY LOOP
    public void moveToPose(Follower f, Pose... targetPoses) {
        PathBuilder PB = f.pathBuilder();
        Pose lastPose = f.getPose();
        double lastHeading = f.getHeading();
        for (Pose targetPose : targetPoses) {
            PB.addPath(new BezierCurve(lastPose, targetPose))
                    .setLinearHeadingInterpolation(lastHeading, targetPose.getHeading());
            lastPose = targetPose;
            lastHeading = targetPose.getHeading();
        }
        f.followPath(PB.build());
    }

    public enum shootingSequence {
        GO_TO_POSITION,
        ALIGN_AT,
        OPEN_BLOCKER,
        SHOOT,
        END

    }
    shootingSequence shootingSequenceSavedStep = shootingSequence.GO_TO_POSITION;
    public boolean handleShootingSequence(ShootingPosition shootPos, Follower follower, Telemetry tele, Boolean farShoot, boolean aprilTag){
        tele.addData("Curr Step in handleShootingSequence:", "%s", shootingSequenceSavedStep);
        switch (shootingSequenceSavedStep) {
            case GO_TO_POSITION:
                FWSpinTo(shootPos.getShootingVelocity()); // Make sure we are up to vel
                if (follower.isBusy()) return false; //Cant move past this until we get to pos
                if (aprilTag){
                    if (!SFM.handFullShootPosAlignSequence(follower, shootPos, this)){
                        return false;
                    }
                } else {
                    moveToPose(follower, shootPos.getShootingPose());
                }
                shootingSequenceSavedStep = shootingSequence.OPEN_BLOCKER;
                break;
            case ALIGN_AT:
                break;
            case OPEN_BLOCKER:
                if (follower.isBusy()) return false; //Cant move past this until we get to pos
                if (!(FWUpToSpeed(shootPos.getShootingVelocity() - Auton_startShootingVelocityTolerance))) return false;
                openBlocker();
                blockerTimer.resetTimer();
                shootingSequenceSavedStep = shootingSequence.SHOOT;
                break;
            case SHOOT:
                if (blockerTimer.getElapsedTime() < Auton_blockerWait) return false;
                if (!(FWUpToSpeed(shootPos.getShootingVelocity() - Auton_startShootingVelocityTolerance))) return false;
                if (farShoot) {
                    startTransferFar();
                } else {
                    startTransfer();
                }
                startIntakeFast();
                shootingSequenceSavedStep = shootingSequence.END;
                shootSequenceTimer.resetTimer();
                break;
            case END:
                if (!farShoot && shootSequenceTimer.getElapsedTime() < Auton_ballShootSequenceTime) return false;
                if (farShoot && shootSequenceTimer.getElapsedTime() < Auton_ballShootSequenceTimeFar) return false;

                killTransfer();
                closeBlocker();
                shootingSequenceSavedStep = shootingSequence.GO_TO_POSITION;
                return true;
        }
        return false;
    }

    public enum pickUpBalls {
        FIND_DEPOT,
        MOVE_TO_START,
        PICK_UP_BALLS,
        MOVE_BACK_TO_START,
        END
    }
    pickUpBalls pickUpBallsSavedStep = pickUpBalls.FIND_DEPOT;
    Pose targetDepotStart = null;
    Pose targetDepotEnd = null;
    public boolean handlePickUpBalls(String autonColor, int depotNum, boolean returnToStart, Follower follower, Telemetry tele) {
        tele.addData("Curr Step in handlePickUpBalls:", "%s", pickUpBallsSavedStep);
        switch (pickUpBallsSavedStep) {
            case FIND_DEPOT:
                if (autonColor.equals("Red")) {
                    switch (depotNum) {
                        case 1:
                            targetDepotStart = RedBallDepotStart1;
                            targetDepotEnd = RedBallDepotEnd1;
                            break;
                        case 2:
                            targetDepotStart = RedBallDepotStart2;
                            targetDepotEnd = RedBallDepotEnd2;
                            break;
                        case 3:
                            targetDepotStart = RedBallDepotStart3;
                            targetDepotEnd = RedBallDepotEnd3;
                            break;
                        case 4:
                            targetDepotStart = RedBallDepotStart4;
                            targetDepotEnd = RedBallDepotEnd4;
                    }
                } else {
                    switch (depotNum) {
                        case 1:
                            targetDepotStart = BlueBallDepotStart1;
                            targetDepotEnd = BlueBallDepotEnd1;
                            break;
                        case 2:
                            targetDepotStart = BlueBallDepotStart2;
                            targetDepotEnd = BlueBallDepotEnd2;
                            break;
                        case 3:
                            targetDepotStart = BlueBallDepotStart3;
                            targetDepotEnd = BlueBallDepotEnd3;
                            break;
                        case 4:
                            targetDepotStart = BlueBallDepotStart4;
                            targetDepotEnd = BlueBallDepotEnd4;
                    }
                }
                pickUpBallsSavedStep = pickUpBalls.MOVE_TO_START;
                break;
            case MOVE_TO_START:
                if (follower.isBusy()) return false;
                moveToPose(follower, targetDepotStart);
                pickUpBallsSavedStep = pickUpBalls.PICK_UP_BALLS;
                break;
            case PICK_UP_BALLS:
                startIntakeFast();
                startTransfer();
                if (follower.isBusy()) return false;
                moveToPose(follower, targetDepotEnd);
                pickUpBallsSavedStep = pickUpBalls.MOVE_BACK_TO_START;
                break;
            case MOVE_BACK_TO_START:
                if (!returnToStart) {
                    pickUpBallsSavedStep = pickUpBalls.END;
                    break;
                }
                if (follower.isBusy()) return false;
                moveToPose(follower, targetDepotStart);
                pickUpBallsSavedStep = pickUpBalls.END;
                break;
            case END:
                if (follower.isBusy()) return  false;
                pickUpBallsSavedStep = pickUpBalls.FIND_DEPOT;
                killTransfer();
                return true;
        }
        return false;
    }

    public enum gateIntake {
        FIND_GATE,
        PREINIT,
        GO_TO_INIT,
        PICK_UP_BALLS,
        RETURN_TO_INIT,
        RETURN_TO_PREINIT
    }
    gateIntake gateIntakeSavedStep = gateIntake.FIND_GATE;
    Pose gateInit = null;
    Pose gateOpen = null;
    Pose preInit = null;
    Pose unstuckPose = null;

    boolean wentToGateOpen = false;

    public boolean handleGateIntake(String autonColor, Follower follower, Telemetry tele){
        switch (gateIntakeSavedStep){
            case FIND_GATE:
                wentToGateOpen = false;
                if (autonColor.equals("Red")){
                    gateInit = RedGatePickupInit;
                    gateOpen = RedGatePickupOpen;
                    preInit = RedBallDepotStart2;
                } else {
                    gateInit = BlueGatePickupInit;
                    gateOpen = BlueGatePickupOpen;
                    preInit = BlueBallDepotStart2;

                }
                gateIntakeSavedStep = gateIntake.PREINIT;
                break;
            case PREINIT:
                if (follower.isBusy()) return false;
                moveToPose(follower, preInit);
                gateIntakeSavedStep = gateIntake.GO_TO_INIT;
                break;
            case GO_TO_INIT:
                if (follower.isBusy()) return false;
                moveToPose(follower, gateInit);
                gateIntakeSavedStep = gateIntake.PICK_UP_BALLS;
                gateIntakeTimer.resetTimer();
                break;
            case PICK_UP_BALLS:
                if (follower.isBusy()) return false;
                startTransfer();
                startIntakeFast();
                if (!wentToGateOpen) {
                    moveToPose(follower, gateOpen);
                    wentToGateOpen = true;
                }
                if (gateIntakeTimer.getElapsedTime() > Auton_gateIntakeWait) {
                    gateIntakeSavedStep = gateIntake.RETURN_TO_INIT;
                }
                break;
            case RETURN_TO_INIT:
                if (follower.isBusy()) return false;
                moveToPose(follower, gateInit);
                killIntake();
                killTransfer();
                gateIntakeSavedStep = gateIntake.RETURN_TO_PREINIT;
                break;
            case RETURN_TO_PREINIT:
                if (follower.isBusy()) return false;
                moveToPose(follower, preInit);
                gateIntakeSavedStep = gateIntake.FIND_GATE;
                return true;
        }
        return false;
    }

    public enum gateOpenSequence{
        FIND_GATE,
        OPEN_GATE,
        RETURN_TO_INIT,
        RETURN_TO_PREINIT
    }
    gateOpenSequence gateOpenSequenceSavedStep = gateOpenSequence.FIND_GATE;
    int unstuckDirection = 0;
    ElapsedTime unstuckTimer = new ElapsedTime();
    ElapsedTime handleOpenGateStepTimer = new ElapsedTime();
    boolean wentToPreInit = false; // <-- ADD THIS

    public boolean handleOpenGate(String autonColor, Follower follower, boolean goToPreinit, Telemetry tele){
        tele.addData("Curr Step in handleOpenGate:", "%s", gateOpenSequenceSavedStep);
        switch (gateOpenSequenceSavedStep) {
            case FIND_GATE:
                wentToGateOpen = false;
                wentToPreInit = false; // Reset our new flag

                if (autonColor.equals("Red")){
                    gateInit = RedGateInit;
                    gateOpen = RedGateOpen;
                    preInit = RedBallDepotStart2;
                    unstuckDirection = -1;
                } else {
                    gateInit = BlueGateInit;
                    gateOpen = BlueGateOpen;
                    preInit = BlueBallDepotStart2;
                    unstuckDirection = 1;
                }
                unstuckPose = gateOpen;
                gateOpenSequenceSavedStep = gateOpenSequence.OPEN_GATE;
                openGateTimer.resetTimer();
                unstuckTimer.reset();
                handleOpenGateStepTimer.reset();
                break;

            case OPEN_GATE:
                tele.addData("going to ", unstuckPose);
                tele.addData("bot pos ", follower.getPose());

                // 1. Command the initial gate push path ONLY ONCE
                if (!wentToGateOpen) {
                    moveToPose(follower, gateInit, gateOpen);
                    wentToGateOpen = true;
                    openGateTimer.resetTimer();
                }

                // 2. FIX: Only command the unstuck path IF the timer expires.
                // Do NOT command this every loop tick!
                if (unstuckTimer.seconds() >= 1 && handleOpenGateStepTimer.seconds() > 3) {
                    unstuckPose = new Pose(unstuckPose.getX() + unstuckDirection, unstuckPose.getY(), unstuckPose.getHeading());
                    moveToPose(follower, unstuckPose); // <-- Moved inside the IF block
                    unstuckTimer.reset();
                    return false;
                }

                if (openGateTimer.getElapsedTime() < Auton_gateOpenWait) return false;

                if (!goToPreinit) {
                    gateOpenSequenceSavedStep = gateOpenSequence.FIND_GATE;
                    return true;
                } else {
                    gateOpenSequenceSavedStep = gateOpenSequence.RETURN_TO_PREINIT;
                    break;
                }



            case RETURN_TO_PREINIT:
                tele.addData("going to ", preInit);
                tele.addData("bot pos ", follower.getPose());

                // FIX: Command it to move ONCE, then wait until it finishes driving!
                if (!wentToPreInit) {
                    moveToPose(follower, preInit);
                    wentToPreInit = true;
                }

                if (follower.isBusy()) return false; // Don't return true until actually there

                // FIX: Used to be gateIntakeSavedStep. Must be gateOpenSequenceSavedStep!
                gateOpenSequenceSavedStep = gateOpenSequence.FIND_GATE;
                return true;
        }
        return false;
    }

    public enum pickupFarBallsSequence{
        FIND_POSES,
        GO_TO_START,
        GO_TO_END,
        WAIT_FOR_END
    }
    pickupFarBallsSequence pickupFarBallsSequenceSavedStep = pickupFarBallsSequence.FIND_POSES;
    Pose farPickupStartPose = null;
    Pose farPickupEndPose = null;

    public boolean handlePickupFarBalls(String autonColor, Follower follower, Telemetry tele) {
        // FIX: Replaced gateOpenSequenceSavedStep with the correct enum variable
        tele.addData("Curr Step in handlePickupFarBalls:", "%s", pickupFarBallsSequenceSavedStep);

        switch (pickupFarBallsSequenceSavedStep) {
            case FIND_POSES:
                if (autonColor.equals("Red")){
                    farPickupStartPose = RedBallDepotStart4;
                    farPickupEndPose = RedBallDepotEnd4;
                } else {
                    farPickupStartPose = BlueBallDepotStart4;
                    farPickupEndPose = BlueBallDepotEnd4;
                }
                pickupFarBallsSequenceSavedStep = pickupFarBallsSequence.GO_TO_START;
                break;

            case GO_TO_START:
                if (follower.isBusy()) return false; // Ensure we are ready to move
                moveToPose(follower, farPickupStartPose);
                pickupFarBallsSequenceSavedStep = pickupFarBallsSequence.GO_TO_END;
                break;

            case GO_TO_END:
                if (follower.isBusy()) return false; // WAIT until we reach the start pose!

                // Turn on intake and transfer to grab the balls as we drive
                startIntakeFast();
                startTransfer();

                moveToPose(follower, farPickupEndPose);
                pickupFarBallsSequenceSavedStep = pickupFarBallsSequence.WAIT_FOR_END;
                break;

            case WAIT_FOR_END:
                if (follower.isBusy()) return false; // WAIT until we finish driving through the balls!

                // FIX: Reset the sequence so it can run again later if needed
                pickupFarBallsSequenceSavedStep = pickupFarBallsSequence.FIND_POSES;
                return true; // Tell the OpMode we are finished!
        }
        return false; // Still running...
    }

    public enum gateWait{
        FIND_GATEWAIT,
        GO_TO_GATEWAIT,
        CHECK_TO_LEAVE
    }
    gateWait gateWaitSavedStep = gateWait.FIND_GATEWAIT;
    Pose gateWaitPos;

    public boolean handleGateWait(String autonColor, Follower follower, Telemetry tele){
        tele.addData("Curr Step in gateWait:", "%s", gateWaitSavedStep);
        switch (gateWaitSavedStep){
            case FIND_GATEWAIT:
                if (autonColor.equals("Red")){
                    gateWaitPos = RedWaitByHumanZone;
                } else {
                    gateWaitPos = BlueWaitByHumanZone;
                }
                gateWaitSavedStep = gateWait.GO_TO_GATEWAIT;
                break;
            case GO_TO_GATEWAIT:
                startTransfer();
                moveToPose(follower, gateWaitPos);
                gateWaitTimer.resetTimer();
                gateWaitSavedStep = gateWait.CHECK_TO_LEAVE;
                break;
            case CHECK_TO_LEAVE:
                if (!(SFM.ballInPosition().equals(SensorFusionManager.ballState.NO_BALLS))){
                    if (gateWaitTimer.getElapsedTime() > Auton_gateWaitTime){
                        killTransfer();
                        return true;
                    }
                    gateWaitSavedStep = gateWait.FIND_GATEWAIT;
                    break;
                }
        }
        return false;
    }
    public void updateStoragePosition(Follower follower){
        Storage.Storage_endOfAutonPose = follower.getPose();
    }

    //****** OTHER ******
    public boolean endAuton(Follower follower, String color){
        if (follower.isBusy()) return false;
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Storage.Storage_endOfAutonPose = follower.getPose();
        Storage.Storage_endOfAutonColor = color;
        return true;
    }

}

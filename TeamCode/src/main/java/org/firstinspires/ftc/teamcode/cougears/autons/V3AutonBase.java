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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;


public class V3AutonBase {
    //Initializing motors
    public DcMotorEx FW, Intake, Transfer;
    public Servo Blocker;
    //initializing toggles
    public boolean IntakeSpinning;

    public Timer blockerTimer, shootSequenceTimer, gateIntakeTimer, openGateTimer;

    HardwareMap HM;
    Telemetry tele;

    public V3AutonBase(HardwareMap HardwareMap, Telemetry Telemetry) {
        HM = HardwareMap;
        tele = Telemetry;
        blockerTimer = new Timer();
        shootSequenceTimer = new Timer();
        gateIntakeTimer = new Timer();
        openGateTimer = new Timer();
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
    //****** Transfer ******
    public void startTransfer() { Transfer.setPower(Drive_transferPower);}
    public void killTransfer() { Transfer.setPower(0);}
    public void ejectTransfer() { Transfer.setPower(-1);}

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
        OPEN_BLOCKER,
        SHOOT,
        END

    }
    shootingSequence shootingSequenceSavedStep = shootingSequence.GO_TO_POSITION;
    public boolean handleShootingSequence(ShootingPosition shootPos, Follower follower, Telemetry tele){
        tele.addData("Curr Step in handleShootingSequence:", "%s", shootingSequenceSavedStep);
        switch (shootingSequenceSavedStep) {
            case GO_TO_POSITION:
                FWSpinTo(shootPos.getShootingVelocity()); // Make sure we are up to vel
                if (follower.isBusy()) return false; //Cant move past this until we get to pos
                moveToPose(follower, shootPos.getShootingPose());
                shootingSequenceSavedStep = shootingSequence.OPEN_BLOCKER;
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
                startTransfer();
                startIntake();
                shootingSequenceSavedStep = shootingSequence.END;
                shootSequenceTimer.resetTimer();
                break;
            case END:
                if (shootSequenceTimer.getElapsedTime() < Auton_ballShootSequenceTime) return false;
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
                startIntake();
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
                startIntake();
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
    public boolean handleOpenGate(String autonColor, Follower follower, boolean goToPreinit, Telemetry tele){
        tele.addData("Curr Step in handleOpenGate:", "%s", gateOpenSequenceSavedStep);
        switch (gateOpenSequenceSavedStep) {
            case FIND_GATE:
                wentToGateOpen = false;
                if (autonColor.equals("Red")){
                    gateInit = RedGateInit;
                    gateOpen = RedGateOpen;
                    preInit = RedBallDepotStart2;

                } else {
                    gateInit = BlueGateInit;
                    gateOpen = RedGateOpen;
                    preInit = BlueBallDepotStart2;

                }
                gateOpenSequenceSavedStep = gateOpenSequence.OPEN_GATE;
                openGateTimer.resetTimer();
                break;
            case OPEN_GATE:
                if (!wentToGateOpen) {
                    moveToPose(follower, gateInit, gateOpen);
                    wentToGateOpen = true;
                    openGateTimer.resetTimer();
                }
                moveToPose(follower, gateOpen);
                if (openGateTimer.getElapsedTime() < Auton_gateOpenWait) return false;

                if (!goToPreinit) {
                    gateOpenSequenceSavedStep = gateOpenSequence.FIND_GATE;
                    return true;
                } else {
                    gateOpenSequenceSavedStep = gateOpenSequence.RETURN_TO_PREINIT;
                    break;
                }
            case RETURN_TO_PREINIT:
                if (follower.isBusy()) return false;
                moveToPose(follower, preInit);
                gateIntakeSavedStep = gateIntake.FIND_GATE;
                return true;
        }
        return false;
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

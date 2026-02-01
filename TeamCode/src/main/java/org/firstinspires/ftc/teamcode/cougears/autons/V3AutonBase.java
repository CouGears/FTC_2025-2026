package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
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

    public Timer blockerTimer, shootSequenceTimer;

    HardwareMap HM;
    Telemetry tele;

    public V3AutonBase(HardwareMap HardwareMap, Telemetry Telemetry) {
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

    public enum shootingSequence {
        GO_TO_POSITION,
        OPEN_BLOCKER,
        SHOOT,
        END

    }
    shootingSequence shootingSequenceSavedStep = shootingSequence.GO_TO_POSITION;
    public boolean handleShootingSequence(ShootingPosition shootPos, Follower follower){
        blockerTimer = new Timer();
        shootSequenceTimer = new Timer();
        switch (shootingSequenceSavedStep) {
            case GO_TO_POSITION:
                FWSpinTo(shootPos.getShootingVelocity()); // Make sure we are up to vel
                if (follower.isBusy()) return false; //Cant move past this until we get to pos
                moveToPose(follower, shootPos.getShootingPose());
                shootingSequenceSavedStep = shootingSequence.OPEN_BLOCKER;
                break;
            case OPEN_BLOCKER:
                if (follower.isBusy()) return false; //Cant move past this until we get to pos
                if (!(FWUpToSpeed(shootPos.getShootingVelocity() - 50))) return false;
                openBlocker();
                blockerTimer.resetTimer();
                shootingSequenceSavedStep = shootingSequence.SHOOT;
                break;
            case SHOOT:
                if (blockerTimer.getElapsedTime() < Auton_gateWait) return false;
                startTransfer();
                startIntake();
                shootingSequenceSavedStep = shootingSequence.END;
                break;
            case END:
                if (shootSequenceTimer.getElapsedTime() < Auton_ballShootSequenceTime) return false;
                killIntake();
                killTransfer();
                killFW();
                shootingSequenceSavedStep = shootingSequence.GO_TO_POSITION;
                return true;
        }
        return false;
    }

    public enum pickUpBalls {
        FIND_DEPOT,
        MOVE_TO_START,
        PICK_UP_BALLS,
        MOVE_BACK_TO_START

    }
    pickUpBalls pickUpBallsSavedStep = pickUpBalls.FIND_DEPOT;
    Pose targetDepotStart = null;
    Pose targetDepotEnd = null;
    public boolean handlePickUpBalls(String autonColor, int depotNum, Follower follower) {
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
                if (follower.isBusy()) return false;
                moveToPose(follower, targetDepotEnd);
                pickUpBallsSavedStep = pickUpBalls.MOVE_BACK_TO_START;
                break;
            case MOVE_BACK_TO_START:
                if (follower.isBusy()) return false;
                killIntake();
                moveToPose(follower, targetDepotStart);
                pickUpBallsSavedStep = pickUpBalls.FIND_DEPOT;
                return true;
        }
        return false;
    }


    //****** OTHER ******
    public void endAuton(Follower follower, String color){
        FW.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
        Storage.Storage_endOfAutonPose = follower.getPose();
        Storage.Storage_endOfAutonColor = color;
    }

}

package org.firstinspires.ftc.teamcode.cougears.autons.Blue;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.BlueBasicEndClose;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.BlueStartPos;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.blueShootingPosHashMap;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.buildPaths;
import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.Storage_endOfAutonColor;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Blue")
public class BlueClose extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public Pose startPos   = BlueStartPos;
    public ShootingPosition shootPos = blueShootingPosHashMap.get("BlueTriangleClose");
    public Pose endPos   = BlueBasicEndClose;

    public int BDCounter = 0;
    public  int numBDToPickup = 0;
    public boolean incrimentedBDCounter = false;


    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP,
        CLOSETRIANGLE_BASICENDCLOSE,
        END
    }
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28 && currStep != pathStep.END) {
            bot.moveToPose(follower, endPos);
        }
        switch (currStep) {
            case SHOOT_BALLS:
                if (bot.handleShootingSequence(shootPos, follower, telemetry, false, false)) {
                    if (BDCounter < numBDToPickup) {
                        setPathStep(pathStep.BD_PICKUP);
                        incrimentedBDCounter = false;
                    } else {
                        setPathStep(pathStep.CLOSETRIANGLE_BASICENDCLOSE);
                    }
                }
                break;
            case BD_PICKUP:
                if (!incrimentedBDCounter){
                    BDCounter++;
                    incrimentedBDCounter = true;
                }

                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, true, follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                }
                break;
            case CLOSETRIANGLE_BASICENDCLOSE:
                bot.moveToPose(follower, endPos);
                setPathStep(pathStep.END);
                break;
            case END:
                if (!follower.isBusy()) {
                    bot.endAuton(follower, shootPos.getShootingColor());
                    terminateOpModeNow();
                }
                break;
            default:
                telemetry.addLine("No Step");
        }
    }

    public void setPathStep (pathStep newStep){
        currStep = newStep;
        stepTimer.resetTimer();
    }


    @Override
    public void init() {
        Storage_endOfAutonColor = "Blue";

        stepTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPos);
        buildPaths(follower);
        bot = new V3AutonBase(hardwareMap, telemetry);
        bot.botInit();
    }

    @Override
    public void init_loop() {
        telemetry.addData("NumBDToPickup", "%d", numBDToPickup);
        if(gamepad1.dpadUpWasPressed() && numBDToPickup != 3){
            numBDToPickup++;
        } else if (gamepad1.dpadDownWasPressed() && numBDToPickup != 0){
            numBDToPickup--;
        }
        telemetry.update();
    }

    @Override
    public void start() {
        bot.startIntakeFast();
        opModeTimer.resetTimer();
        setPathStep(pathStep.SHOOT_BALLS);
    }

    @Override
    public void loop() {
        follower.update();
        bot.updateStoragePosition(follower);
        stepUpdate();

        // Telemetry for debugging
        telemetry.addData("Current Step", currStep);
        telemetry.update();
    }
}
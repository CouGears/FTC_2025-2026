package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedBasicEndFar;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedStartPosFar;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.buildPaths;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.redShootingPosHashMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedFarSensor extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public Pose startPos   = RedStartPosFar;
    public ShootingPosition shootPos = redShootingPosHashMap.get("RedFar");
    public Pose endPos   = RedBasicEndFar;
    boolean pickedUpBD = false;
    boolean endAuton = false;

    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP_4,
        WAIT_BY_GATE,
        GO_TO_END_AUTON_POS,
        END
    }
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 23 && currStep != pathStep.END) {
            setPathStep(pathStep.SHOOT_BALLS);
            endAuton = true;
        }
        switch (currStep) {
            case SHOOT_BALLS:
                if (!pickedUpBD && bot.handleShootingSequence(shootPos, follower, telemetry, true, true)) {
                    setPathStep(pathStep.BD_PICKUP_4);
                    pickedUpBD = false;
                } else if (endAuton && bot.handleShootingSequence(shootPos, follower, telemetry, true, true)){
                    setPathStep(pathStep.GO_TO_END_AUTON_POS);
                } else {
                    setPathStep(pathStep.WAIT_BY_GATE);
                }
                break;
            case BD_PICKUP_4:
                if (bot.handlePickUpBalls(shootPos.getShootingColor(), 4, false, follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                    pickedUpBD = true;
                }
                break;
            case WAIT_BY_GATE:
                if (bot.handleGateWait(shootPos.getShootingColor(), follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                }
                break;
            case GO_TO_END_AUTON_POS:
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
        stepTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPos);
        buildPaths(follower);
        bot = new V3AutonBase(hardwareMap, telemetry);
        bot.botInit();
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
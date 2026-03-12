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
public class RedFar_GateRecycle extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public Pose startPos   = RedStartPosFar;
    public ShootingPosition shootPos = redShootingPosHashMap.get("RedFar");
    public Pose endPos   = RedBasicEndFar;

    public enum pathStep {
        SHOOT_BALLS,
        GATE_RECYCLE,
        SHOOTPOS_BASICENDFAR,
        END
    }
    public boolean preloadsShot = false;
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28 && currStep != pathStep.END) {
            bot.moveToPose(follower, endPos);
        }
        switch (currStep) {
            case SHOOT_BALLS:
                if (bot.handleShootingSequence(shootPos, follower, telemetry, true)) {
                    if(!preloadsShot){
                        setPathStep(pathStep.GATE_RECYCLE);
                        preloadsShot = true;
                    } else {
                        setPathStep(pathStep.SHOOTPOS_BASICENDFAR);
                    }
                }
                break;
            case GATE_RECYCLE:
                if (bot.handleGateWait(shootPos.getShootingColor(), follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                }
                break;
            case SHOOTPOS_BASICENDFAR:
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
        bot.startIntake();
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
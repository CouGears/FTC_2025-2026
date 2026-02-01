package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedBasicEndFar;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedStartPos;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedStartPosFar;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.buildPaths;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.redShootingPosHashMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.Blue.BlueFar_Preloads;
import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedFar_BD1 extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public ShootingPosition shootPos = redShootingPosHashMap.get("RedFar");
    public Pose endPos   = RedBasicEndFar;

    public int BDCounter = 4;
    public final int numBDToPickup = 1;
    public boolean decrimentedBDCounter = false;


    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP,
        CLOSETRIANGLE_BASICENDFAR,
        END
    }
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) {
            bot.moveToPose(follower, endPos);
            setPathStep(pathStep.END);
        }
        switch (currStep) {
            case SHOOT_BALLS:
                if (bot.handleShootingSequence(shootPos, follower, telemetry)) { // Any step after a step which moves the bot must have this if statement to make sure we dont do anything until the bot is in teh right spot
                    if (BDCounter > 4 - numBDToPickup) {
                        setPathStep(pathStep.BD_PICKUP);
                        decrimentedBDCounter = false;
                    } else {
                        setPathStep(pathStep.CLOSETRIANGLE_BASICENDFAR);
                    }
                }
                break;
            case BD_PICKUP:
                if (!decrimentedBDCounter){
                    BDCounter--;
                    decrimentedBDCounter = true;
                }

                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                }
                break;
            case CLOSETRIANGLE_BASICENDFAR:
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
        follower.setPose(RedStartPosFar);
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
        stepUpdate();

        // Telemetry for debugging
        telemetry.addData("Current Step", currStep);
        telemetry.update();
    }
}
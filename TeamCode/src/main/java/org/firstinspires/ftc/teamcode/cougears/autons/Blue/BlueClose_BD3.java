package org.firstinspires.ftc.teamcode.cougears.autons.Blue;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Blue")
public class BlueClose_BD3 extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public ShootingPosition shootPos = blueShootingPosHashMap.get("BlueTriangleClose");
    public Pose endPos   = BlueBasicEndClose;
    public int BDCounter = 0;
    public final int numBDToPickup = 3;
    public boolean incrimentedBDCounter = false;


    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP,
        CLOSETRIANGLE_BASICEND,
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
                    if (BDCounter < numBDToPickup) {
                        setPathStep(pathStep.BD_PICKUP);
                        incrimentedBDCounter = false;
                    } else {
                        setPathStep(pathStep.CLOSETRIANGLE_BASICEND);
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
            case CLOSETRIANGLE_BASICEND:
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
        follower.setPose(BlueStartPos);
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
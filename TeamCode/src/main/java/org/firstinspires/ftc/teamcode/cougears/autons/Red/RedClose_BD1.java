package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedBasicEndClose;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedStartPos;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.buildPaths;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.redShootingPosHashMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedClose_BD1 extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public ShootingPosition shootPos = redShootingPosHashMap.get("RedTriangleClose");
    public Pose endPos   = RedBasicEndClose;
    public int BDCounter = 0;
    public final int numBDToPickup = 1;
    public boolean incrimentedBDCounter = false;


    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP,
        CLOSETRIANGLE_BASICEND,
        END
    }
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) { bot.moveToPose(follower, endPos); } //CHANGE IF BLUE
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

                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, follower, telemetry)){
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
        follower.setPose(RedStartPos);
        buildPaths(follower);
        bot = new V3AutonBase(hardwareMap, telemetry);
        bot.botInit();
        bot.startIntake();
    }

    @Override
    public void start() {
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
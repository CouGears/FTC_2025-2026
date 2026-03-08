package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedCloseFullAuton extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public ShootingPosition shootPos = redShootingPosHashMap.get("RedTriangleClose");
    public Pose endPos   = RedBasicEndClose;
    public int BDCounter = 2;



    public enum pathStep {
        SHOOT_BALLS,
        BD_PICKUP2,
        OPEN_GATE,
        BD_PICKUP1,
        BD_PICKUP3,
        CLOSETRIANGLE_BASICEND,
        END
    }
    pathStep currStep = pathStep.SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) {
            bot.moveToPose(follower, endPos);
            setPathStep(pathStep.END);
        }
        telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());
        switch (currStep) {
            case SHOOT_BALLS:
                if (bot.handleShootingSequence(shootPos, follower, telemetry, false)) { // Any step after a step which moves the bot must have this if statement to make sure we dont do anything until the bot is in teh right spot
                    if (BDCounter == 2){
                        setPathStep(pathStep.BD_PICKUP2);
                    } else if (BDCounter == 1){
                        setPathStep(pathStep.BD_PICKUP1);
                    } else if (BDCounter == 3){
                        setPathStep(pathStep.BD_PICKUP3);
                    } else {
                        setPathStep(pathStep.CLOSETRIANGLE_BASICEND);
                    }
                }
                break;
            case BD_PICKUP2:
                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, false, follower, telemetry)){
                    setPathStep(pathStep.OPEN_GATE);
                    BDCounter = 1;
                }
                break;
            case BD_PICKUP1:
                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, false, follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                    BDCounter = 3;
                }
                break;
            case BD_PICKUP3:
                if (bot.handlePickUpBalls(shootPos.getShootingColor(), BDCounter, false,follower, telemetry)){
                    setPathStep(pathStep.SHOOT_BALLS);
                    BDCounter = 0;
                }
                break;
            case OPEN_GATE:
                if (bot.handleOpenGate(shootPos.getShootingColor(), follower, true, telemetry)){
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
        follower.setPose(RedStartPos);
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
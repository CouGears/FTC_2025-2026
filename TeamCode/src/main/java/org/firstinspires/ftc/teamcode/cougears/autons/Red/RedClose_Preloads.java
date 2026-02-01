package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

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
public class RedClose_Preloads extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public ShootingPosition shootPos = redShootingPosHashMap.get("RedTriangleClose");
    public Pose endPos   = RedBasicEndClose;

    public enum pathStep {
        START_CLOSETRIANGLE_SHOOT_BALLS,
        CLOSETRIANGLE_BASICEND,
        END
    }
    pathStep currStep = pathStep.START_CLOSETRIANGLE_SHOOT_BALLS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) { bot.moveToPose(follower, endPos); } //CHANGE IF BLUE
        switch (currStep) {
            case START_CLOSETRIANGLE_SHOOT_BALLS:
                if (bot.handleShootingSequence(shootPos, follower, telemetry)) { // Any step after a step which moves the bot must have this if statement to make sure we dont do anything until the bot is in teh right spot
                    setPathStep(pathStep.CLOSETRIANGLE_BASICEND);
                }
                break;
            case CLOSETRIANGLE_BASICEND:
                bot.moveToPose(follower, endPos);
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
        setPathStep(pathStep.START_CLOSETRIANGLE_SHOOT_BALLS);
    }

    @Override
    public void loop() {
        telemetry.addData("FW SPEED", "%.2f", bot.FW.getVelocity());
        telemetry.addData("Busy?", "%b", follower.isBusy());

        follower.update();
        stepUpdate();

        // Telemetry for debugging
        telemetry.addData("Current Step", currStep);
        telemetry.update();
    }
}
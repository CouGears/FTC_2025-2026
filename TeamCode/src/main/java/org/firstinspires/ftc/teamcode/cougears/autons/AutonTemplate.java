package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Disabled // ***DELETE ME***
@Autonomous (group = "Red")
public class AutonTemplate extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V3AutonBase bot;

    public int  numShots = 0;
    public ShootingPosition shootPos = redShootingPosArray[0];

    public enum pathStep {
        INIT_STEP,
        SHOOT_BALLS,
        END
    }
    pathStep currStep = pathStep.INIT_STEP;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) { bot.moveToPose(follower, RedBasicEndClose); } //CHANGE IF BLUE
        switch (currStep) {
            case INIT_STEP:
                bot.FWSpinTo(shootPos.getShootingVelocity()); //Change to where you want to shoot from
                bot.moveToPose(follower, shootPos.getShootingPose());
                setPathStep(pathStep.SHOOT_BALLS);
                break;
            case SHOOT_BALLS:
                if (!follower.isBusy()) { // Any step after a step which moves the bot must have this if statement to make sure we dont do anything until the bot is in teh right spot
//                bot.handleShootingSequence(); Rafi is finishing this up
                    setPathStep(pathStep.END);
                }
                break; // Break must be outside the if
            case END:
                if (!follower.isBusy()) {
                    bot.endAuton(follower, "Red");
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
        opModeTimer.resetTimer();
        setPathStep(pathStep.INIT_STEP);
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
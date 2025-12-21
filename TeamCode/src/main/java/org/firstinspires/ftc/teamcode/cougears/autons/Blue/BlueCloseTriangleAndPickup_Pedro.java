package org.firstinspires.ftc.teamcode.cougears.autons.Blue;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.V2AutonController;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Blue")
public class BlueCloseTriangleAndPickup_Pedro extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;
    private boolean firstShootingComplete = false;

    public enum pathStep {
        STARTPOS_SHOOTTRIANGLEPOS,
        FIRST_SHOOTING_SEQUENCE,
        SHOOTPOS_BALLDEPOT,
        BALLDEPOT_SHOOTPOS,
        SECOND_SHOOTING_SEQUENCE,
        SHOOTPOS_BASICEND,
        END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTTRIANGLEPOS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) { bot.moveToPose(follower, BlueBasicEnd);  }
        switch (currStep) {
            case STARTPOS_SHOOTTRIANGLEPOS:
                bot.spinUpClose();
                follower.followPath(BlueStartPosToBlueShootTrianglePos);
                setPathStep(pathStep.FIRST_SHOOTING_SEQUENCE);
                break;

            case FIRST_SHOOTING_SEQUENCE:
                // Use the common shooting sequence from V2AutonController
                if (!follower.isBusy()) {
                    boolean sequenceActive = bot.updateShootingSequence(follower);
                    if (!sequenceActive) {
                        // First shooting complete, go pickup more balls
                        setPathStep(pathStep.SHOOTPOS_BALLDEPOT);
                    }
                } else {
                    // Keep updating sequence even while follower is busy
                    bot.updateShootingSequence(follower);
                }
                break;

            case SHOOTPOS_BALLDEPOT:
                follower.setMaxPower(.5);
                bot.startIntake();
                follower.followPath(BlueShootTrianglePosToBallDepot1Pickup);
                setPathStep(pathStep.BALLDEPOT_SHOOTPOS);
                break;

            case BALLDEPOT_SHOOTPOS:
                if (!follower.isBusy()) {
                    follower.setMaxPower(1);
                    follower.followPath(BlueBallDepotEnd1ToBlueShootPos);
                    setPathStep(pathStep.SECOND_SHOOTING_SEQUENCE);
                }
                break;

            case SECOND_SHOOTING_SEQUENCE:
                // Use the common shooting sequence again for the second set of shots
                if (!follower.isBusy()) {
                    boolean sequenceActive = bot.updateShootingSequence(follower);
                    if (!sequenceActive) {
                        // Second shooting complete, go to end position
                        setPathStep(pathStep.SHOOTPOS_BASICEND);
                    }
                } else {
                    // Keep updating sequence even while follower is busy
                    bot.updateShootingSequence(follower);
                }
                break;

            case SHOOTPOS_BASICEND:
                follower.followPath(BlueShootTrianglePosToBlueBasicEnd);
                setPathStep(pathStep.END);
                break;

            case END:
                bot.endAuton("Blue");
                break;

            default:
                telemetry.addLine("No Step");
        }
    }

    public void setPathStep (pathStep newStep){
        currStep = newStep;
        stepTimer.resetTimer();

        // When entering shooting sequences, start them
        if (newStep == pathStep.FIRST_SHOOTING_SEQUENCE) {
            bot.resetShotCounter();
            bot.startShootingSequence(null);
        } else if (newStep == pathStep.SECOND_SHOOTING_SEQUENCE) {
            bot.resetShotCounter();
            bot.startShootingSequence(null);
        }
    }

    public void start(){
        opModeTimer.resetTimer();
        setPathStep(pathStep.STARTPOS_SHOOTTRIANGLEPOS);
    }

    @Override
    public void init() {
        stepTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(BlueStartPos);
        buildPaths(follower);
        bot = new V2AutonController(hardwareMap, telemetry);
        bot.botInit();
        bot.follower = follower; // Set follower reference in bot
    }

    @Override
    public void loop() {
        follower.update();
        stepUpdate();

        // Telemetry for debugging
        telemetry.addData("Current Step", currStep);
        telemetry.addData("Shots Fired", bot.getNumShots());
        telemetry.addData("Shoot Step", bot.getCurrentShootStep());
        telemetry.update();
    }
}
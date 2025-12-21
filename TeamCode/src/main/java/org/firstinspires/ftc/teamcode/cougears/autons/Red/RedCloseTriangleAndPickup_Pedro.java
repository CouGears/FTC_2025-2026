package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.V2AutonController;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedCloseTriangleAndPickup_Pedro extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;

    public enum pathStep {
        STARTPOS_SHOOTTRIANGLEPOS,
        FIRST_SHOOTING_SEQUENCE,
        SHOOTPOS_BALLDEPOTSTART,
        BALLDEPOTSTART_BALLDEPOTEND,
        BALLDEPOTEND_SHOOTPOS,
        SECOND_SHOOTING_SEQUENCE,
        SHOOTPOS_BASICEND,
        END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTTRIANGLEPOS;

    public void stepUpdate() {
        switch (currStep) {
            case STARTPOS_SHOOTTRIANGLEPOS:
                bot.spinUpClose();
                follower.followPath(RedStartPosToRedShootTrianglePos);
                setPathStep(pathStep.FIRST_SHOOTING_SEQUENCE);
                break;

            case FIRST_SHOOTING_SEQUENCE:
                // Check for timeout - if we're past 28 seconds, skip to end
                if (opModeTimer.getElapsedTimeSeconds() >= 28) {
                    setPathStep(pathStep.SHOOTPOS_BASICEND);
                    break;
                }

                // Use the common shooting sequence from V2AutonController
                if (!follower.isBusy()) {
                    // Check velocity optimization for faster shooting
                    if (bot.getNumShots() > 0 && bot.FW.getVelocity() >= FW_shootVel - 100) {
                        // Flywheel is up to speed, can proceed faster
                    }

                    boolean sequenceActive = bot.updateShootingSequence(follower);
                    if (!sequenceActive) {
                        // First shooting complete, go pickup more balls
                        setPathStep(pathStep.SHOOTPOS_BALLDEPOTSTART);
                    }
                } else {
                    // Keep updating sequence even while follower is busy
                    bot.updateShootingSequence(follower);
                }
                break;

            case SHOOTPOS_BALLDEPOTSTART:
                bot.startIntake();
                follower.followPath(RedShootPosToRedBallDepotStart1);
                setPathStep(pathStep.BALLDEPOTSTART_BALLDEPOTEND);
                break;

            case BALLDEPOTSTART_BALLDEPOTEND:
                if (!follower.isBusy()){
                    follower.setMaxPower(Auton_pickupSpeed);
                    follower.followPath(RedBallDepotStart1ToRedBallDepotEnd1);
                    setPathStep(pathStep.BALLDEPOTEND_SHOOTPOS);
                }
                break;

            case BALLDEPOTEND_SHOOTPOS:
                if (!follower.isBusy()) {
                    follower.setMaxPower(1);
                    follower.followPath(RedBallDepotEnd1ToRedShootPos);
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
                follower.followPath(RedShootTrianglePosToRedBasicEnd);
                setPathStep(pathStep.END);
                break;

            case END:
                bot.endAuton("Red");
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
        follower.setPose(RedStartPos);
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
        telemetry.addData("OpMode Time", opModeTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}
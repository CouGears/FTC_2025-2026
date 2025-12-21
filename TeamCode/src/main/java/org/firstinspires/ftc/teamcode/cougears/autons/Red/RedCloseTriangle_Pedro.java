package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.V2AutonController;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (group = "Red")
public class RedCloseTriangle_Pedro extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;

    public enum pathStep {
        STARTPOS_SHOOTTRIANGLEPOS,
        SHOOTING_SEQUENCE,
        SHOOTPOS_BASICEND,
        END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTTRIANGLEPOS;

    public void stepUpdate() {
        if (opModeTimer.getElapsedTimeSeconds() >= 28) { bot.moveToPose(follower, RedBasicEnd);  }
        switch (currStep) {
            case STARTPOS_SHOOTTRIANGLEPOS:
                bot.spinUpClose();
                follower.followPath(RedStartPosToRedShootTrianglePos);
                setPathStep(pathStep.SHOOTING_SEQUENCE);
                break;

            case SHOOTING_SEQUENCE:
                // Use the common shooting sequence from V2AutonController
                if (!follower.isBusy()) {
                    boolean sequenceActive = bot.updateShootingSequence(follower);
                    if (!sequenceActive) {
                        // Shooting complete, move to end
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

        // When entering shooting sequence, start it
        if (newStep == pathStep.SHOOTING_SEQUENCE) {
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
        telemetry.update();
    }
}
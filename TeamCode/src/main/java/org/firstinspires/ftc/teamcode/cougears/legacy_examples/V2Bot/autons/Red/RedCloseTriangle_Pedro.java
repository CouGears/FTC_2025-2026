package org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_ballTransferWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_firstShotExtraSpinupWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_gateWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_numberOfRepeatShots;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_pushNewBallWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_spinupWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.Auton_transferResetWait;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.FW_shootVel;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.autons.V2AutonController;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Disabled

@Autonomous (group = "Red")
public class RedCloseTriangle_Pedro extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;
    public int  numShots = 0;

    public enum pathStep {
        STARTPOS_SHOOTTRIANGLEPOS,
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL,
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
                setPathStep(pathStep.SPINUP);
                break;

            case SPINUP:
                if (!follower.isBusy()) {
                    if (numShots == 0 && stepTimer.getElapsedTime() >= Auton_spinupWait+Auton_firstShotExtraSpinupWait){
                        setPathStep(pathStep.OPEN);
                    } else if ((numShots > 0 && stepTimer.getElapsedTime() >= Auton_spinupWait) || bot.FW.getVelocity() >= FW_shootVel - 100) {
                        setPathStep(pathStep.OPEN);
                    }
                }
                break;
            case OPEN:
                bot.blockerOpen();
                bot.killIntake();
                if (stepTimer.getElapsedTime() >= Auton_gateWait) {
                    setPathStep(pathStep.SHOOT);
                }
                break;
            case SHOOT:
                if (!follower.isBusy()) {
                    bot.transferArmUp();
                    bot.spinFeeder();
                    if (stepTimer.getElapsedTime() >= Auton_ballTransferWait) {
                        setPathStep(pathStep.CLOSE);
                        numShots++;
                    }
                }
                break;
            case CLOSE:
                if (!follower.isBusy()) {
                    bot.blockerClose();
                    bot.transferArmDown();
                    bot.killFeeder();
                    if (numShots >= Auton_numberOfRepeatShots) {
                        setPathStep(pathStep.SHOOTPOS_BASICEND);
                    } else if (stepTimer.getElapsedTime() >= Auton_transferResetWait) {
                        setPathStep(pathStep.PUSH_NEW_BALL);
                    }
                }
                break;
            case PUSH_NEW_BALL:
                bot.startIntake();
                if (stepTimer.getElapsedTime() >= Auton_pushNewBallWait) {
                    setPathStep(pathStep.SPINUP);
                }
                break;

            case SHOOTPOS_BASICEND:
                follower.followPath(RedShootTrianglePosToRedBasicEnd);
                setPathStep(pathStep.END);
                break;

            case END:
                bot.endAuton(follower, "Red");
                break;

            default:
                telemetry.addLine("No Step");
        }
    }

    public void setPathStep (pathStep newStep){
        currStep = newStep;
        stepTimer.resetTimer();
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
package org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.autons.Red;

import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.PresetConstants.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.legacy_examples.V2Bot.autons.V2AutonController;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
// https://drive.google.com/drive/u/1/folders/195ZOaVz4Y1V8vUM4KzjuoF6N3_f7NXtK <- The vision
@Disabled

@Autonomous (group = "Red")
public class RedFarWall_Pedro extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;
    public int numShots = 0;

    public enum pathStep {
        // For moving: START_END
        // For action: ACTION
        // For movement with action: START_END_ACTION
        STARTPOS_SHOOTWALLPOS,
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL,
        END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTWALLPOS;

    public void stepUpdate() {
        switch (currStep) {
            case STARTPOS_SHOOTWALLPOS:
                bot.spinUpClose();
                follower.followPath(RedFarStartPosToRedShootWallPos);
                setPathStep(pathStep.SPINUP);
                break;
            case SPINUP:
                if (!follower.isBusy()) {
                    bot.spinUpClose();
                    if (numShots == 0 && stepTimer.getElapsedTime() >= Auton_spinupWait+Auton_firstShotExtraSpinupWait){
                        setPathStep(pathStep.OPEN);
                    } else if (numShots > 0 && stepTimer.getElapsedTime() >= Auton_spinupWait) {
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
                        setPathStep(pathStep.END);
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
        setPathStep(pathStep.STARTPOS_SHOOTWALLPOS);
    }

    @Override
    public void init() {
        stepTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(RedStartPosFar);
        buildPaths(follower);
        bot = new V2AutonController(hardwareMap, telemetry);
        bot.botInit();
    }

    @Override
    public void loop() {
        follower.update();
        stepUpdate();
    }
}

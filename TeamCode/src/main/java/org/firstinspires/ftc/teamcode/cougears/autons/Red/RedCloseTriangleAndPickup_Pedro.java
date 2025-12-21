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
    public int numShots = 0;

    public enum pathStep {
        // For moving: START_END
        // For action: ACTION
        // For movement with action: START_END_ACTION
        STARTPOS_SHOOTTRIANGLEPOS,
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL,
        SHOOTPOS_BALLDEPOTSTART, BALLDEPOTSTART_BALLDEPOTEND, BALLDEPOTEND_SHOOTPOS,
        SHOOTPOS_BASICEND, END
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
                        setPathStep(pathStep.SHOOTPOS_BALLDEPOTSTART);
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
                    setPathStep(pathStep.SPINUP);
                    numShots = 0;
                }
                break;

            case SHOOTPOS_BASICEND:
                follower.followPath(RedShootTrianglePosToRedBasicEnd);
                setPathStep(pathStep.END);
                break;
            case END:
                bot.endAuton();
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
    }
}

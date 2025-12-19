package org.firstinspires.ftc.teamcode.cougears.autons.Blue;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

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
    public int numShots = 0;

    public enum pathStep {
        // For moving: START_END
        // For action: ACTION
        // For movement with action: START_END_ACTION
        STARTPOS_SHOOTTRIANGLEPOS,
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL,
        SHOOTPOS_BALLDEPOT, BALLDEPOT_SHOOTPOS,
        SHOOTPOS_BASICEND, END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTTRIANGLEPOS;

    public void stepUpdate() {
        switch (currStep) {
            case STARTPOS_SHOOTTRIANGLEPOS:
                bot.spinUpClose();
                follower.followPath(BlueStartPosToBlueShootTrianglePos);
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
                        setPathStep(pathStep.SHOOTPOS_BALLDEPOT);
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
            case SHOOTPOS_BALLDEPOT:
                follower.setMaxPower(.5);
                bot.startIntake();
                follower.followPath(BlueShootTrianglePosToBallDepot1Pickup);
                setPathStep(pathStep.BALLDEPOT_SHOOTPOS);
                break;
            case BALLDEPOT_SHOOTPOS:
                follower.setMaxPower(1);
                follower.followPath(BlueBallDepotEnd1ToBlueShootPos);
                setPathStep(pathStep.OPEN);
                numShots = 0;
                break;

            case SHOOTPOS_BASICEND:
                follower.followPath(BlueShootTrianglePosToBlueBasicEnd);
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
        follower.setPose(BlueStartPos);
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

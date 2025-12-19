package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static org.firstinspires.ftc.teamcode.cougears.autons.Positions.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class SamplePedroAuton extends OpMode {
    public Follower follower;
    public Timer stepTimer, opModeTimer;
    public V2AutonController bot;
    public int numShots = 0;

    public enum pathStep {
        // For moving: START_END
        // For action: ACTION
        // For movement with action: START_END_ACTION
        STARTPOS_SHOOTPOS,
        SPINUP, OPEN, SHOOT, CLOSE, PUSH_NEW_BALL,
        END
    }
    pathStep currStep = pathStep.STARTPOS_SHOOTPOS;

    private PathChain Path_StartPos_ShootPos;
    public void buildPaths(){
        Path_StartPos_ShootPos = follower.pathBuilder()
                .addPath(new BezierLine(RedStartPos, RedShootPos))
                .setLinearHeadingInterpolation(RedStartPos.getHeading(), RedShootPos.getHeading())
                .build();
    }
    public void stepUpdate() {
        switch (currStep) {
            case STARTPOS_SHOOTPOS:
                bot.spinUpClose();
                follower.followPath(Path_StartPos_ShootPos);
                setPathStep(pathStep.SPINUP);
                break;
            case SPINUP:
                if (!follower.isBusy()) {
                    bot.spinUpClose();
                    if (stepTimer.getElapsedTime() >= Auton_spinupWait){
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
                    if (numShots >= 5) {
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
        setPathStep(pathStep.STARTPOS_SHOOTPOS);
    }

    @Override
    public void init() {
        stepTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setPose(RedStartPos);

        bot = new V2AutonController(hardwareMap, telemetry);
        bot.botInit();
    }

    @Override
    public void loop() {
        follower.update();
        stepUpdate();
    }
}

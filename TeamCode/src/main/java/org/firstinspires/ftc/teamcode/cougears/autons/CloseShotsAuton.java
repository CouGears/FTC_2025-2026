package org.firstinspires.ftc.teamcode.cougears.autons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.V2TeleOpBase;

public class CloseShotsAuton extends LinearOpMode {
    public V2TeleOpBase bot = null;
    @Override
    public void runOpMode(){
        bot = new V2TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();
        telemetry.addLine("Ready");
        waitForStart();
        while (opModeIsActive()){
            bot.createTimer("MoveBackAndInit");
            while(!bot.timerExpired_Seconds("MoveBackAndInit", timeBackwardsClose)) {
                bot.spinUpClose();
                bot.manualMove(speedBackwardsClose, 0 ,0);
            }
            bot.manualMove(0, 0 ,0);

            boolean doneShooting = false;
            int numberOfShots = 0;
            bot.createTimer("ShootSequenceStep1");
            bot.createTimer("ShootSequenceStep2");
            bot.createTimer("ShootSequenceStep3");
            while (!doneShooting) {
                bot.spinUpClose();
                if (bot.timerExpired_Seconds("ShootSequenceStep1", 1)){
                    bot.transferArmUp();
                    bot.killIntake();
                    bot.deleteTimer("ShootSequenceStep1");
                }
                if (bot.timerExpired_Seconds("ShootSequenceStep2", 2)){
                    bot.transferArmDown();
                    bot.deleteTimer("ShootSequenceStep2");
                }
                if (bot.timerExpired_Seconds("ShootSequenceStep3", 2.5)){
                    bot.startIntake();

                    bot.createTimer("ShootSequenceStep1");
                    bot.createTimer("ShootSequenceStep2");
                    bot.createTimer("ShootSequenceStep3");
                    numberOfShots++;
                }
                if (numberOfShots == repeatShots) doneShooting = true;
            }
            terminateOpModeNow();
        }
        bot.endTeleOp();
    }
}

package org.firstinspires.ftc.teamcode.cougears.autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.V2TeleOpBase;
@Autonomous
public class CloseShotsAuton extends LinearOpMode {
    public V2TeleOpBase bot = null;
    @Override
    public void runOpMode(){
        bot = new V2TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();
        telemetry.addLine("Ready");
        waitForStart();

        bot.createTimer("MoveBackAndInit");
        while(!bot.timerExpired_Seconds("MoveBackAndInit", timeBackwardsClose)) {
            bot.spinUpClose();
            bot.manualMove(speedBackwardsClose, 0 ,0);
        }
        bot.manualMove(0, 0 ,0);

        for (int i = 0; i < repeatShots; i++) {
            if (!opModeIsActive()) break; // Allow stopping mid-sequence
            telemetry.addData("Shooting Shot", i + 1);
            telemetry.update();
            bot.blockerOpen();
            sleep((long) gateWait);
            bot.transferArmUp();
            bot.spinFeeder();
            sleep((long) shootSequenceWait);
            bot.killFeeder();
            bot.transferArmDown();
            bot.blockerClose();
            sleep((long) gateWait);
        }
        bot.endTeleOp();
    }
}

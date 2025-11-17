package org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.teleops.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagManager;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.OldPresetConstants.*;
@Disabled

@Autonomous(name="CloseShotsAuton", group="Autonomous")
public class CloseShotsAuton extends LinearOpMode {

    @Override
    public void runOpMode() {
        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        AprilTagManager ATM = new AprilTagManager(hardwareMap, telemetry, bot);
        bot.botInit();
        ATM.initAprilTag();

        // Signal that initialization is complete
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();

        bot.createTimer("MoveBack");
        while(!bot.timerExpired_Seconds("MoveBack", timeBack) && opModeIsActive()){
            bot.motorFR.setPower(.5);
            bot.motorBR.setPower(.5);
            bot.motorFL.setPower(.5);
            bot.motorBL.setPower(.5);
        }
        bot.motorFR.setPower(0);
        bot.motorBR.setPower(0);
        bot.motorFL.setPower(0);
        bot.motorBL.setPower(0);

        bot.createTimer("AutonInit");
        while (!bot.timerExpired_Seconds("AutonInit", spinUpTime) && opModeIsActive()){
            bot.FW.setVelocity(autonCloseShot);
        }

        for (int i = 0; i < 5 && opModeIsActive(); i++){
            bot.GateServoPush();
            sleep(gateUpTimeMS);
            bot.PushServoPush();
            sleep(generalCycleTime);
            bot.PushServoReset();
            bot.GateServoReset();
            sleep(generalCycleTime);
        }
        bot.spinDown();
        bot.endTeleOp();
    }
}
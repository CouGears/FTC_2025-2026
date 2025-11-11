package org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.teleops.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagManager;
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.OldPresetConstants.*;
@Disabled

@Autonomous(name="FarShotsAuton", group="Autonomous")
public class FarShotsAuton extends LinearOpMode {

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

        bot.createTimer("MoveForward");
        while(!bot.timerExpired_Seconds("MoveForward", timeFwd) ){
            bot.motorFR.setPower(-.2);
            bot.motorBR.setPower(-.2);
            bot.motorFL.setPower(-.2);
            bot.motorBL.setPower(-.2);
        }
        bot.motorFR.setPower(0);
        bot.motorBR.setPower(0);
        bot.motorFL.setPower(0);
        bot.motorBL.setPower(0);

        bot.createTimer("AutonInit");
        while (!bot.timerExpired_Seconds("AutonInit", spinUpTime)){
            ATM.alignToAT(redTag);
            ATM.alignToAT(blueTag);
            bot.FW.setVelocity(autonFarShot);
        }

        for (int i = 0; i < 5; i++){
            bot.GateServoPush();
            sleep(gateUpTimeMS);
            if (i < 3)
                bot.PushServoPush();
            sleep(generalCycleTime);
            bot.PushServoReset();
            bot.GateServoReset();
            sleep(generalCycleTime);
        }
        bot.spinDown();

        bot.createTimer("MoveForward");
        while(!bot.timerExpired_Seconds("MoveForward", 2)){
            bot.motorFR.setPower(-.2);
            bot.motorBR.setPower(-.2);
            bot.motorFL.setPower(-.2);
            bot.motorBL.setPower(-.2);
        }
        bot.endTeleOp();
        terminateOpModeNow();
    }
}
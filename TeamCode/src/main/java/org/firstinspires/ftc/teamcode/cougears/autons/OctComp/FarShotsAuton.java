package org.firstinspires.ftc.teamcode.cougears.autons.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cougears.teleops.DC_Teleopbase;
import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.teamcode.cougears.util.DC_ATM;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
@Autonomous(name="FarShotsAuton", group="Autonomous")
public class FarShotsAuton extends LinearOpMode {

    @Override
    public void runOpMode() {
        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        DC_ATM ATM = new DC_ATM(hardwareMap, telemetry, bot);
        bot.botInit();
        ATM.initAprilTag();

        // Signal that initialization is complete
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            bot.createTimer("AutonInit");
            while (!bot.timerExpired_Seconds("AutonInit", 3)){
                bot.spinUpFar();
                ATM.alignToAT(redTag);
                ATM.alignToAT(blueTag);
            }

            for (int i = 0; i < 5; i++){
                bot.GateServoPush();
                sleep(500);
                bot.GateServoReset();
                sleep(500);
                bot.PushServoPush();
                sleep(500);
                bot.PushServoReset();
                sleep(500);
            }
            bot.spinDown();

            bot.createTimer("MoveForward");
            while(!bot.timerExpired_Seconds("MoveForward", 2)){
                bot.manualMove(.2,0,0);
            }
        }
        bot.endTeleOp();
    }
}
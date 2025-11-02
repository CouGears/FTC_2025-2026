package org.firstinspires.ftc.teamcode.cougears.autons.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cougears.teleops.DC_Teleopbase;
import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.teamcode.cougears.util.DC_ATM;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
@Autonomous(name="CloseShotsAuton", group="Autonomous")
public class CloseShotsAuton extends LinearOpMode {

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

        bot.createTimer("MoveBack");
        while(!bot.timerExpired_Seconds("MoveBack", timeFwd) && opModeIsActive()){
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

        bot.createTimer("MoveSideways");
        while(!bot.timerExpired_Seconds("MoveSideways", 2) && opModeIsActive()){
            bot.manualMove(0,.2,0);
        }
        bot.endTeleOp();
    }
}
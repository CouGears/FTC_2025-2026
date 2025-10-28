package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.shootVel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="OctoberCompTeleop_Drive 2", group="Drive")
public class OctoberCompTeleop_Drive_2_Controller extends LinearOpMode {

    @Override
    public void runOpMode() {
        boolean slowed = false;
        OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        // Initialize motors
        bot.botInit();
        APM AP = new APM(bot);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();


        while (opModeIsActive()) {
            //****** DRIVE ******
            if(bot.isPressed(1, Button.B)){
                slowed = !slowed;
            }
            if (!slowed){
                bot.RafiDrive(gamepad1);
            } else {
                bot.SlowRafiDrive(gamepad1);
            }
            telemetry.addData("Slowed", "%b", slowed);


            //****** FLYWHEEL ******
            if (bot.isPressed(2, Button.X)) {
                bot.FWSpinning = !bot.FWSpinning;
                if (bot.FWSpinning) {
                    bot.spinUp();
                } else {
                    bot.spinDown();
                }
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());
            telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", shootVel);

            //****** SERVOS ******
            if (bot.isPressed(2, Button.L_BUMPER)) {
                bot.GateServoUp = !bot.GateServoUp;
                if (bot.GateServoUp) {
                    bot.GateServoPush();
                } else {
                    bot.GateServoReset();
                }
            }


            telemetry.update();
            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

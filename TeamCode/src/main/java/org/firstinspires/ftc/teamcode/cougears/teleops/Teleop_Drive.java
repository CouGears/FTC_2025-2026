package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="Teleop_Drive", group="Drive")

public class Teleop_Drive extends LinearOpMode {

    boolean spinOn = false;
    boolean intakeIsOn = false;
    @Override
    public void runOpMode() {
        DC_Teleopbase bot = new DC_Teleopbase(hardwareMap, telemetry, gamepad1, gamepad2);
        // Initialize motors
        bot.botInit();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();


        while (opModeIsActive()) {
            bot.drive(gamepad1);
            if(bot.isPressed(1, Button.X)) {
                spinOn = !spinOn;
                if(spinOn)
                    bot.spinUp();
                else
                    bot.spinDown();
            }
            if(bot.isPressed(1, Button.Y)) {
                intakeIsOn = !intakeIsOn;
                if(intakeIsOn)
                    bot.intakeOn();
                else
                    bot.intakeOff();
            }
            if(gamepad1.dpad_up){
                bot.conveyorUp();
            }else if(gamepad1.dpad_down){
                bot.conveyorDown();
            }else{
                bot.conveyorOff();
            }
            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

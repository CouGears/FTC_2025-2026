package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(name="V3Teleop", group="Drive")

public class V3TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap);
        // Initialize motors
        bot.botInit();
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();


        while (opModeIsActive()) {
            //****** DRIVE (Controller 1)******
            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (!bot.isHeld(1, Button.DPAD_DOWN) && PTM.follower.isBusy()){
                PTM.breakFollower();
            }
            if (!PTM.isBusy()) {
                PTM.updatePos(); // Update w/o motors
                if (bot.isPressed(1, Button.B)) {
                    bot.toggleSlow();
                }
                bot.RafiDrive(gamepad1);
                telemetry.addData("Slowed", "%b", bot.slowed);
            }

            if (bot.isPressed(1, Button.Y)) {
                PTM.switchGoal();
            }
            telemetry.addData("Assigned Goal", "%s", PTM.getGoal());

            //****** AUTON MOVING ******
            if (bot.isHeld(1, Button.DPAD_DOWN)){
                if (PTM.getGoal().equals("Red"))
                    PTM.moveToPos(RedShootTrianglePos);
                else if (PTM.getGoal().equals("Blue"))
                    PTM.moveToPos(BlueShootTrianglePos);
                PTM.updatePosAndMotors();
            }
            if (bot.isHeld(1, Button.DPAD_UP)) {
                PTM.alignToGoal();
            }


            //****** INTAKE ******
            if (bot.isPressed(1, Button.X)) {
                bot.deleteTimer("RejectIntake");
                if (!bot.IntakeSpinning)
                    bot.startIntake();
                else
                    bot.killIntake();
            }

            //****** FLYWHEEL (Controller 2)******
            // BUTTONS: L_TRIGGER, L_BUMPER, R_TRIGGER, R_BUMPER
            if (bot.isHeld(2, Button.L_TRIGGER)) {
                bot.spinUpClose();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", FW_shootVel);
            }
            else if (bot.isHeld(2, Button.L_BUMPER)) {
                bot.ejectFW();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", FW_ejectionVel);
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** SHOOT SEQUENCE (Controller 2)******
            if (bot.isHeld(2, Button.R_TRIGGER)) {
                bot.openBlocker();
                if ((bot.blockerIsOpen() && bot.FWUpToSpeed(FW_shootVel)) || bot.isHeld(2, Button.R_BUMPER)){
                    bot.startTransfer();
                }
                else
                    bot.killTransfer();
            }

            //****** EJECT BALLS (Controller 1 & 2)******
            if (bot.isPressed(2, Button.R_STICKPRESS) || bot.isPressed(1, Button.R_STICKPRESS)) {
                bot.ejectIntake();
                bot.createTimer("Eject");
            }
            if (bot.timerExpired_MSeconds("Eject", 1500)){
                bot.startIntake();
                bot.deleteTimer("Eject");
            }

            bot.update();
            sleep(10);
        }
        bot.endTeleOp();
    }
}

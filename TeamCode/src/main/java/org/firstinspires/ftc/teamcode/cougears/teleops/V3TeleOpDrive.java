package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.goalUtils;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(name="V2Teleop", group="Drive")

public class V3TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        goalUtils goal = new goalUtils();
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
            // BUTTONS: L-Joystick, R-Joystick, B
            if (bot.isPressed(1, Button.B)) {
                bot.toggleSlow();
            }
            if (!bot.isHeld(1, Button.DPAD_DOWN) && PTM.follower.isBusy()){
                PTM.follower.breakFollowing();
            }

            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (PTM.follower.isBusy()) {
                telemetry.addLine("PEDRO IS DRIVING");
//                PTM.update();
            } else {
//                PTM.follower.setPose(bot.getPedroPose());
                PTM.follower.updatePose();
                if (bot.isPressed(1, Button.B)) {
                    bot.toggleSlow();
                }
                bot.RafiDrive(gamepad1);
                telemetry.addData("Slowed", "%b", bot.slowed);
            }

            if (bot.isPressed(1, Button.Y)) {
                goal.switchLockedGoal();
            }
            goal.displayLockedTag(telemetry);

            //****** AUTON MOVING ******
            if (bot.isHeld(1, Button.DPAD_DOWN)){
                if (goal.getLockedTagID() == AT_redTag)
                    PTM.moveToPos(RedShootTrianglePos);
                else if (goal.getLockedTagID() == AT_blueTag)
                    PTM.moveToPos(BlueShootTrianglePos);
                PTM.update();
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
                bot.spinUpFar();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", FW_shootVelFar);
            } else if (bot.isHeld(2, Button.R_BUMPER)) {
                bot.ejectFW();
                telemetry.addData("Flywheel", "AIMING FOR  vel %.2f", FW_ejectionVel);
            } else {
                bot.killFW();
            }
            telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

            //****** SHOOT SEQUENCE (Controller 2)******
            // BUTTONS: R_TRIGGER
            if (bot.isPressed(2, Button.R_TRIGGER)) {
                if (bot.timers.get("ShootSequence") == null) { // Not in the middle of a sequence
                    bot.blockerOpen();
                    bot.createTimer("ShootSequence");
                }
                else {
                    bot.createTimer("ShootSequence", (long) Auton_gateWait); // Dont want to waste time opeing gate if gate is already open
                }
            }
            bot.handleShootSequence();

            //****** EJECT BALLS (Controller 1 & 2)******
            // R_STICKPRESS
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

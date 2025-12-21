package org.firstinspires.ftc.teamcode.cougears.teleops;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.goalUtils;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(name="V2Teleop", group="Drive")
public class V2TeleOpDrive extends LinearOpMode {

    @Override
    public void runOpMode() {
        goalUtils goal = new goalUtils();
        V2TeleOpBase bot = new V2TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        V2AprilTagManager ATM = new V2AprilTagManager(hardwareMap, telemetry, bot);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap);

        bot.botInit();
        ATM.initAprilTag();
        goal.getLockedGoal(); // Initialize goal based on auton color

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Starting Alliance", goal.getLockedTagIndex() == 0 ? "RED" : "BLUE");
        telemetry.update();
        waitForStart();

        // Check if we're in two-controller mode
        boolean twoControllerMode = (gamepad2.getGamepadId() != -1);

        while (opModeIsActive()) {

            //****** DRIVE ******
            // Slow mode toggle - C1 only
            if (bot.isPressed(1, Button.B)) bot.toggleSlow();

            // Auto-drive to shooting position - either controller can trigger
            if (bot.isHeld(Button.DPAD_DOWN, Button.DPAD_DOWN)){
                PTM.moveToPos(RedShootTrianglePos);
                PTM.update();
            } else if (PTM.follower.isBusy()){
                PTM.follower.breakFollowing();
            }

            // Drive control
            telemetry.addData("Is PedroBusy?", "%b", PTM.follower.isBusy());
            if (PTM.follower.isBusy()) {
                telemetry.addLine("PEDRO IS DRIVING");
                PTM.update();
            } else {
                PTM.follower.setPose(bot.getPedroPose());

                // RafiDrive handles both modes internally
                bot.RafiDrive(gamepad1, twoControllerMode);

                telemetry.addData("Slowed", "%b", bot.slowed);
                telemetry.addData("Control Mode", twoControllerMode ? "TWO CONTROLLER" : "ONE CONTROLLER");
            }

            // ========== ONE CONTROLLER MODE ==========
            if (!twoControllerMode) {
                //****** ORIGINAL SINGLE CONTROLLER CONTROLS ******

                //****** GOAL LOCK (C1: DPAD_UP) ******
                if (bot.isPressed(1, Button.DPAD_UP)) goal.toggleTagLock();
                goal.displayLockedTag(telemetry);
                if (goal.isTagLockEnabled()) bot.odoTurretAdjust();

                //****** INTAKE (C1: X) ******
                if (bot.isPressed(1, Button.X)) {
                    bot.deleteTimer("RejectIntake");
                    if (!bot.IntakeSpinning) bot.startIntake();
                    else bot.killIntake();
                }

                //****** FLYWHEEL (C1: Triggers/Bumpers) ******
                if (bot.isHeld(1, Button.L_TRIGGER)) {
                    bot.spinUpClose();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_shootVel);
                }
                else if (bot.isHeld(1, Button.L_BUMPER)) {
                    bot.spinUpFar();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_shootVelFar);
                } else if (bot.isHeld(1, Button.R_BUMPER)) {
                    bot.ejectFW();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_ejectionVel);
                } else {
                    bot.killFW();
                }
                telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

                //****** TURRET (C1: A/DPAD_L/DPAD_R) ******
                if (bot.isPressed(1, Button.A)) bot.resetTurret();
                else if (bot.isPressed(1, Button.DPAD_RIGHT)) bot.moveTurretR();
                else if (bot.isPressed(1, Button.DPAD_LEFT)) bot.moveTurretL();

                //****** SHOOT SEQUENCE (C1: R_TRIGGER) ******
                if (bot.isPressed(1, Button.R_TRIGGER)) {
                    bot.blockerOpen();
                    if (bot.timers.get("ShootSequence") == null) bot.createTimer("ShootSequence");
                    else bot.createTimer("ShootSequence", (long) Auton_gateWait);
                }

                if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait + Auton_transferResetWait)){
                    bot.killFeeder();
                    bot.transferArmDown();
                    bot.blockerClose();
                } else if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait)){
                    bot.spinFeeder();
                    bot.transferArmUp();
                    bot.killIntake();
                }

                if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait + 1250)){
                    bot.startIntake();
                    bot.deleteTimer("ShootSequence");
                }

                //****** EJECT BALLS (C1: R_STICKPRESS) ******
                if (bot.isPressed(1, Button.R_STICKPRESS)) {
                    bot.ejectFeeder();
                    bot.ejectIntake();
                    bot.createTimer("Eject");
                }
                if (bot.timerExpired_MSeconds("Eject", 1500)){
                    bot.killFeeder();
                    bot.startIntake();
                    bot.deleteTimer("Eject");
                }
            }
            // ========== TWO CONTROLLER MODE ==========
            else {
                //****** NEW DUAL CONTROLLER CONTROLS ******

                //****** CONTROLLER 1 (Driver) ******
                // Right stick Y = forward/backward
                // Left stick X = strafe
                // Right trigger = turn right
                // Left trigger = turn left
                // B = slow mode (handled above)

                //****** GOAL LOCK - Either controller (C1: DPAD_UP, C2: DPAD_UP) ******
                if (bot.isPressed(Button.DPAD_UP, Button.DPAD_UP)) goal.toggleTagLock();
                goal.displayLockedTag(telemetry);
                if (goal.isTagLockEnabled()) bot.odoTurretAdjust();

                //****** TURRET MANUAL - Either controller (C1: DPAD_L/R, C2: DPAD_L/R) ******
                if (bot.isPressed(Button.DPAD_LEFT, Button.DPAD_LEFT)) bot.moveTurretL();
                else if (bot.isPressed(Button.DPAD_RIGHT, Button.DPAD_RIGHT)) bot.moveTurretR();

                //****** INTAKE - C1 only (C1: X) ******
                if (bot.isPressed(1, Button.X)) {
                    bot.deleteTimer("RejectIntake");
                    if (!bot.IntakeSpinning) bot.startIntake();
                    else bot.killIntake();
                }

                //****** REJECT INTAKE - Either controller (C1: Y, C2: Y) ******
                if (bot.isPressed(Button.Y, Button.Y)) {
                    bot.ejectIntake();
                    bot.createTimer("RejectIntake");
                }
                if (bot.timerExpired_MSeconds("RejectIntake", 1000)){
                    bot.startIntake();
                    bot.deleteTimer("RejectIntake");
                }

                //****** CONTROLLER 2 (Operator) ******

                //****** TURRET RESET - C2 only (C2: A) ******
                if (bot.isPressed(2, Button.A)) bot.resetTurret();

                //****** FLYWHEEL - C2 only (C2: L_TRIGGER/L_BUMPER/R_BUMPER) ******
                if (bot.isHeld(2, Button.L_TRIGGER)) {
                    bot.spinUpClose();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_shootVel);
                }
                else if (bot.isHeld(2, Button.L_BUMPER)) {
                    bot.spinUpFar();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_shootVelFar);
                } else if (bot.isHeld(2, Button.R_BUMPER)) {
                    bot.ejectFW();
                    telemetry.addData("Flywheel", "AIMING FOR vel %.2f", FW_ejectionVel);
                } else {
                    bot.killFW();
                }
                telemetry.addData("Flywheel", "RUNNING at vel %.2f", bot.FW.getVelocity());

                //****** SHOOT SEQUENCE - C2 only (C2: R_TRIGGER) ******
                if (bot.isPressed(2, Button.R_TRIGGER)) {
                    bot.blockerOpen();
                    if (bot.timers.get("ShootSequence") == null) bot.createTimer("ShootSequence");
                    else bot.createTimer("ShootSequence", (long) Auton_gateWait);
                }

                if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait + Auton_transferResetWait)){
                    bot.killFeeder();
                    bot.transferArmDown();
                    bot.blockerClose();
                } else if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait)){
                    bot.spinFeeder();
                    bot.transferArmUp();
                    bot.killIntake();
                }

                if (bot.timerExpired_MSeconds("ShootSequence", Auton_gateWait + 1250)){
                    bot.startIntake();
                    bot.deleteTimer("ShootSequence");
                }

                //****** EJECT BALLS - C2 only (C2: R_STICKPRESS) ******
                if (bot.isPressed(2, Button.R_STICKPRESS)) {
                    bot.ejectFeeder();
                    bot.ejectIntake();
                    bot.createTimer("Eject");
                }
                if (bot.timerExpired_MSeconds("Eject", 1500)){
                    bot.killFeeder();
                    bot.startIntake();
                    bot.deleteTimer("Eject");
                }
            }

            bot.update();
            telemetry.update();
            sleep(10);
        }

        bot.endTeleOp();
    }
}
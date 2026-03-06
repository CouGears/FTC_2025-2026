package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;


@TeleOp(name="StorageTest", group="Testing")
public class StorageTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Initialize motors
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap, Storage.Storage_endOfAutonPose);

        bot.botInit();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            bot.RafiDrive(gamepad1, false);
            telemetry.addData("Current X Pos", PTM.getCurrPos().getX());
            telemetry.addData("Current Y Pos", PTM.getCurrPos().getY());
            telemetry.addData("Current Head", PTM.getCurrPos().getHeading());
            telemetry.addData("Current Color", PTM.getGoal());
            telemetry.addLine("--------------------");
            telemetry.addData("Storage X Pos", Storage.Storage_endOfAutonPose.getX());
            telemetry.addData("Storage Y Pos", Storage.Storage_endOfAutonPose.getY());
            telemetry.addData("Storage Head", Storage.Storage_endOfAutonPose.getHeading());
            telemetry.addData("Storage Color", Storage.Storage_endOfAutonColor);

            telemetry.addLine("\n Press X to save pose to Storage");
            telemetry.addLine("Press Y to switch color");


            if (bot.isPressed(1, Button.X)){
                Storage.Storage_endOfAutonPose = PTM.getCurrPos();
                Storage.Storage_endOfAutonColor = PTM.getGoal();
            }
            if (bot.isPressed(1, Button.Y)){
                PTM.switchGoal();
            }

            bot.update();
            PTM.updatePos();
        }
        bot.endTeleOp();
    }
}
package org.firstinspires.ftc.teamcode.cougears.testing.Driving;


import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

public class PoseFinder extends LinearOpMode {
    V3TeleOpBase bot = null;
    @Override
    public void runOpMode() throws InterruptedException {
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap, new Pose(0,0,0)); //TODO @rafi
        bot.botInit();
        waitForStart();
        while(opModeIsActive()){
            bot.drive(gamepad1);
            telemetry.addData("x", "%.5f", PTM.getCurrPos().getX());
            telemetry.addData("y", "%.5f", PTM.getCurrPos().getY());
            telemetry.addData("Heading", "%.5f", PTM.getCurrPos().getHeading());
            PTM.updatePos();
            bot.update();
        }
    }
}

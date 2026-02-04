package org.firstinspires.ftc.teamcode.cougears.testing.Driving;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(group="Testing")
public class PoseFinderRed extends LinearOpMode {
    V3TeleOpBase bot = null;
    @Override
    public void runOpMode() throws InterruptedException {
        V3TeleOpBase bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        PedroTeleOpManager PTM = new PedroTeleOpManager(hardwareMap, RedStartPos);
        bot.botInit();
        waitForStart();
        while(opModeIsActive()){
            bot.RafiDrive(gamepad1, false);
            telemetry.addData("x", "%.5f", PTM.getCurrPos().getX());
            telemetry.addData("y", "%.5f", PTM.getCurrPos().getY());
            telemetry.addData("Heading", "%.5f", Math.toDegrees(PTM.getCurrPos().getHeading()));
            PTM.updatePos();
            bot.update();
        }
    }
}

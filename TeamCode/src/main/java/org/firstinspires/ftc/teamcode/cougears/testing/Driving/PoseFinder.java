package org.firstinspires.ftc.teamcode.cougears.testing.Driving;


import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Drive_switchedJoysticks;

import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;

@TeleOp(group="Testing")
public class PoseFinder extends OpMode {
    V3TeleOpBase bot = null;
    PedroTeleOpManager PTM = null;
    Pose startingPos = RedStartPos;
    String startingPoseName = "Red Start";

    @Override
    public void init() {
        bot = new V3TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();
    }

    @Override
    public void init_loop() {
        telemetry.addLine("Press A to start from Red, B to start from Blue, X for Red Anchor");
        telemetry.addData("Currently selected starting pos", "%s", startingPoseName);
        if (gamepad1.a){
            startingPos = RedStartPos;
            startingPoseName = "Red Start";
        } else if (gamepad1.b) {
            startingPos = BlueStartPos;
            startingPoseName = "Blue Start";
        } else if (gamepad1.x) {
            startingPos = RedAnchorPoint;
            startingPoseName = "Red Anchor Point";
        }
        telemetry.update();
        super.init_loop();
    }

    @Override
    public void start() {
        PTM = new PedroTeleOpManager(hardwareMap, startingPos);
        super.start();
    }

    @Override
    public void loop() {
        bot.RafiDrive(gamepad1, Drive_switchedJoysticks);
        telemetry.addData("x", "%.5f", PTM.getCurrPos().getX());
        telemetry.addData("y", "%.5f", PTM.getCurrPos().getY());
        telemetry.addData("Heading", "%.5f", Math.toDegrees(PTM.getCurrPos().getHeading()));
        PTM.updatePos();
        bot.update();
    }
}

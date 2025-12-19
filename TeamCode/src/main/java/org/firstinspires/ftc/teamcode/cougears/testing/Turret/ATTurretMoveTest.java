package org.firstinspires.ftc.teamcode.cougears.testing.Turret;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.teleops.V2TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.teleops.V2AprilTagManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

@TeleOp (name = "ATTurretMoveTest", group = "Testing")
public class ATTurretMoveTest extends LinearOpMode {

    @Override
    public void runOpMode(){
        V2TeleOpBase bot = new V2TeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
        V2AprilTagManager ATM = new V2AprilTagManager(hardwareMap, telemetry, bot);
        bot.botInit();
        ATM.initAprilTag();
        telemetry.addLine("Initlized");
        waitForStart();
        while (opModeIsActive()){
            AprilTagDetection blueTagDetection = ATM.scanForAT(AT_blueTag);
            if (blueTagDetection != null)
            {
                telemetry.addLine("blueTagID = " + blueTagDetection.id);
                telemetry.addLine("blueTagBearing = " + blueTagDetection.ftcPose.bearing);
            }

            AprilTagDetection redTagDetection = ATM.scanForAT(AT_redTag);
            if (redTagDetection != null)
            {
                telemetry.addLine("redTagID = " + redTagDetection.id);
                telemetry.addLine("redTagBearing = " + redTagDetection.ftcPose.bearing);
            }
            if (bot.isPressed(1, GamepadManager.Button.Y)) {

            }
            bot.update();
            sleep(10);
        }
    }
}

package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagManager;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.PresetConstants;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name="ATMTest", group="Testing")
public class ATMTest extends LinearOpMode {

    @Override
    public void runOpMode(){
        BotBase bot = new BotBase(hardwareMap, telemetry, gamepad1, gamepad2);
        AprilTagManager ATM = new AprilTagManager(hardwareMap, telemetry, bot);
        while (opModeIsActive()){
            bot.drive(gamepad1);
            telemetry.addData("ALIGN:", "L_Bumper");
            telemetry.addData("FULL AUTO:", "R_Bumper");

            AprilTagDetection detectedTag = ATM.scanForAT(blueTag);
            if (detectedTag == null) {
                detectedTag = ATM.scanForAT(redTag);
            }
            telemetry.addData("Detected Tag?", "%b");

            if (detectedTag != null) {
                if (bot.isHeld(1, GamepadManager.Button.L_BUMPER)) {
                    ATM.alignToAT(detectedTag.id);
                } else if (bot.isHeld(1, GamepadManager.Button.R_BUMPER)) {
                    ATM.FullAutoMove(detectedTag.id);
                }
            }

            bot.update();
        }
    }
}

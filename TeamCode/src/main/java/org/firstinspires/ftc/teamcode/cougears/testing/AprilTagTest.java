package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

@TeleOp(name = "AprilTagTest", group = "Testing")
public class AprilTagTest extends LinearOpMode {

    private final int redTag = 24;
    private final int blueTag = 20;
    private final double desiredDist = 67.0;

    private int targetTagID = redTag;
    private BotBase bot;

    private final double manualPowerScale = 1.0;

    @Override
    public void runOpMode() {
        while (opModeIsActive()) {
            bot.update();

            // --- Tag selection ---
            if (bot.isPressed(1, GamepadManager.Button.A)) {
                targetTagID = blueTag;
            } else if (bot.isPressed(1, GamepadManager.Button.B)) {
                targetTagID = redTag;
            }

            // --- Manual input ---
            double forward = -gamepad1.right_stick_y * manualPowerScale; // up = forward
            double strafe  =  -gamepad1.right_stick_x * manualPowerScale; // left/right = strafe
            double rotate  =  gamepad1.left_stick_x * manualPowerScale;  // left stick X = rotate

            // --- Auto control ---
            // <<< CHANGED: use isHeld(...) so alignToTag runs continuously while RB is held >>>
            if (bot.isHeld(1, GamepadManager.Button.R_BUMPER)) {
                double rotatePower = bot.alignToTag(targetTagID);
                telemetry.addData("Mode", "Align to Tag (RB held)");
                telemetry.addData("Rotate", String.format("%.2f", rotatePower));
            } else if (bot.isPressed(1, GamepadManager.Button.L_BUMPER)) {
                double drivePower = bot.moveToATDist(targetTagID, desiredDist);
                telemetry.addData("Mode", "Move to Distance (LB)");
                telemetry.addData("Drive", String.format("%.2f", drivePower));
            } else {
                bot.drive(gamepad1);
                telemetry.addData("Mode", "Manual Drive");
            }

            bot.ATval(targetTagID, true);
            telemetry.update();

            sleep(20);
        }

        bot.endTeleOp();
    }
}

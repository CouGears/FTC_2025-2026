package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

@TeleOp(name="GamepadTesting", group="Testing")
public class GamepadTesting extends LinearOpMode {

    // Define the different states our OpMode can be in. This makes the code much clearer.

    @Override
    public void runOpMode() {
        GamepadManager GPM = new GamepadManager(gamepad1);
        telemetry.addData(">", "Ready to start.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("-", "PRESS (May need to spam it)");
            telemetry.addData("X", GPM.isPressed(Button.X));
            telemetry.addData("Y", GPM.isPressed(Button.Y));
            telemetry.addData("B", GPM.isPressed(Button.B));
            telemetry.addData("A", GPM.isPressed(Button.A));
            telemetry.addData("L_Bump", GPM.isPressed(Button.L_BUMPER));
            telemetry.addData("L_Trig", GPM.isPressed(Button.L_TRIGGER));
            telemetry.addData("R_Bump", GPM.isPressed(Button.R_BUMPER));
            telemetry.addData("R_Trig", GPM.isPressed(Button.R_TRIGGER));
            telemetry.addData("Up   ", GPM.isPressed(Button.DPAD_UP));
            telemetry.addData("Right", GPM.isPressed(Button.DPAD_RIGHT));
            telemetry.addData("Down ", GPM.isPressed(Button.DPAD_DOWN));
            telemetry.addData("Left ", GPM.isPressed(Button.DPAD_LEFT));
            telemetry.addData("LStick", GPM.isPressed(Button.L_STICKPRESS));
            telemetry.addData("RStick", GPM.isPressed(Button.R_STICKPRESS));
            telemetry.addData("-", "HOLD");
            telemetry.addData("X", GPM.isHeld(Button.X));
            telemetry.addData("Y", GPM.isHeld(Button.Y));
            telemetry.addData("B", GPM.isHeld(Button.B));
            telemetry.addData("A", GPM.isHeld(Button.A));
            telemetry.addData("L_Bump", GPM.isHeld(Button.L_BUMPER));
            telemetry.addData("L_Trig", GPM.isHeld(Button.L_TRIGGER));
            telemetry.addData("R_Bump", GPM.isHeld(Button.R_BUMPER));
            telemetry.addData("R_Trig", GPM.isHeld(Button.R_TRIGGER));
            telemetry.addData("Up   ", GPM.isHeld(Button.DPAD_UP));
            telemetry.addData("Right", GPM.isHeld(Button.DPAD_RIGHT));
            telemetry.addData("Down ", GPM.isHeld(Button.DPAD_DOWN));
            telemetry.addData("Left ", GPM.isHeld(Button.DPAD_LEFT));
            telemetry.addData("LStick", GPM.isHeld(Button.L_STICKPRESS));
            telemetry.addData("RStick", GPM.isHeld(Button.R_STICKPRESS));
            telemetry.update();
            GPM.update();
            sleep(10);
        }
    }
}
package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;


@TeleOp(name="Servo Range Test", group="Testing")
public class ServoRangeTest extends LinearOpMode {

    // Declare the servo
    private Servo servo1;
    private boolean auto;
    private GamepadManager GPM;
    private double currPos = .5;
    private final double step = .001;

    @Override
    public void runOpMode() {
        GPM = new GamepadManager(gamepad1);

        try {
            servo1 = hardwareMap.get(Servo.class, "servo1");
            servo1.setPosition(currPos);
            telemetry.addLine("Servo 'servo1' initialized.");
        } catch (Exception e) {
            telemetry.addLine("ERROR: Could not find servo 'servo1'.");
            telemetry.update();
            sleep(2000);
            return; // Exit
        }

        servo1.setPosition(currPos);

        telemetry.addLine("Servo initialized. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (GPM.isPressed(Button.Y))
                auto = !auto;

            if (auto) {
                telemetry.addData("MODE", "AUTO SWEEP");
                telemetry.addLine("Press Y to switch to Manual mode.");
                currPos += .005;

                if (currPos >= 1)
                    currPos = 0;
                sleep(20);
            }
            else {
                telemetry.addData("MODE", "MANUAL");
                telemetry.addLine("Triggers: Large Control | Bumpers: Fine Control");
                if (GPM.isHeld(Button.R_TRIGGER)) { // Use a threshold for analog triggers
                    currPos += step * 5;
                } else if (GPM.isHeld(Button.L_TRIGGER)) {
                    currPos -= step * 5;
                }

                if (GPM.isHeld(Button.R_BUMPER)) {
                    currPos += step;
                } else if (GPM.isHeld(Button.L_BUMPER)) {
                    currPos -= step;
                }
            }

            currPos = Range.clip(currPos, 0.0, 1.0);
            servo1.setPosition(currPos);

            telemetry.addData("Servo Position", "%.3f", currPos);
            telemetry.update();
            sleep(10);
            GPM.update();
        }
    }
}

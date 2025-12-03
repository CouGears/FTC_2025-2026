package org.firstinspires.ftc.teamcode.cougears.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name="MotorTest", group="Testing")
public class MotorTest extends LinearOpMode {
    enum State {
        SELECT_MOTOR,
        CONTROL_MOTOR
    }

    private State state = State.SELECT_MOTOR;

    private final ArrayList<String> motorNames = new ArrayList<>();
    private final ArrayList<DcMotorEx> motors = new ArrayList<>();

    private int selectedIndex = 0;

    @Override
    public void runOpMode() {
        GamepadManager GPM = new GamepadManager(gamepad1);
        // ---- Scan for all DcMotorEx motors ----
        for (String name : hardwareMap.getAllNames(DcMotorEx.class)) {
            try {
                DcMotorEx m = hardwareMap.get(DcMotorEx.class, name);
                motors.add(m);
                motorNames.add(name);
            } catch (Exception ignored) {
            }
        }

        telemetry.addData("Motors Found", motorNames.size());
        for (String s : motorNames) telemetry.addLine(" - " + s);
        telemetry.addLine("Press START when ready.");
        telemetry.update();

        waitForStart();

        if (motors.isEmpty()) {
            telemetry.addData("ERROR", "No motors found!");
            telemetry.update();
            sleep(3000);
            return;
        }

        while (opModeIsActive()) {
            if (state == State.SELECT_MOTOR) {
                telemetry.addLine("=== SELECT A MOTOR ===");
                telemetry.addLine("Use D-Pad Up/Down to scroll");
                telemetry.addLine("Press A to select");
                telemetry.addLine("Press B to turn off this Motor");
                telemetry.addLine("Press X to turn off all Motors");
                telemetry.addLine("Selected Index: " + selectedIndex % motors.size());

                // scroll down
                if (GPM.isPressed(Button.DPAD_DOWN) || GPM.isPressed(Button.DPAD_RIGHT)) {
                    selectedIndex = Math.abs((selectedIndex + 1) % motors.size());
                }
                // scroll up
                else if (GPM.isPressed(Button.DPAD_UP) || GPM.isPressed(Button.DPAD_LEFT)) {
                    selectedIndex = Math.abs((selectedIndex - 1) % motors.size());
                }

                // Print motor name
                for (int i = 0; i < motorNames.size(); i++) {
                    if (i == selectedIndex) {
                        telemetry.addData(">", motorNames.get(i));
                    } else {
                        telemetry.addData(" ", motorNames.get(i));
                    }
                }

                //Allow control
                if (GPM.isPressed(Button.A)) {
                    state = State.CONTROL_MOTOR;
                }
                if (GPM.isPressed(Button.B)) {
                    motors.get(selectedIndex).setPower(0);
                }
                if (GPM.isPressed(Button.X)) {
                    for(DcMotorEx motor : motors)
                        motor.setPower(0);
                }

            } else if (state == State.CONTROL_MOTOR) {
                DcMotorEx selectedMotor = motors.get(selectedIndex);

                telemetry.addLine("=== MOTOR CONTROL ===");
                telemetry.addData("Motor", motorNames.get(selectedIndex));
                telemetry.addData("Power", "%.2f", selectedMotor.getPower());
                telemetry.addData("Velocity", "%.2f", selectedMotor.getVelocity());
                telemetry.addData("Note:", "Only use velocity if there is an encoder");
                telemetry.addData("Direction", "%s", selectedMotor.getDirection().toString());
                telemetry.addLine("D-Pad Up: Increase Power");
                telemetry.addLine("D-Pad Down: Decrease Power");
                telemetry.addLine("Y: Change Dir");
                telemetry.addLine("B: Return to selection");

                double currPower = selectedMotor.getPower();
                if (GPM.isPressed(Button.DPAD_UP)) currPower += .1;
                if (GPM.isPressed(Button.DPAD_DOWN)) currPower -= .1;
                selectedMotor.setPower(currPower);

                if (GPM.isPressed(Button.Y)) {
                    DcMotorSimple.Direction currDir = selectedMotor.getDirection();
                    if (currDir == DcMotorSimple.Direction.FORWARD)
                        selectedMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                    else
                        selectedMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                }

                if (GPM.isPressed(Button.B)) {
                    state = State.SELECT_MOTOR;
                }

            }
            GPM.update();
            telemetry.update();
            sleep(40);
            // stop motors on exit
        }
        for (DcMotorEx m : motors) m.setPower(0);
    }
}

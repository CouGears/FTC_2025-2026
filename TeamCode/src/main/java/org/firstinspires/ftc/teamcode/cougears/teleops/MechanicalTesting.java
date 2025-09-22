package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

@TeleOp(name="MechanicalTesting", group="Testing")
public class MechanicalTesting extends LinearOpMode {

    // Define the different states our OpMode can be in. This makes the code much clearer.
    enum SetupState {
        MOTOR1_DIR,
        MOTOR2_DIR,
        RUNNING
    }

    // Start in the first setup state.
    private SetupState currentState = SetupState.MOTOR1_DIR;

    DcMotor m1 = null;
    DcMotor m2 = null;

    @Override
    public void runOpMode() {
        GamepadManager GPM = new GamepadManager(gamepad1);
        telemetry.addData("Status", "Initializing...");
        try {
            m1 = hardwareMap.get(DcMotor.class, "motor1");
            m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            telemetry.addData("Motor 1", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Motor 1", "NOT FOUND");
        }

        try {
            m2 = hardwareMap.get(DcMotor.class, "motor2");
            m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            telemetry.addData("Motor 2", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Motor 2", "NOT FOUND");
        }

        telemetry.addData(">", "Ready to start.");
        telemetry.update();

        double power = 0.0;

        // If no motors are configured, just end the OpMode.
        if (m1 == null && m2 == null) {
            telemetry.addData("ERROR", "No motors were found. Exiting.");
            telemetry.update();
            sleep(2000);
            return;
        }

        waitForStart();

        while (opModeIsActive()) {

            // We use a switch statement to handle the logic for each state separately.
            switch (currentState) {

                case MOTOR1_DIR:
                    telemetry.addData("CURRENT STATE", "Setup Motor 1 Direction");
                    telemetry.addData("A Button", "Forward");
                    telemetry.addData("B Button", "Reverse");
                    telemetry.addData("X Button", "Go to Next Step");

                    if (m1 != null) {
                        if (GPM.isPressed(GamepadManager.Button.A)) {
                            m1.setDirection(DcMotorSimple.Direction.FORWARD);
                        }
                        if (GPM.isPressed(GamepadManager.Button.B)) {
                            m1.setDirection(DcMotorSimple.Direction.REVERSE);
                        }
                    }

                    // When X is pressed, transition to the next state.
                    if (GPM.isPressed(GamepadManager.Button.X)) {
                        currentState = SetupState.MOTOR2_DIR;
                    }
                    break;

                case MOTOR2_DIR:
                    telemetry.addData("CURRENT STATE", "Setup Motor 2 Direction");
                    telemetry.addData("A Button", "Forward");
                    telemetry.addData("B Button", "Reverse");
                    telemetry.addData("X Button", "Confirm and Start Running");

                    if (m2 != null) {
                        if (GPM.isPressed(GamepadManager.Button.A)) {
                            m2.setDirection(DcMotorSimple.Direction.FORWARD);
                        }
                        if (GPM.isPressed(GamepadManager.Button.B)) {
                            m2.setDirection(DcMotorSimple.Direction.REVERSE);
                        }
                    }

                    // When X is pressed again, transition to the final running state.
                    if (GPM.isPressed(GamepadManager.Button.X)) {
                        currentState = SetupState.RUNNING;
                    }
                    break;

                case RUNNING:
                    telemetry.addData("CURRENT STATE", "Running Motors");
                    telemetry.addData("Y Button", "Increase Power");
                    telemetry.addData("X Button", "Decrease Power");
                    telemetry.addData("Current Power", power);

                    // Now Y and X have a different, clear purpose.
                    if (GPM.isPressed(GamepadManager.Button.Y)) {
                        power += 0.2;
                    }
                    if (GPM.isPressed(GamepadManager.Button.X)) {
                        power -= 0.2;
                    }

                    // Use Range.clip to safely keep the power between -1.0 and 1.0.
                    power = Range.clip(power, -1.0, 1.0);

                    if (m1 != null) m1.setPower(power);
                    if (m2 != null) m2.setPower(power);
                    break;
            }

            telemetry.update();
            sleep(10);
        }
    }
}
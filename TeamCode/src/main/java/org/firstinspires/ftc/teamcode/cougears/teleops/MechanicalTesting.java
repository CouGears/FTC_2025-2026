package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

@TeleOp(name="MechanicalTesting", group="Testing")
public class MechanicalTesting extends LinearOpMode {
    boolean aIsPressed = false;
    boolean bIsPressed = false;
    boolean xIsPressed = false;
    boolean yIsPressed = false;
    boolean motor1init = false;
    boolean motor2init = false;

    DcMotor m1 = null;
    DcMotor m2 = null;

    @Override
    public void runOpMode() {
        // Initialize motors

        telemetry.addData("Status", "Initialized");
        try{
            m1 = hardwareMap.get(DcMotor.class, "motor1");
            m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor1init = true;
        } catch (Exception e) {
            motor1init = false;
        }

        try{
            m2 = hardwareMap.get(DcMotor.class, "motor2");
            m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor2init = true;
        } catch (Exception e) {
            motor2init = false;
        }

        telemetry.update();
        double power = 0.0;

        waitForStart();

        while (opModeIsActive()) {
            while (!xIsPressed && gamepad1.x && m1 != null) {
                telemetry.addData(">", "Set Directions for M1 (Press X to move to next step)");
                telemetry.addData(">", "M1: A=fwd, B=bck ");
                if (!aIsPressed && gamepad1.a)
                    m1.setDirection(DcMotorSimple.Direction.FORWARD);
                if (!bIsPressed && gamepad1.b)
                    m1.setDirection(DcMotorSimple.Direction.REVERSE);
                telemetry.update();
                xIsPressed = gamepad1.x;
                aIsPressed = gamepad1.a;
                bIsPressed = gamepad1.b;
            }

            while (!xIsPressed && gamepad1.x && m2 != null) {
                telemetry.addData(">", "Set Directions for M2 (Press X to move to next step)");
                telemetry.addData(">", "M1: A=fwd, B=bck ");
                if (!aIsPressed && gamepad1.a)
                    m2.setDirection(DcMotorSimple.Direction.FORWARD);
                if (!bIsPressed && gamepad1.b)
                    m2.setDirection(DcMotorSimple.Direction.REVERSE);
                telemetry.update();
                xIsPressed = gamepad1.x;
                aIsPressed = gamepad1.a;
                bIsPressed = gamepad1.b;
            }

            if (m1 != null || m2 != null) {
                if (!yIsPressed && gamepad1.y && power != 1.0)
                    power += .2;
                if (!xIsPressed && gamepad1.x && power != -1.0)
                    power -= .2;

                if (m1 != null)
                    m1.setPower(power);
                if (m2 != null)
                    m2.setPower(power);
            } else {
                telemetry.addData("ERROR", "BOTH MOTORS FAILED");
            }

            xIsPressed = gamepad1.x;
            yIsPressed = gamepad1.y;
            telemetry.update();
            sleep(10);
        }
    }
}
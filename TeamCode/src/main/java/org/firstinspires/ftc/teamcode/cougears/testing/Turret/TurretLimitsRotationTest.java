package org.firstinspires.ftc.teamcode.cougears.testing.Turret;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;


@TeleOp(name="TurretLimitsRotationTest", group="Testing")
public class TurretLimitsRotationTest extends LinearOpMode {

    // Define the different states our OpMode can be in. This makes the code much clearer.

    @Override
    public void runOpMode() {
        DcMotorEx turret = hardwareMap.get(DcMotorEx.class, "TurretRotator");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(.5); // Inc to speed it up
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData(">", "Ready to start.");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            turret.setTargetPosition(Turret_turretPos[0]);
            telemetry.addData(">", "turretPos[0] = %d", Turret_turretPos[0]);
            telemetry.update();
            sleep(3000);

            turret.setTargetPosition(Turret_turretPos[1]);
            telemetry.addData(">", "turretPos[1] = %d", Turret_turretPos[1]);
            telemetry.update();
            sleep(3000);

            turret.setTargetPosition(Turret_turretPos[2]);
            telemetry.addData(">", "turretPos[2] = %d", Turret_turretPos[2]);
            telemetry.update();
            sleep(3000);
        }
    }
}
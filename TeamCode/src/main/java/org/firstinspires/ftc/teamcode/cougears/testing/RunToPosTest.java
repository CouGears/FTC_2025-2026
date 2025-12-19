package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

@TeleOp(name="RunToPosTest", group="Testing")

public class RunToPosTest extends LinearOpMode {


    @Override
    public void runOpMode(){
        DcMotorEx turret = hardwareMap.get(DcMotorEx.class, "TurretRotator");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setTargetPosition(0);
        turret.setPower(1);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        GamepadManager GPM = new GamepadManager(gamepad1);
        int targetPos = 0;
        telemetry.addLine("Ready");
        waitForStart();

        while(opModeIsActive()){
            turret.setTargetPosition(targetPos);
            if (GPM.isPressed(Button.L_TRIGGER))
                targetPos += Turret_turretStep;
            if (GPM.isPressed(Button.R_TRIGGER))
                targetPos -= Turret_turretStep;
            telemetry.addLine("TargetPos = " + targetPos);
            telemetry.update();
            GPM.update();
        }
    }
}

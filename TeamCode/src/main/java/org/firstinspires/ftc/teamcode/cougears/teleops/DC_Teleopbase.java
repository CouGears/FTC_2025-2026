package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

// First line after runOpMode should be:
// DC_Teleopbase bot = new DC_Teleopbase(hardwareMap, telemetry, gamepad1, gamepad2);
public class DC_Teleopbase extends BotBase {

    DcMotor FW_L, FW_R, intake;

    public DC_Teleopbase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            FW_L = HM.get(DcMotor.class, "FW_L");
            FW_R = HM.get(DcMotor.class, "FW_R");
            intake = HM.get(DcMotor.class, "intake");

            FW_L.setDirection(DcMotorSimple.Direction.REVERSE);
            FW_R.setDirection(DcMotorSimple.Direction.FORWARD);
            intake.setDirection(DcMotorSimple.Direction.FORWARD);

            FW_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            return true;
        } catch (Exception e) {
            tele.addData("ERROR", e);
            return false;
        }
    }

    //****** FLYWHEELS ******
    public void spinUp() {
        FW_L.setPower(1);
        FW_R.setPower(1);
    }

    public void spinUp(double p) {
        FW_L.setPower(p);
        FW_R.setPower(p);
    }

    public void spinDown() {
        FW_L.setPower(0);
        FW_R.setPower(0);
    }

    //****** INATKE ******
    public void intakeOn() {
        intake.setPower(1);
    }

    public void intakeOn(double p) {
        intake.setPower(p);
    }

    public void intakeOff() {
        intake.setPower(0);
    }
}

package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

// First line after runOpMode should be:
// OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
public class OctoberCompTeleOpBase extends BotBase {

    DcMotor FW_L, FW_R;
    Servo lever;

    public OctoberCompTeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            FW_L = HM.get(DcMotor.class, "FW_L");
            FW_R = HM.get(DcMotor.class, "FW_R");
            lever = HM.get(Servo.class, "lever");


            FW_L.setDirection(DcMotorSimple.Direction.REVERSE);
            FW_R.setDirection(DcMotorSimple.Direction.FORWARD);


            FW_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


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
    //****** SERVOS ******
    public void pushBall() {
        lever.setPosition(1);
    }
    public void servoReset() {
        lever.setPosition(.5);
    }

    public void endTeleOp(){
        super.endTeleOp();
        FW_L.setPower(0);
        FW_R.setPower(0);
    }
}

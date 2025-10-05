package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

// First line after runOpMode should be:
// OctoberCompTeleOpBase bot = new OctoberCompTeleOpBase(hardwareMap, telemetry, gamepad1, gamepad2);
public class OctoberCompTeleOpBase extends BotBase {

    DcMotor FW_L, FW_R;
    Servo BServo, FServo;

    public OctoberCompTeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            FW_L = HM.get(DcMotor.class, "FW_L");
            FW_R = HM.get(DcMotor.class, "FW_R");
            BServo = HM.get(Servo.class, "BServo");
            FServo = HM.get(Servo.class, "FServo");

            FW_L.setDirection(DcMotorSimple.Direction.REVERSE);
            FW_R.setDirection(DcMotorSimple.Direction.FORWARD);


            FW_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }

        BServo.setPosition(BServoPos[0]);
        FServo.setPosition(FServoPos[0]);
        return true;
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
    public void FServoPush()  { FServo.setPosition(FServoPos[1]); }
    public void FServoReset() { FServo.setPosition(FServoPos[0]); }
    public void BServoPush()  { FServo.setPosition(BServoPos[1]); }
    public void BServoReset() { FServo.setPosition(BServoPos[0]); }

    public void endTeleOp(){
        super.endTeleOp();
        FW_L.setPower(0);
        FW_R.setPower(0);
    }
}

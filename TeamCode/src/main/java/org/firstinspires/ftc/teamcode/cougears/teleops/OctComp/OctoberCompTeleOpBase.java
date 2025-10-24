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

    private DcMotor FW;
    private Servo GateServo;
    public boolean FWSpinning, GateServoUp;

    public OctoberCompTeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            GateServo = HM.get(Servo.class, "GateServo");

            FW = HM.get(DcMotor.class, "FW");
            FW.setDirection(DcMotorSimple.Direction.REVERSE);
            FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }

        GateServo.setPosition(GateServoPos[0]);
        return true;
    }

    //****** FLYWHEELS ******
    public void spinUp() { FW.setPower(1); }
    public void spinDown() { FW.setPower(0); }

    //****** SERVOS ******
    public void GateServoPush()  { GateServo.setPosition(GateServoPos[1]); }
    public void GateServoReset() { GateServo.setPosition(GateServoPos[0]); }


    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
    }
}

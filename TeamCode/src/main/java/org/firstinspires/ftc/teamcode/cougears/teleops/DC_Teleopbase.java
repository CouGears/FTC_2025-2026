package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

public class DC_Teleopbase extends BotBase {
    DcMotor FW_L, FW_R, intake;
    public DC_Teleopbase(HardwareMap HardwareMap, Telemetry Telemetry) {
        super(HardwareMap, Telemetry);
    }
    public boolean botInit() {
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
    public void spinUp(){
        double power = 1;
        FW_L.setPower(power);
        FW_R.setPower(power);
    }
    public void spinUp(double p){
        double power = p;
        FW_L.setPower(power);
        FW_R.setPower(power);
    }
    public void intakeOn(){
        double power = 1;
        intake.setPower(power);
    }
    public void intakeOn(double p){
        double power = p;
        intake.setPower(power);
    }
    public void spinDown(){
        FW_L.setPower(0);
        FW_R.setPower(0);
    }
    public void intakeOff(){
        intake.setPower(0);
    }
}

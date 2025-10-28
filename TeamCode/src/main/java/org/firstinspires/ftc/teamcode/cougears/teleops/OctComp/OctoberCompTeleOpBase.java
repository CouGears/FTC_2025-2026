package org.firstinspires.ftc.teamcode.cougears.teleops.OctComp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

    public DcMotorEx FW;
    private Servo GateServo;
    public boolean FWSpinning, GateServoUp;

    public OctoberCompTeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, gamepad1, gamepad2);
    }

    public boolean botInit() {
        super.botInit();
        try {
            GateServo = HM.get(Servo.class, "GateServo");

            FW = HM.get(DcMotorEx.class, "FW");
            FW.setDirection(DcMotor.Direction.REVERSE);
            FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FW.setVelocityPIDFCoefficients(5.0, 0, 0, 6.16);
        } catch (Exception e) {
            tele.addData("ERROR", "COULD NOT INIT");
            tele.addData("ERROR MSG:", e);
            return false;
        }

        GateServo.setPosition(GateServoPos[0]);
        return true;
    }

    //****** FLYWHEELS ******
    public void spinUp() {
//        FW.setPower(.7);
        FW.setVelocity(shootVel);
    }
    public void spinDown() {
        FW.setPower(0);
        tele.addData("Flywheel", "NOT RUNNING at power %.2f", FW.getPower());
    }

    //****** SERVOS ******
    public void GateServoPush()  {
        GateServo.setPosition(GateServoPos[1]);
        tele.addData("GateServo", "at pos %.2f", GateServo.getPosition());
    }
    public void GateServoReset() {
        GateServo.setPosition(GateServoPos[0]);
        tele.addData("GateServo", "at pos %.2f", GateServo.getPosition());
    }


    public void endTeleOp(){
        super.endTeleOp();
        FW.setPower(0);
    }
    public void RafiDrive(Gamepad gamepad1) {
        tele.addData(">", "RUNNING RAFI DRIVE");
        double forward =  gamepad1.right_stick_y;
        double strafe  =  gamepad1.right_stick_x;
        double turn    =  gamepad1.left_stick_x;

        // Mecanum drive calculations for a LEFT-side motor reversal configuration.
        // These formulas are different from the standard right-side reversal.
        double frontLeftPower  = forward - strafe - turn;
        double frontRightPower = forward + strafe + turn;
        double backLeftPower   = forward + strafe - turn;
        double backRightPower  = forward - strafe + turn;

        // Normalize the motor powers to ensure no value exceeds 1.0
        double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower  /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower   /= maxPower;
            backRightPower  /= maxPower;
        }

        // Set the power for each motor
        motorFL.setPower(frontLeftPower);
        motorFR.setPower(frontRightPower);
        motorBL.setPower(backLeftPower);
        motorBR.setPower(backRightPower);

        // Optional: Add telemetry to see motor powers on the Driver Hub
        // tele.addData("Drive Motors", "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
        //         frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }
    public void SlowRafiDrive (Gamepad gamepad1){
        tele.addData(">", "RUNNING SLOW RAFI DRIVE");
        double forward =  gamepad1.right_stick_y/4;
        double strafe  =  gamepad1.right_stick_x/4;
        double turn    =  gamepad1.left_stick_x/4;

        // Mecanum drive calculations for a LEFT-side motor reversal configuration.
        // These formulas are different from the standard right-side reversal.
        double frontLeftPower  = forward - strafe - turn;
        double frontRightPower = forward + strafe + turn;
        double backLeftPower   = forward + strafe - turn;
        double backRightPower  = forward - strafe + turn;

        // Normalize the motor powers to ensure no value exceeds 1.0
        double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower  /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower   /= maxPower;
            backRightPower  /= maxPower;
        }

        // Set the power for each motor
        motorFL.setPower(frontLeftPower);
        motorFR.setPower(frontRightPower);
        motorBL.setPower(backLeftPower);
        motorBR.setPower(backRightPower);

        // Optional: Add telemetry to see motor powers on the Driver Hub
        // tele.addData("Drive Motors", "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
        //         frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }
}


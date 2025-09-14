package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/*
    ***NOTICE*** Untested as of Sep 10.
 */
public class BotBase {
    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;
    // Constants for motor speed
    private static final double MAX_SPEED = 1.0;
    private static final double MIN_SPEED = -1.0;

    private final HardwareMap HM;
    private final Telemetry tele;

    public BotBase (HardwareMap HardwareMap, Telemetry Telemetry) {
        HM = HardwareMap;
        tele = Telemetry;
    }

    public boolean botInit() {
        try {
            motorFL = HM.get(DcMotor.class, "motorFL");
            motorFR = HM.get(DcMotor.class, "motorFR");
            motorBL = HM.get(DcMotor.class, "motorBL");
            motorBR = HM.get(DcMotor.class, "motorBR");

            motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
            motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
            motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
            motorBR.setDirection(DcMotorSimple.Direction.FORWARD);

            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            return true;
        } catch (Exception e) {
            tele.addData("ERROR", e);
            return false;
        }
    }

    public void botDrive(Gamepad gamepad1){
        // Drive controls
        double drive = gamepad1.left_stick_y; // Forward/back strafe on left stick Y
        double strafe = gamepad1.left_stick_x; // Left/right drive on left stick X
        double rotate = gamepad1.right_stick_x; // Rotation on right stick X

        // Calculate drive motor powers for strafe-forward configuration
        double frontLeftPower = strafe + drive + rotate;
        double frontRightPower = strafe - drive - rotate;
        double backLeftPower = strafe - drive + rotate;
        double backRightPower = strafe + drive - rotate;

        // Normalize drive motor powers
        double maxPower = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        motorFL.setPower(Range.clip(frontLeftPower, MIN_SPEED, MAX_SPEED));
        motorFR.setPower(Range.clip(frontRightPower, MIN_SPEED, MAX_SPEED));
        motorBL.setPower(Range.clip(backLeftPower, MIN_SPEED, MAX_SPEED));
        motorBR.setPower(Range.clip(backRightPower, MIN_SPEED, MAX_SPEED));
        tele.addData("Drive Motors", "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public void endTeleOp(){
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }
}

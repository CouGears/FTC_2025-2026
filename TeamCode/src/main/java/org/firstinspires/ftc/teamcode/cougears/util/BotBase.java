package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

import java.util.HashMap;


/*
 ***NOTICE*** Untested as of Sep 10.
 */
public class BotBase {
    public DcMotor motorFL;
    public DcMotor motorFR;
    public DcMotor motorBL;
    public DcMotor motorBR;
    // Constants for motor speed
    private static final double MAX_SPEED = 1.0;
    private static final double MIN_SPEED = -1.0;

    public final HardwareMap HM;
    public final Telemetry tele;

    public final GamepadManager GPM_1, GPM_2;

    public HashMap<String, ElapsedTime> timers;

    public BotBase (HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        HM = HardwareMap;
        tele = Telemetry;
        GPM_1 = new GamepadManager(gamepad1);
        GPM_2 = new GamepadManager(gamepad2);
    }

    // ****** MOTORS AND DRIVING ******
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

    public void drive(Gamepad gamepad1){
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


    public void manualMove (double drive, double strafe, double rotate) {
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
    }

    // ****** GPM ******
    public boolean isPressed (int controllerNum, GamepadManager.Button b){
        if (controllerNum == 2)
            return GPM_2.isPressed(b);
        else // We are proactivily assuming if you dont mean controller 2, you mean controller 1 in all situations
            return GPM_1.isPressed(b);
    }

    public boolean isHeld (int controllerNum, GamepadManager.Button b){
        if (controllerNum == 2)
            return GPM_2.isHeld(b);
        else // We are proactivily assuming if you dont mean controller 2, you mean controller 1 in all situations
            return GPM_1.isHeld(b);
    }


    // ****** TIMERS ******
    // Only using try-catch if there is ever a problem later -E
    public boolean createTimer(String title){
        try {
            timers.put(title, new ElapsedTime());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Need to check if timer exists or nullptr err -E
    public boolean resetTime(String title){
        if (timers.get(title) != null) {
            timers.get(title).reset();
            return true;
        }
        else
            return false;
    }

    public boolean timerExpired_Seconds(String title, double seconds){
        ElapsedTime timer = timers.get(title);
        return timer != null && timer.seconds() >= seconds; // fancy ik :) -E
    }
    public boolean timerExpired_MSeconds(String title, double mseconds){
        ElapsedTime timer = timers.get(title);
        return timer != null && timer.milliseconds() >= mseconds;
    }

    // ****** MISC ******

    // Anything that needs to be done every "tick"
    // Should always be called as last line in "whileOpModeisActive()" loop
    public void update(){
        GPM_1.update();
        GPM_2.update();
        tele.update();
    }

    // Should always be called as first line after "whileOpModeisActive()" loop
    public void endTeleOp(){
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }
}

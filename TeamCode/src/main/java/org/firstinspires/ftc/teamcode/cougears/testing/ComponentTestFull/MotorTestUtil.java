package org.firstinspires.ftc.teamcode.cougears.testing.ComponentTestFull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MotorTestUtil {
    public  ArrayList<String> motorNames = new ArrayList<>();
    public  ArrayList<DcMotorEx> motors = new ArrayList<>();

    public String loadMotors(HardwareMap HM){
        for (String name : HM.getAllNames(DcMotorEx.class)) {
            try {
                DcMotorEx m = HM.get(DcMotorEx.class, name);
                motors.add(m);
                motorNames.add(name);
            } catch (Exception e) {
                return "Error: " + e;
            }
        }
        return "Motors Loaded";
    }
    public void selectMotorControl(int selectedIndex, GamepadManager GPM){
        if (GPM.isPressed(GamepadManager.Button.B)) {
            motors.get(selectedIndex).setPower(0);
        }
        if (GPM.isPressed(GamepadManager.Button.X)) {
            for(DcMotorEx motor : motors)
                motor.setPower(0);
        }
    }

    public void controlMotor(int selectedIndex, GamepadManager GPM){
        DcMotorEx selectedMotor = motors.get(selectedIndex);

        double currPower = selectedMotor.getPower();
        if (GPM.isPressed(GamepadManager.Button.R_TRIGGER)) currPower += .1;
        if (GPM.isPressed(GamepadManager.Button.L_TRIGGER)) currPower -= .1;
        if (GPM.isPressed(GamepadManager.Button.R_BUMPER)) currPower += .05;
        if (GPM.isPressed(GamepadManager.Button.L_BUMPER)) currPower -= .05;
        selectedMotor.setPower(currPower);

        if (GPM.isPressed(GamepadManager.Button.Y)) {
            DcMotorSimple.Direction currDir = selectedMotor.getDirection();
            if (currDir == DcMotorSimple.Direction.FORWARD)
                selectedMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            else
                selectedMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }
}

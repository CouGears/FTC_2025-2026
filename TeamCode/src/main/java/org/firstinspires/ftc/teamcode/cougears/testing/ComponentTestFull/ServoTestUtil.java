package org.firstinspires.ftc.teamcode.cougears.testing.ComponentTestFull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

import java.util.ArrayList;

public class ServoTestUtil {
    public final ArrayList<String> ServoNames = new ArrayList<>();
    public final ArrayList<Servo> servos = new ArrayList<>();

    public String loadServos(HardwareMap HM){
        for (String name : HM.getAllNames(Servo.class)) {
            try {
                Servo s = HM.get(Servo.class, name);
                servos.add(s);
                ServoNames.add(name);
            } catch (Exception e) {
                return "Error: " + e;
            }
        }
        return "Servos Loaded";
    }

    public void selectServoControl(int selectedIndex, GamepadManager GPM){
        // If we want to do anything in the select servo screen
    }

    public void controlServo(int selectedIndex, GamepadManager GPM){
        Servo selectedServo = servos.get(selectedIndex);

        double currPos = selectedServo.getPosition();
        if (GPM.isPressed(GamepadManager.Button.R_TRIGGER)) currPos += .1;
        if (GPM.isPressed(GamepadManager.Button.L_TRIGGER)) currPos -= .1;
        if (GPM.isPressed(GamepadManager.Button.R_BUMPER)) currPos += .05;
        if (GPM.isPressed(GamepadManager.Button.L_BUMPER)) currPos -= .05;
        selectedServo.setPosition(currPos);
    }
}

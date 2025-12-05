package org.firstinspires.ftc.teamcode.cougears.testing.ComponentTestFull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

import java.util.ArrayList;

public class CRServoUtil {
    public final ArrayList<String> CRServoNames = new ArrayList<>();
    public final ArrayList<CRServo> CRServos = new ArrayList<>();

    public String loadCRServos(HardwareMap HM){
        for (String name : HM.getAllNames(CRServo.class)) {
            try {
                CRServo CRs = HM.get(CRServo.class, name);
                CRServos.add(CRs);
                CRServoNames.add(name);
            } catch (Exception e) {
                return "Error: " + e;
            }
        }
        return "CRServos Loaded";
    }

    public void selectCRServoControl(int selectedIndex, GamepadManager GPM){
        if (GPM.isPressed(GamepadManager.Button.B)) {
            CRServos.get(selectedIndex).setPower(0);
        }
        if (GPM.isPressed(GamepadManager.Button.X)) {
            for(CRServo currCRServo : CRServos)
                currCRServo.setPower(0);
        }
    }

    public void controlCRServo(int selectedIndex, GamepadManager GPM){
        CRServo selectedSCRervo = CRServos.get(selectedIndex);
        double currPower = selectedSCRervo.getPower();
        if (GPM.isPressed(GamepadManager.Button.R_TRIGGER)) currPower += .1;
        if (GPM.isPressed(GamepadManager.Button.L_TRIGGER)) currPower -= .1;
        if (GPM.isPressed(GamepadManager.Button.R_BUMPER)) currPower += .05;
        if (GPM.isPressed(GamepadManager.Button.L_BUMPER)) currPower -= .05;
        selectedSCRervo.setPower(currPower);
    }
}

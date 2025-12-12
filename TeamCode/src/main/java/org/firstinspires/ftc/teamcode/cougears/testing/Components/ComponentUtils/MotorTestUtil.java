package org.firstinspires.ftc.teamcode.cougears.testing.Components.ComponentUtils;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

import java.util.ArrayList;

public class MotorTestUtil {
    public ArrayList<String> motorNames = new ArrayList<>();
    public ArrayList<DcMotorEx> motors = new ArrayList<>();

    private long lastStepTime = 0;
    private static final long STEP_INTERVAL_MS = 300;

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
        if (GPM.isPressed(GamepadManager.Button.B))
            motors.get(selectedIndex).setPower(0);

        if (GPM.isPressed(GamepadManager.Button.X))
            for(DcMotorEx m : motors) m.setPower(0);
    }

    public void controlMotor(int selectedIndex, GamepadManager GPM){
        DcMotorEx m = motors.get(selectedIndex);
        long t = System.currentTimeMillis();

        if (t - lastStepTime >= STEP_INTERVAL_MS) {
            double p = m.getPower();
            boolean act = false;

            if (GPM.isHeld(GamepadManager.Button.R_TRIGGER)) { p += 0.1; act = true; }
            if (GPM.isHeld(GamepadManager.Button.L_TRIGGER)) { p -= 0.1; act = true; }
            if (GPM.isHeld(GamepadManager.Button.R_BUMPER))  { p += 0.05; act = true; }
            if (GPM.isHeld(GamepadManager.Button.L_BUMPER))  { p -= 0.05; act = true; }

            if (act) {
                m.setPower(p);
                lastStepTime = t;
            }
        }

        if (GPM.isPressed(GamepadManager.Button.Y)) {
            m.setDirection(
                    m.getDirection() == DcMotorSimple.Direction.FORWARD ?
                            DcMotorSimple.Direction.REVERSE :
                            DcMotorSimple.Direction.FORWARD
            );
        }
    }
}

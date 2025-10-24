package org.firstinspires.ftc.teamcode.cougears.util;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;


public class GamepadManager {

    public Gamepad linkedGamepad = null;
    public Gamepad lastGamepadState = null;
    private HashMap<Button, ElapsedTime> buttonCooldown;
    private final double cooldownDuration = 250;

    public enum Button {
        A, B, X, Y,
        DPAD_UP, DPAD_RIGHT, DPAD_DOWN, DPAD_LEFT,
        R_TRIGGER, R_BUMPER, R_STICKPRESS,
        L_TRIGGER, L_BUMPER, L_STICKPRESS
    }

    public GamepadManager (Gamepad GP){
        linkedGamepad = GP;
        lastGamepadState = new Gamepad();
        buttonCooldown = new HashMap<>();
        update();
    }

    // MUST be run at every loop of a teleop
    public void update(){
        try {
            lastGamepadState.copy(linkedGamepad);
        } catch (Exception e){
            // _/(0_0)\_ idk just dont do anything. It should work.
        }
    }

    // Take in one of the button enums and return if it is pressed now and wasnt before
    public boolean isPressed(Button b){
        boolean newPress = false;
        switch (b){
            case A: newPress = linkedGamepad.a && !lastGamepadState.a; break;
            case B: newPress = linkedGamepad.b && !lastGamepadState.b; break;
            case X: newPress = linkedGamepad.x && !lastGamepadState.x; break;
            case Y: newPress = linkedGamepad.y && !lastGamepadState.y; break;
            case DPAD_UP: newPress = linkedGamepad.dpad_up && !lastGamepadState.dpad_up; break;
            case DPAD_RIGHT: newPress = linkedGamepad.dpad_right && !lastGamepadState.dpad_right; break;
            case DPAD_DOWN: newPress = linkedGamepad.dpad_down && !lastGamepadState.dpad_down; break;
            case DPAD_LEFT: newPress = linkedGamepad.dpad_left && !lastGamepadState.dpad_left; break;
            case L_BUMPER: newPress = linkedGamepad.left_bumper && !lastGamepadState.left_bumper; break;
            case L_STICKPRESS: newPress = linkedGamepad.left_stick_button && !lastGamepadState.left_stick_button; break;
            case L_TRIGGER: newPress = linkedGamepad.left_trigger > 0 && !(lastGamepadState.left_trigger > 0); break;
            case R_BUMPER: newPress = linkedGamepad.right_bumper && !lastGamepadState.right_bumper; break;
            case R_STICKPRESS: newPress = linkedGamepad.right_stick_button && !lastGamepadState.right_stick_button; break;
            case R_TRIGGER: newPress = linkedGamepad.right_trigger > 0 && !(lastGamepadState.right_trigger > 0); break;
        }
        if (!newPress)
            return false;

        ElapsedTime timer = buttonCooldown.get(b);
        if (timer == null){
            timer = new ElapsedTime();
            buttonCooldown.put(b, timer);
        }

        if (timer.milliseconds() > cooldownDuration){
            timer.reset();
            return true;
        } else {
            return false;
        }
    }

    public boolean isHeld(Button b){
        switch (b){
            case A: return linkedGamepad.a;
            case B: return linkedGamepad.b;
            case X: return linkedGamepad.x;
            case Y: return linkedGamepad.y;
            case DPAD_UP: return linkedGamepad.dpad_up;
            case DPAD_RIGHT: return linkedGamepad.dpad_right;
            case DPAD_DOWN: return linkedGamepad.dpad_down;
            case DPAD_LEFT: return linkedGamepad.dpad_left;
            case L_BUMPER: return linkedGamepad.left_bumper;
            case L_STICKPRESS: return linkedGamepad.left_stick_button;
            case L_TRIGGER: return linkedGamepad.left_trigger > 0;
            case R_BUMPER: return linkedGamepad.right_bumper;
            case R_STICKPRESS: return linkedGamepad.right_stick_button;
            case R_TRIGGER: return linkedGamepad.right_trigger > 0;
        }
        return false;
    }
}

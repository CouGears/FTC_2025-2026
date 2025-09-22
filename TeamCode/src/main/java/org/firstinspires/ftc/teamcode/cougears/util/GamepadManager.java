package org.firstinspires.ftc.teamcode.cougears.util;
import com.qualcomm.robotcore.hardware.Gamepad;


public class GamepadManager {

    public Gamepad linkedGamepad = null;
    public Gamepad lastGamepadState = null;

    public enum Button {
        A, B, X, Y,
        DPAD_UP, DPAD_RIGHT, DPAD_DOWN, DPAD_LEFT,
        R_TRIGGER, R_BUMPER, R_STICKPRESS,
        L_TRIGGER, L_BUMPER, L_STICKPRESS
    }

    public GamepadManager (Gamepad GP){
        linkedGamepad = GP;
        lastGamepadState = new Gamepad();
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
        switch (b){
            case A: return linkedGamepad.a && !lastGamepadState.a;
            case B: return linkedGamepad.b && !lastGamepadState.b;
            case X: return linkedGamepad.x && !lastGamepadState.x;
            case Y: return linkedGamepad.y && !lastGamepadState.y;
            case DPAD_UP: return linkedGamepad.dpad_up && !lastGamepadState.dpad_up;
            case DPAD_RIGHT: return linkedGamepad.dpad_right && !lastGamepadState.dpad_right;
            case DPAD_DOWN: return linkedGamepad.dpad_down && !lastGamepadState.dpad_down;
            case DPAD_LEFT: return linkedGamepad.dpad_left && !lastGamepadState.dpad_left;
            case L_BUMPER: return linkedGamepad.left_bumper && !lastGamepadState.left_bumper;
            case L_STICKPRESS: return linkedGamepad.left_stick_button && !lastGamepadState.left_stick_button;
            case L_TRIGGER: return linkedGamepad.left_trigger > 0 && !(lastGamepadState.left_trigger > 0);
            case R_BUMPER: return linkedGamepad.right_bumper && !lastGamepadState.right_bumper;
            case R_STICKPRESS: return linkedGamepad.right_stick_button && !lastGamepadState.right_stick_button;
            case R_TRIGGER: return linkedGamepad.right_trigger > 0 && !(lastGamepadState.right_trigger > 0);
        }
        return false;
    }
}

package org.firstinspires.ftc.teamcode.cougears.testing.ComponentTestFull;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;

import java.util.ArrayList;


@TeleOp(name="MotorTest", group="Testing")
public class ComponentTest extends LinearOpMode {
    enum State {
        SELECT_MOTOR,
        CONTROL_MOTOR,
        SELECT_SERVO,
        CONTROL_SERVO,
        SELECT_CRSERVO,
        CONTROL_CRSERVO
    }

    private State state = State.SELECT_MOTOR;

    private int selectedIndex = 0;
    GamepadManager GPM = null;
    MotorTestUtil MTU = new MotorTestUtil();
    ServoTestUtil STU = new ServoTestUtil();
    CRServoUtil CRSTU = new CRServoUtil();

    @Override
    public void runOpMode() {
        GPM = new GamepadManager(gamepad1);
        // ---- Scan for all DcMotorEx motors ----
        telemetry.addLine(MTU.loadMotors(hardwareMap));
        telemetry.addLine(STU.loadServos(hardwareMap));
        telemetry.addLine(CRSTU.loadCRServos(hardwareMap));

        telemetry.addData("Motors Found", MTU.motorNames.size());
        for (String s : MTU.motorNames) telemetry.addLine(" - " + s);

        telemetry.addData("Servos Found", STU.ServoNames.size());
        for (String s : STU.ServoNames) telemetry.addLine(" - " + s);

        telemetry.addData("CRServos Found", CRSTU.CRServoNames.size());
        for (String s : CRSTU.CRServoNames) telemetry.addLine(" - " + s);

        telemetry.addLine("Press START when ready.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            try {
                GUIControl(); //L&R Dpad, A and B
                printStateData(telemetry);
                switch (state) {
                    case SELECT_MOTOR:
                        MTU.selectMotorControl(selectedIndex, GPM);
                        break;
                    case CONTROL_MOTOR:
                        MTU.controlMotor(selectedIndex, GPM);
                        break;
                    case SELECT_SERVO:
                        STU.selectServoControl(selectedIndex, GPM);
                        break;
                    case CONTROL_SERVO:
                        STU.controlServo(selectedIndex, GPM);
                        break;
                    case SELECT_CRSERVO:
                        CRSTU.selectCRServoControl(selectedIndex, GPM);
                        break;
                    case CONTROL_CRSERVO:
                        CRSTU.controlCRServo(selectedIndex, GPM);
                        break;
                }
            } catch (Exception e) {
                telemetry.addData("ERROR", "Problem displaying tab %s", state);
                telemetry.addData("MSG", e);
            }
            GPM.update();
            telemetry.update();
            sleep(10);
        }
        for (DcMotorEx m : MTU.motors) m.setPower(0);
        for (CRServo CRs : CRSTU.CRServos) CRs.setPower(0);
    }



    public void printStateData(Telemetry telemetry){
        // print controls for each tab
        switch (state){
            case SELECT_MOTOR:
                telemetry.addLine("=== SELECT A MOTOR ===");
                telemetry.addLine("Use D-Pad Up/Down to scroll");
                telemetry.addLine("Press A to select");
                telemetry.addLine("Press B to turn off this Motor");
                telemetry.addLine("Press X to turn off all Motors");
                telemetry.addLine("Selected Index: " + selectedIndex);
                listComponents();
                break;
            case CONTROL_MOTOR:
                DcMotorEx selectedMotor = MTU.motors.get(selectedIndex);
                telemetry.addLine("=== MOTOR CONTROL ===");
                telemetry.addData("Motor", MTU.motorNames.get(selectedIndex));
                telemetry.addData("Power", "%.2f", selectedMotor.getPower());
                telemetry.addData("Velocity", "%.2f", selectedMotor.getVelocity());
                telemetry.addData("Note:", "Only use velocity if there is an encoder");
                telemetry.addData("Direction", "%s", selectedMotor.getDirection().toString());
                telemetry.addLine("R_Trigger: + Fast");
                telemetry.addLine("L_Trigger: - Fast");
                telemetry.addLine("R_Bumper: + Slow");
                telemetry.addLine("L_Bumper: - Slow");
                telemetry.addLine("Y: Change Dir");
                telemetry.addLine("B: Return to selection");
                break;
            case SELECT_SERVO:
                telemetry.addLine("=== SELECT A SERVO ===");
                telemetry.addLine("Use D-Pad Up/Down to scroll");
                telemetry.addLine("Press A to select");
                telemetry.addLine("Selected Index: " + selectedIndex);
                listComponents();
                break;
            case CONTROL_SERVO:
                Servo selectedServo = STU.servos.get(selectedIndex);
                telemetry.addLine("=== SERVO CONTROL ===");
                telemetry.addData("Servo", STU.ServoNames.get(selectedIndex));
                telemetry.addData("Pos", "%.2f", selectedServo.getPosition());
                telemetry.addLine("R_Trigger: + Fast");
                telemetry.addLine("L_Trigger: - Fast");
                telemetry.addLine("R_Bumper: + Slow");
                telemetry.addLine("L_Bumper: - Slow");
                telemetry.addLine("B: Return to selection");
                break;
            case SELECT_CRSERVO:
                telemetry.addLine("=== SELECT A CRSERVO ===");
                telemetry.addLine("Use D-Pad Up/Down to scroll");
                telemetry.addLine("Press A to select");
                telemetry.addLine("Press B to turn off this CRServo");
                telemetry.addLine("Press X to turn off all CRServos");
                telemetry.addLine("Selected Index: " + selectedIndex);
                listComponents();
                break;
            case CONTROL_CRSERVO:
                CRServo selectedCRServo = CRSTU.CRServos.get(selectedIndex);
                telemetry.addLine("=== CRSERVO CONTROL ===");
                telemetry.addData("Motor", CRSTU.CRServoNames.get(selectedIndex));
                telemetry.addData("power", "%.2f", selectedCRServo.getPower());
                telemetry.addLine("R_Trigger: + Fast");
                telemetry.addLine("L_Trigger: - Fast");
                telemetry.addLine("R_Bumper: + Slow");
                telemetry.addLine("L_Bumper: - Slow");
                telemetry.addLine("B: Return to selection");
                break;
        }
    }

    public void listComponents(){
        // General func to list the devices of the selected type
        ArrayList<String> deviceNames = new ArrayList<>();
        switch (state){
            case SELECT_MOTOR:
                deviceNames = MTU.motorNames;
                break;
            case SELECT_SERVO:
                deviceNames = STU.ServoNames;
                break;
            case SELECT_CRSERVO:
                deviceNames = CRSTU.CRServoNames;
                break;
            default:
                deviceNames.add("ERROR: Not in a selection state");
        }

        for (int i = 0; i < deviceNames.size(); i++) {
            if (i == selectedIndex) {
                telemetry.addData(">", deviceNames.get(i));
            } else {
                telemetry.addData(" ", deviceNames.get(i));
            }
        }
    }

    public void GUIControl(){
        // Depending on the state we are in we are dealing w/ diff size arr lists
        // To make sure we dont go out of bounds, we need to find out what arr we are using so we can % by the right #
        int arraySize = 0;
        switch (state){
            case SELECT_MOTOR:
            case CONTROL_MOTOR:
                arraySize = MTU.motors.size();
                break;
            case SELECT_SERVO:
            case CONTROL_SERVO:
                arraySize = STU.servos.size();
                break;
            case SELECT_CRSERVO:
            case CONTROL_CRSERVO:
                arraySize = CRSTU.CRServos.size();
                break;
        }

        // Now we deal if the user wants to change the selected index
        if (GPM.isPressed(Button.DPAD_DOWN) && arraySize > 0){
            if (selectedIndex == arraySize - 1)
                selectedIndex = 0;
            else
                selectedIndex++;
        }
        if (GPM.isPressed(Button.DPAD_UP) && arraySize > 0){
            if (selectedIndex == 0)
                selectedIndex = arraySize - 1;
            else
                selectedIndex--;
        }

        // Changing tabs (from selecting a motor -> servo -> CRServo)
        // Need to set selectedIndex to 0 so we start at top of the list
        if (GPM.isPressed(Button.DPAD_RIGHT)){
            if (state == State.SELECT_MOTOR){
                state = State.SELECT_SERVO;
            } else if (state == State.SELECT_SERVO){
                state = State.SELECT_CRSERVO;
            } else if (state == State.SELECT_CRSERVO){
                state = State.SELECT_MOTOR;
            }
            selectedIndex = 0;
        }
        if (GPM.isPressed(Button.DPAD_LEFT)){
            if (state == State.SELECT_CRSERVO){
                state = State.SELECT_SERVO;
            } else if (state == State.SELECT_SERVO){
                state = State.SELECT_MOTOR;
            } else if (state == State.SELECT_MOTOR){
                state = State.SELECT_CRSERVO;
            }
            selectedIndex = 0;
        }

        // Going from controlling a component to the corrosponding selection screen
        if (GPM.isPressed(Button.B)) {
            if (state == State.CONTROL_MOTOR){
                state = State.SELECT_MOTOR;
            } else if (state == State.CONTROL_SERVO){
                state = State.SELECT_SERVO;
            } else if (state == State.CONTROL_CRSERVO){
                state = State.SELECT_CRSERVO;
            }
        }
        // Going from selecting a component to the corrosponding controlling screen
        if (GPM.isPressed(Button.A)) {
            if (state == State.SELECT_MOTOR){
                state = State.CONTROL_MOTOR;
            } else if (state == State.SELECT_SERVO){
                state = State.CONTROL_SERVO;
            } else if (state == State.SELECT_CRSERVO){
                state = State.CONTROL_CRSERVO;
            }
        }

    }
}


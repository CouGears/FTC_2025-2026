package org.firstinspires.ftc.teamcode.cougears.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager.Button;


@TeleOp(name="TransferTest", group="Testing")
public class TransferTest extends LinearOpMode {

    private CRServo transfer1;
    private CRServo transfer2;
    private GamepadManager GPM = null;

    @Override
    public void runOpMode() {
        try {
            transfer1 = hardwareMap.get(CRServo.class, "Transfer1");
            transfer2 = hardwareMap.get(CRServo.class, "Transfer2");
            GPM = new GamepadManager(gamepad1);
        } catch (Exception e) {
            telemetry.addData("ERROR", e);
        }

        // Reverse one servo
        transfer1.setDirection(CRServo.Direction.REVERSE);
        telemetry.addLine("Press A to toggle motors/servos");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            // Toggle logic
            if (GPM.isHeld(Button.A)) {
                    transfer1.setPower(1);
                    transfer2.setPower(1);
                }
            else {
                transfer1.setPower(0);
                transfer2.setPower(0);
            }
            telemetry.addData("Transfer1 Power", transfer1.getPower());
            telemetry.addData("Transfer2 Power", transfer2.getPower());
            telemetry.update();
            GPM.update();
        }
        // Stop all at the end
        transfer1.setPower(0);
        transfer2.setPower(0);
    }
}




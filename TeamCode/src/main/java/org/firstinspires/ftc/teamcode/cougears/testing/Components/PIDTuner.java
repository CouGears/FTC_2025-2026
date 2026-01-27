package org.firstinspires.ftc.teamcode.cougears.testing.Components;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;

@TeleOp (group = "Testing")
public class PIDTuner extends LinearOpMode {
    public DcMotorEx FW;
    public double highVel = FW_shootVelFar;
    public double lowVel = FW_shootVel;
    public double curTargVel = highVel;
    public double[] increm = {10, 1, 0.1, 0.01};

    public double P = 0;
    public double I = 0;
    public double D = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        int ind = 0;
        GamepadManager GPM = new GamepadManager(gamepad1);
        try {
            FW = hardwareMap.get(DcMotorEx.class, "FW");
        } catch (Exception e) {
            telemetry.addData("Error", "Could not find motor named 'FW'. Check your configuration.");
            telemetry.update();
            sleep(5000);
            return;
        }
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.yWasPressed()){
                if (curTargVel == highVel){
                    curTargVel = lowVel;
                } else {
                    curTargVel = highVel;
                }
            }
            if (gamepad1.aWasPressed()){
                ind = (ind+1)%increm.length;
            }
            if (gamepad1.dpadLeftWasPressed()){
                I-=increm[ind];
            }

            if (gamepad1.dpadRightWasPressed()){
                I+=increm[ind];
            }

            if (gamepad1.dpadUpWasPressed()){
                P+=increm[ind];
            }

            if (gamepad1.dpadDownWasPressed()){
                P-=increm[ind];
            }
            if (gamepad1.bWasPressed()){
                D+=increm[ind];
            }

            if (gamepad1.xWasPressed()){
                D-=increm[ind];
            }
            PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, 0);
            FW.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
            FW.setVelocity(curTargVel);

            double curVel = FW.getVelocity();
            double error = curTargVel-curVel;

            telemetry.addData("Target Velocity", curTargVel);
            telemetry.addData("Current Velocity", curVel);
            telemetry.addData("Error", error);
            telemetry.addData("P", "%.2f", P);
            telemetry.addData("I", "%.2f", I);
            telemetry.addData("D", "%.2f", D);
            telemetry.addData("Increments", "%.2f", increm[ind]);
            sleep(10);
        }
    }
}

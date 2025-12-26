package org.firstinspires.ftc.teamcode.cougears.testing.Components;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
public class PIDFManualTuner extends OpMode {
    public DcMotorEx FW;
    public double highVel = FW_shootVelFar;
    public double lowVel = FW_shootVel;
    public double curTargVel = highVel;
    public double[] increm = {10, 1, 0.1, 0.01,};
    public int ind = 0;
    double P = 0;
    double F = 0;

    @Override
    public void init(){
        FW = hardwareMap.get(DcMotorEx.class, "FW");
        FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        FW.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        telemetry.addLine("Init Complete");
    }

    @Override
    public void loop(){
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
            F-=increm[ind];
        }

        if (gamepad1.dpadRightWasPressed()){
            F+=increm[ind];
        }

        if (gamepad1.dpadUpWasPressed()){
            P+=increm[ind];
        }

        if (gamepad1.dpadDownWasPressed()){
            P-=increm[ind];
        }


        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        FW.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        double curVel = FW.getVelocity();
        double error = curTargVel-curVel;

        telemetry.addData("Target Velocity", curTargVel);
        telemetry.addData("Current Velocity", curVel);
        telemetry.addData("Error", error);
        telemetry.addData("P", "%.2f (Dpad U-/D+)", P);
        telemetry.addData("F", "%.2f (Dpad L-/R+)", F);
        telemetry.addData("Increments", "%.2f (A+/B-)", increm[ind]);
        telemetry.addLine("Guide: Use Y to slow wheel down to test the adjustment" +
                "\nAdd a bunch of P to start" +
                "\nTune F until error is low" +
                "\nThen readjust P to get there faster" +
                "\nreadjust F again");




    }
}

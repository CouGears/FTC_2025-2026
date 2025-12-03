package org.firstinspires.ftc.teamcode.cougears.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp(name="FullIntakeTestServo", group="Testing")
public class FullIntakeTestServo extends LinearOpMode {


    private DcMotorEx fw;
    private DcMotorEx intake;
    private CRServo transfer1;
    private CRServo transfer2;
    private CRServo transfer3;


    @Override
    public void runOpMode() {


        fw = hardwareMap.get(DcMotorEx.class, "FW");
        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        transfer1 = hardwareMap.get(CRServo.class, "Transfer1");
        transfer2 = hardwareMap.get(CRServo.class, "Transfer2");



        // Reverse one servo
        transfer1.setDirection(CRServo.Direction.REVERSE);


        telemetry.addLine("Press A to toggle motors/servos");
        telemetry.update();


        waitForStart();


        boolean motorsOn = false;
        boolean aPrev = false;


        while (opModeIsActive()) {
            boolean rtpress = gamepad1.right_bumper;
            boolean ltpress = gamepad1.left_bumper;


            // Toggle logic
            if (a && !aPrev) {
                motorsOn = !motorsOn;


                if (motorsOn) {
                    fw.setPower(1.0);
                    intake.setPower(1.0);
                    transfer1.setPower(1);
                    transfer2.setPower(1);
                    transfer3.setPower(1);


                } else {
                    fw.setPower(0);
                    intake.setPower(0);
                    transfer1.setPower(0);
                    transfer2.setPower(0);
                    transfer3.setPower(0);


                }
            }
            aPrev = a;


            telemetry.addData("Motors On?", motorsOn);
            telemetry.addData("FW Power", fw.getPower());
            telemetry.addData("Intake Power", intake.getPower());
            telemetry.addData("Transfer1 Power", transfer1.getPower());
            telemetry.addData("Transfer2 Power", transfer2.getPower());
            telemetry.update();
        }


        // Stop all at the end
        fw.setPower(0);
        intake.setPower(0);
        transfer1.setPower(0);
        transfer2.setPower(0);
        transfer3.setPower(0);


    }
}




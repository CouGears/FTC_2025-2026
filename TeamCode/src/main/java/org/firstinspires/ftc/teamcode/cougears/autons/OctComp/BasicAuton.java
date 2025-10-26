package org.firstinspires.ftc.teamcode.cougears.autons.OctComp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;

@Autonomous(name="Simple Fwd Autn", group="Autonomous")
public class BasicAuton extends LinearOpMode {

    private ElapsedTime timer = new ElapsedTime();
    private final double TIME_RUNNING = 3.0;

    @Override
    public void runOpMode() {
        BotBase bot = new BotBase(hardwareMap, telemetry, gamepad1, gamepad2);
        bot.botInit();

        // Signal that initialization is complete
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();
        /* For reference
        double frontLeftPower = strafe + drive + rotate;
        double frontRightPower = strafe - drive - rotate;
        double backLeftPower = strafe - drive + rotate;
        double backRightPower = strafe + drive - rotate;
         */
        bot.motorFL.setPower(0.5);
        bot.motorFR.setPower(-0.5);
        bot.motorBL.setPower(-0.5);
        bot.motorBR.setPower(0.5);

        timer.reset();

        while (opModeIsActive() && timer.seconds() < TIME_RUNNING) {
            // We can add telemetry here to see the timer counting up
            telemetry.addData("Time", "Running for %.2f seconds", timer.seconds());
            telemetry.update();
        }

        bot.motorFL.setPower(0);
        bot.motorFR.setPower(0);
        bot.motorBL.setPower(0);
        bot.motorBR.setPower(0);

    }
}
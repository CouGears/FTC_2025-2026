package org.firstinspires.ftc.teamcode.cougears.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Pinpoint Odometry Test", group = "Testing")
public class Odo extends LinearOpMode {

    GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {

        // Get the Pinpoint from hardware map
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // ---- REQUIRED SETUP (do once on init) ----

        // goBILDA 4-bar pods
        pinpoint.setEncoderResolution(
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
        );

        // Encoder directions (adjust if needed)
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        // Pod offsets (CHANGE THESE to match your robot, in mm)
        // Example values only
        pinpoint.setOffsets(-84, -168,DistanceUnit.MM);

        // Zero position + IMU (robot MUST be still)
        pinpoint.resetPosAndIMU();

        telemetry.addLine("Pinpoint initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // REQUIRED every loop
            pinpoint.update();

            // Read position
            double x = pinpoint.getPosition().getX(DistanceUnit.MM);
            double y = pinpoint.getPosition().getY(DistanceUnit.MM);
            double heading = pinpoint.getPosition().getHeading(AngleUnit.DEGREES);

            telemetry.addData("X (mm)", x);
            telemetry.addData("Y (mm)", y);
            telemetry.addData("Heading (deg)", heading);
            telemetry.addData("Status", pinpoint.getDeviceStatus());

            telemetry.update();
        }
    }
}

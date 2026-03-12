package org.firstinspires.ftc.teamcode.cougears.testing.Components;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.cougears.util.SensorFusionManager;

@TeleOp
public class LEDTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SensorFusionManager SFM = new SensorFusionManager(hardwareMap, telemetry);
        ElapsedTime timer = new ElapsedTime();
        waitForStart();
        while (opModeIsActive()) {
            timer.reset();
            telemetry.addData("1", SFM.sensorDetectingBall(1));
            telemetry.addData("1 Dist", SFM.distSensor1.getDistance(DistanceUnit.CM));

            telemetry.addData("2", SFM.sensorDetectingBall(2));
            telemetry.addData("2 Dist", SFM.distSensor2.getDistance(DistanceUnit.CM));

            telemetry.addData("3", SFM.sensorDetectingBall(3));
            telemetry.addData("3 Dist", SFM.distSensor3.getDistance(DistanceUnit.CM));
            telemetry.addData("Loop took (ms)", timer.milliseconds());
            telemetry.update();
        }
    }
}

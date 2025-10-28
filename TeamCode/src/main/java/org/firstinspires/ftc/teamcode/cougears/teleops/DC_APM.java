package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTagBase;


public class DC_APM extends AprilTagBase{

    public DC_Teleopbase bot;
    public final GamepadManager GPM_1, GPM_2;
    public int desiredTag = -1;


    public DC_APM(DC_Teleopbase robot, HardwareMap HardwareMap, Telemetry Telemetry,
                  AprilTagProcessor AprilTagProcessor, VisionPortal VisionPortal,
                  Gamepad gamepad1, Gamepad gamepad2) {
        super(HardwareMap, Telemetry, AprilTagProcessor, VisionPortal);
        bot = robot;
        GPM_1 = new GamepadManager(gamepad1);
        GPM_2 = new GamepadManager(gamepad2);
    }

    public boolean APScan(int tagID) {
        if (aprilTag == null || aprilTag.getDetections() == null || aprilTag != tagID) {
            tele.addData("AprilTag", "Processor not initialized or no detections");
            return false;
        } else {
            return true;
        }
    }
    public double alignToAP(int tagID) {
        if (APScan(tagID)) {
            double APbearing = tag.ftcPose.bearing;
        } else {

        }

        if (Math.abs(APbearing) <= APBearingTolerance) {
            return 0; // aligned
        }
        double rotatePower = Range.clip(APbearing * 0.02, -0.5, 0.5);
        bot.manualMove(0, 0, rotatePower);
        return rotatePower;
    }

    public double moveToAPDist(int tagID, double desiredDistance) {
        ATval(tagID, false);
        if (APdist == 0) return 0;

        double drivePower = Range.clip((APdist - desiredDistance) * 0.01, -0.5, 0.5);
        bot.manualMove(0, drivePower, 0);
        return drivePower;
    }

    public void aprilLock(int tagID, double desiredDistance) {
        double rotatePower = alignToTag(tagID);
        double drivePower = moveToAPDist(tagID, desiredDistance);

        tele.addData("Rotate", rotatePower);
        tele.addData("Drive", drivePower);
        tele.update();
    }
}

}

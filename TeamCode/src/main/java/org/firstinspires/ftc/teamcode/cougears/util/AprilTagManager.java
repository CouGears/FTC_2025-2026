package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.teleops.OctoberCompTeleOpBase;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
/*
WHAT THIS FILE SHOULD BE ABLE TO DO:
- Take the bot from teleop and move it
- Know what team we are on
 */


public class AprilTagManager extends AprilTagBase{

    public BotBase bot;
    public int wallTag = -1;
    public int ATBearingTolerance = 1;
    
    public AprilTagManager(HardwareMap HardwareMap, Telemetry Telemetry, BotBase Bot) {
        super(HardwareMap, Telemetry);
        bot = Bot;
    }


    public void alignToAT(int tagID) {
        AprilTagDetection tag = scanForAT(tagID);
        if (tag == null) // BE CAREFUL
            return;

        double ATbearing = tag.ftcPose.bearing;

        if (Math.abs(ATbearing) <= ATBearingTolerance) {
            return;
        }
        double rotatePower = ATbearing*0.05;
        bot.manualMove(0, 0, rotatePower);
        tele.addData("Bearing", "%f", ATbearing);
    }

    /*public double moveToATDist(int tagID, double desiredDistance) {
        ATval(tagID, false);
        if (ATdist == 0) return 0;

        double drivePower = Range.clip((ATdist - desiredDistance) * 0.01, -0.5, 0.5);
        bot.manualMove(0, drivePower, 0);
        return drivePower;
    }

    public void aprilLock(int tagID, double desiredDistance) {
        double rotatePower = alignToTag(tagID);
        double drivePower = moveToATDist(tagID, desiredDistance);

        tele.addData("Rotate", rotatePower);
        tele.addData("Drive", drivePower);
        tele.update();
    }*/
}

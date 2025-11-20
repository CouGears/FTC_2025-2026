package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.cougears.legacy_examples.OctComp.teleops.OctoberCompTeleOpBase;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
/*
WHAT THIS FILE SHOULD BE ABLE TO DO:
- Take the bot from teleop and move it
- Know what team we are on
 */


public class AprilTagManager extends AprilTagBase{

    public BotBase bot;
    public int ATBearingTolerance = 1;

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

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

    public void FullAutoMove(int tagID){
        AprilTagDetection tag = scanForAT(tagID);
        if (tag == null) // BE CAREFUL
            return;

        // We want to go to the closer position
        double distToClosePos = Math.abs(tag.ftcPose.range - desiredDistClose);
        double distToFarPos = Math.abs(tag.ftcPose.range - desiredDistFar);
        double  rangeError;
        if (distToClosePos < distToFarPos)
            rangeError = (tag.ftcPose.range - desiredDistClose);
        else
            rangeError = (tag.ftcPose.range - desiredDistFar);
        // These are calculated the same no matter what
        double  headingError    = tag.ftcPose.bearing;
        double  yawError        = tag.ftcPose.yaw;

        // Use the speed and turn "gains" to calculate how we want the robot to move.
        double drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
        double turn   = -Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
        double strafe = -Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
        bot.manualMove(drive, strafe, turn);
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

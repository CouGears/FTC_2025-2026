package org.firstinspires.ftc.teamcode.cougears.teleops;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
import org.firstinspires.ftc.teamcode.cougears.util.goalUtils;
/*
WHAT THIS FILE SHOULD BE ABLE TO DO:
- Take the bot from teleop and move it
- Know what team we are on
 */


public class V2AprilTagManager extends AprilTagBase {

    public BotBase bot;
    public V2TeleOpBase v2bot = null;
    public goalUtils goal;
    public int ATBearingTolerance = 1;

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 1;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 1;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 1;   //  Clip the turn speed to this max value (adjust for your robot)
    public int lockedTagID = AT_redTag;
    public boolean toggleTagLock = false;

    public V2AprilTagManager(HardwareMap HardwareMap, Telemetry Telemetry, BotBase Bot) {
        super(HardwareMap, Telemetry);
        bot = Bot;
    }
    public V2AprilTagManager(HardwareMap HardwareMap, Telemetry Telemetry, V2TeleOpBase Bot, goalUtils Goal) {
        super(HardwareMap, Telemetry);
        v2bot = Bot;
        bot = Bot;
        goal = Goal;
    }






    public void alignTurretToAT() {
        if (v2bot == null) return;
        AprilTagDetection tag = scanForAT(goal.getLockedTagID());
        if (tag == null) return;

        double bearing = tag.ftcPose.bearing;   // degrees offset of tag from robot
        tele.addLine("--- alignTurretToAT ---");
        tele.addData("Bearing", "%.2f", tag.ftcPose.bearing);
        v2bot.adjustTurretDeg(bearing);   // <-- let TeleOpBase handle conversion
    }
    public void FullAutoMove(int tagID){
        AprilTagDetection tag = scanForAT(tagID);
        if (tag == null) // BE CAREFUL
            return;

        // We want to go to the closer position
        double distToClosePos = Math.abs(tag.ftcPose.range - AT_desiredDistClose);
        double distToFarPos = Math.abs(tag.ftcPose.range - AT_desiredDistFar);
        double  rangeError;
        if (distToClosePos < distToFarPos)
            rangeError = (tag.ftcPose.range - AT_desiredDistClose);
        else
            rangeError = (tag.ftcPose.range - AT_desiredDistFar);
        // These are calculated the same no matter what
        double  headingError    = tag.ftcPose.bearing;
        double  yawError        = tag.ftcPose.yaw;

        // Use the speed and turn "gains" to calculate how we want the robot to move.
        double drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
        double turn   = -Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
        double strafe = -Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
        tele.addLine("--- FullAutoMove ---");
        tele.addData("Bearing", "%.2f", tag.ftcPose.bearing);
        tele.addData("drive", "%.2f", drive);
        tele.addData("turn", "%.2f", turn);
        tele.addData("strafe", "%.2f", strafe);

        bot.manualMove(drive, strafe, turn);
    }

    public double ATDist(int tagID){
        AprilTagDetection tag = scanForAT(tagID);
        if (tag == null) // BE CAREFUL
            return -1;
        return tag.ftcPose.range;
    }







    //DEPRECEATED


    public void alignToAT(int tagID) {
        AprilTagDetection tag = scanForAT(tagID);
        if (tag == null) // BE CAREFUL
            return;

        double ATbearing = tag.ftcPose.bearing;

        if (Math.abs(ATbearing) <= ATBearingTolerance) {
            return;
        }
        double rotatePower = ATbearing*0.05;

        tele.addLine("--- alignToAT ---");
        tele.addData("Bearing", "%.2f", tag.ftcPose.bearing);
        tele.addData("rotatePower", "%.2f", rotatePower);

        bot.manualMove(0, 0, -rotatePower);
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

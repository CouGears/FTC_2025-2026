package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;


public class SensorFusionManager {
    AprilTagBase ATB;
    V3TeleOpBase bot;
    Telemetry telemetry;
    public LED redLED1, greenLED1, redLED2, greenLED2, redLED3, greenLED3;
    public DistanceSensor distSensor1, distSensor2, distSensor3;



    public SensorFusionManager(HardwareMap HM, Telemetry telemetry, AprilTagBase ATB, V3TeleOpBase bot) {
         this.ATB = ATB;
         this.bot = bot;
         this.telemetry = telemetry;
        redLED1 = HM.get(LED.class, "redLED1");
        greenLED1 = HM.get(LED.class, "greenLED1");
        redLED2 = HM.get(LED.class, "redLED2");
        greenLED2 = HM.get(LED.class, "greenLED2");
        redLED3 = HM.get(LED.class, "redLED3");
        greenLED3 = HM.get(LED.class, "greenLED3");
        distSensor1 = HM.get(DistanceSensor.class, "DistSensor1");
        distSensor2 = HM.get(DistanceSensor.class, "DistSensor2");
        distSensor3 = HM.get(DistanceSensor.class, "DistSensor3");
    }

    public SensorFusionManager(HardwareMap HM, Telemetry telemetry) {
        this.telemetry = telemetry;
        redLED1 = HM.get(LED.class, "redLED1");
        greenLED1 = HM.get(LED.class, "greenLED1");
        redLED2 = HM.get(LED.class, "redLED2");
        greenLED2 = HM.get(LED.class, "greenLED2");
        redLED3 = HM.get(LED.class, "redLED3");
        greenLED3 = HM.get(LED.class, "greenLED3");
        distSensor1 = HM.get(DistanceSensor.class, "DistSensor1");
        distSensor2 = HM.get(DistanceSensor.class, "DistSensor2");
        distSensor3 = HM.get(DistanceSensor.class, "DistSensor3");
    }

    public enum fullShootPosAlignSequence {
        FIND_TAG_ID,
        GO_TO_POS,
        ALIGN_TO_TAG
    }

    fullShootPosAlignSequence fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_ID;
    int targetTag = redTag;
    double ATbearing = 0.0;
    double rotatePower = 0.0;
    public boolean handFullShootPosAlignSequence(PedroTeleOpManager PTM) {
        telemetry.addData("Curr Step in handFullShootPosAlignSequence:", "%s", fullShootPosAlignSequenceSavedStep);
        switch (fullShootPosAlignSequenceSavedStep) {
            case FIND_TAG_ID:
                targetTag = Storage_endOfAutonColor.equals("Blue") ? blueTag : redTag;
                fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.GO_TO_POS;
                ATbearing = 0.0;
                rotatePower = 0.0;
                break;
            case GO_TO_POS:
                ShootingPosition closestPose = PTM.getClosestShootingPosition();
                PTM.moveToPos(closestPose.getShootingPose());
                fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.ALIGN_TO_TAG;
                break;
            case ALIGN_TO_TAG:
                if (PTM.isBusy()) return false;
                AprilTagDetection tag = ATB.scanForAT(targetTag);
                if (tag == null) return false;
                ATbearing = tag.ftcPose.bearing;
                if (Math.abs(ATbearing) <= ATBearingTolerance) {
                    fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_ID;
                    return true;
                }
                rotatePower = ATbearing * 0.05;
                bot.manualMove(0, 0, -rotatePower);
            return false;
        }
        return false;
    }
    public void resetStep(){
        fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_ID;
    }

    public boolean sensorDetectingBall(int position){
        if (distSensor1.getDistance(DistanceUnit.CM) < DIST_SENSOR_BALL_DISTANCE1 && position == 1) return true;
        if (distSensor2.getDistance(DistanceUnit.CM) < DIST_SENSOR_BALL_DISTANCE2 && position == 2) return true;
        if (distSensor3.getDistance(DistanceUnit.CM) < DIST_SENSOR_BALL_DISTANCE3 && position == 3) return true;
        return false;
    }

    public enum ballState{
        NO_BALLS,
        ONE_BALL,
        TWO_BALLS,
        THREE_BALLS
    }

    public ballState ballInPosition(){
        boolean pos1 = sensorDetectingBall(1);
        boolean pos2 = sensorDetectingBall(2);
        boolean pos3 = sensorDetectingBall(3);
        if (pos1&&pos2&&pos3){
            return ballState.THREE_BALLS;
        } else if (pos1 && pos2){
            return ballState.TWO_BALLS;
        }else if (pos1 && !pos3){
            return ballState.ONE_BALL;
        } else {
            return ballState.NO_BALLS;
        }
    }

    //****** LED ******
    public void handleLEDS(PedroTeleOpManager PTM, Boolean botAligned) {
        FWVelLED(PTM, bot.FW.getVelocity());
        //ballPositionLED();
        //botAlignedLED(botAligned);
    }
    public void FWVelLED(PedroTeleOpManager PTM, double FWVel) {
        double shootVelThirds = (double) (PTM.getClosestShootingPosition().getShootingVelocity() / 3);
        if (FWVel > shootVelThirds * 3 - Auton_startShootingVelocityTolerance) {
            greenLED1.on();
            redLED1.off();
        } else if (FWVel > shootVelThirds * 2 - Auton_startShootingVelocityTolerance) {
            greenLED1.off();
            redLED1.on();
        } else if (FWVel > shootVelThirds - Auton_startShootingVelocityTolerance) {
            greenLED1.on();
            redLED1.on();
        } else {
            greenLED1.off();
            redLED1.off();
        }
    }
    public void ballPositionLED(){
        if (ballInPosition().equals(ballState.NO_BALLS)){
            greenLED2.off();
            redLED2.off();
        } else if (ballInPosition().equals(ballState.ONE_BALL)){
            greenLED2.off();
            redLED2.on();
        } else if (ballInPosition().equals(ballState.TWO_BALLS)){
            greenLED2.on();
            redLED2.on();
        } else if (ballInPosition().equals(ballState.THREE_BALLS)){
            greenLED2.on();
            redLED2.off();
        }
    }

    public void botAlignedLED(Boolean botAligned){
        if (botAligned) {
            greenLED3.on();
            redLED3.off();
        } else {
            greenLED3.off();
            redLED3.on();
        }
    }

}

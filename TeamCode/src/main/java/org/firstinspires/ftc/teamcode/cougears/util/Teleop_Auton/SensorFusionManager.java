package org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.teleops.V3TeleOpBase;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import com.bylazar.camerastream.PanelsCameraStream;


public class SensorFusionManager {
    AprilTagBase ATB;
    V3TeleOpBase bot;
    Telemetry telemetry;

    public SensorFusionManager(HardwareMap HM, Telemetry telemetry, AprilTagBase ATB, V3TeleOpBase bot) {
         this.ATB = ATB;
         this.bot = bot;
         this.telemetry = telemetry;
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



}

package org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.cougears.util.AprilTag.AprilTagBase;
import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

import android.health.connect.datatypes.SexualActivityRecord;


public class SensorFusionShooting {
    HardwareMap HM;
    PedroTeleOpManager PTM;
    AprilTagBase ATM;
    BotBase bot;

    public SensorFusionShooting(HardwareMap hardwareMap, Telemetry telemetry) {
        HM = hardwareMap;
        PedroTeleOpManager PTM = new PedroTeleOpManager(HM);
        AprilTagBase ATM = new AprilTagBase(HM, telemetry);
        BotBase bot = new BotBase(HM, telemetry);
    }

    public enum fullShootPosAlignSequence {
        FIND_TAG_ID,
        GO_TO_POS,
        FIND_TAG_DETAILS,
        ALIGN_TO_TAG
    }

    fullShootPosAlignSequence fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_ID;

    public boolean handFullShootPosAlignSequence(Follower follower) {
        int targetTag = redTag;
        double ATbearing = 0.0;
        switch (fullShootPosAlignSequenceSavedStep) {
            case FIND_TAG_ID:
                if (Storage_endOfAutonColor.equals("Blue")) {
                    targetTag = blueTag;
                }
                fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.GO_TO_POS;
                break;
            case GO_TO_POS:
                ShootingPosition closestPose = PTM.getClosestShootingPosition();
                PTM.moveToPos(closestPose.getShootingPose());
                fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_DETAILS;
            case FIND_TAG_DETAILS:
                if (PTM.isBusy()) return false;
                AprilTagDetection tag = ATM.scanForAT(targetTag);
                if (tag == null) // BE CAREFUL
                    return false;

                ATbearing = tag.ftcPose.bearing;
                fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.ALIGN_TO_TAG;

            case ALIGN_TO_TAG:
                if (Math.abs(ATbearing) <= ATBearingTolerance) {
                    return false;
                }
                double rotatePower = ATbearing*0.05;
                bot.manualMove(0, 0, -rotatePower);
                return true;
        }
        return false;
    }
    public void resetStep(){
        fullShootPosAlignSequenceSavedStep = fullShootPosAlignSequence.FIND_TAG_ID;
    }
}

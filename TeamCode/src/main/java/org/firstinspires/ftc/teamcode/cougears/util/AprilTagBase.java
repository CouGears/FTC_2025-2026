package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.cougears.teleops.OctComp.OctoberCompTeleOpBase;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
WHAT THIS FILE SHOULD BE ABLE TO DO:
- Init AT camera
- Scan for ATs
 */

public class AprilTagBase {
    private static final boolean USE_WEBCAM = true;
    private AprilTagDetection desiredTag = null;
    public AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    final int gain = 255;
    final int exposureMS = 1;

    public final HardwareMap HM;
    public final Telemetry tele;

    public AprilTagBase(HardwareMap HardwareMap, Telemetry Telemetry) {
        // Constructors job is not to init all vars, it is to get important components from Teleop that it cant create itself
        HM = HardwareMap;
        tele = Telemetry;
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(HM.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();
    }

    public boolean setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) return false;
        // Check if camera is streaming
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            tele.addData("Camera", "Not streaming yet");
            tele.update();
            return false;  // cannot set controls yet
        }

        // Set exposure
        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
        }
        exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);

        // Set gain
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);

        return true;
    }
    /*
     This func returns the whole detection if it finds the desired detection.
     RETURNS NULL IF NOT FOUND. BE CAREFUL.
     Do like this:
     AprilTagDetection tag = scanForAT(tagID);
     if (tag == null) // BE CAREFUL
         return;
    */
    public AprilTagDetection scanForAT(int tagID) {
        if (aprilTag == null || aprilTag.getDetections() == null) {
            tele.addData("AprilTag", "Processor not initialized or no detections");
            return null;
        }
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection currDetection : currentDetections){
            if (currDetection.id == tagID){
                return currDetection;
            }
        }
        return null;
    }
}

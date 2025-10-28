package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;



public class AprilTagBase {
    private static final boolean USE_WEBCAM = true;
    public AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    private AprilTagDetection desiredTag = null;
    public final HardwareMap HM;
    public final Telemetry tele;
    final int gain = 255;
    final int exposureMS = 1;

    public AprilTagBase(HardwareMap HardwareMap, Telemetry Telemetry,
                        AprilTagProcessor AprilTagProcessor, VisionPortal VisionPortal) {
        HM = HardwareMap;
        tele = Telemetry;
        aprilTag = AprilTagProcessor;
        visionPortal = VisionPortal;

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
}

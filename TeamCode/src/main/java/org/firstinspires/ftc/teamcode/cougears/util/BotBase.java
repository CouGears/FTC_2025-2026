package org.firstinspires.ftc.teamcode.cougears.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

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

public class BotBase {
    public DcMotor motorFL;
    public DcMotor motorFR;
    public DcMotor motorBL;
    public DcMotor motorBR;
    // Constants for motor speed
    private static final double MAX_SPEED = 1.0;
    private static final double MIN_SPEED = -1.0;

    public final HardwareMap HM;
    public final Telemetry tele;

    public final GamepadManager GPM_1, GPM_2;

    //APRIL TAG CONSTANTS/VARIABLES
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private AprilTagDetection desiredTag = null;

    public double ATdist = 0; // stores distance to last detected tag

    private static final int DESIRED_TAG_ID = -1; // any tag
    private static final double DESIRED_DISTANCE = 67.0;
    private final int ATBlue = 21;
    private final int ATRed = 24;


    public BotBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
        HM = HardwareMap;
        tele = Telemetry;
        GPM_1 = new GamepadManager(gamepad1);
        GPM_2 = new GamepadManager(gamepad2);

        initAprilTag();
    }

    // ****** MOTORS AND DRIVING ******
    public boolean botInit() {
        try {
            motorFL = HM.get(DcMotor.class, "motorFL");
            motorFR = HM.get(DcMotor.class, "motorFR");
            motorBL = HM.get(DcMotor.class, "motorBL");
            motorBR = HM.get(DcMotor.class, "motorBR");

            motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
            motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
            motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
            motorBR.setDirection(DcMotorSimple.Direction.FORWARD);

            motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            return true;
        } catch (Exception e) {
            tele.addData("ERROR", e);
            return false;
        }
    }

    public void drive(Gamepad gamepad1){
        double drive = gamepad1.left_stick_y; // Forward/back strafe on left stick Y
        double strafe = gamepad1.left_stick_x; // Left/right drive on left stick X
        double rotate = gamepad1.right_stick_x; // Rotation on right stick X

        // Calculate drive motor powers for strafe-forward configuration
        double frontLeftPower = strafe + drive + rotate;
        double frontRightPower = strafe - drive - rotate;
        double backLeftPower = strafe - drive + rotate;
        double backRightPower = strafe + drive - rotate;

        // Normalize drive motor powers
        double maxPower = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        motorFL.setPower(Range.clip(frontLeftPower, MIN_SPEED, MAX_SPEED));
        motorFR.setPower(Range.clip(frontRightPower, MIN_SPEED, MAX_SPEED));
        motorBL.setPower(Range.clip(backLeftPower, MIN_SPEED, MAX_SPEED));
        motorBR.setPower(Range.clip(backRightPower, MIN_SPEED, MAX_SPEED));
        tele.addData("Drive Motors", "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public void manualMove (double x, double y, double yaw) {
        double frontLeftPower    =  x - y - yaw;
        double frontRightPower   =  x + y + yaw;
        double backLeftPower     =  x + y - yaw;
        double backRightPower    =  x - y + yaw;

        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        motorFL.setPower(frontLeftPower);
        motorFR.setPower(frontRightPower);
        motorBL.setPower(backLeftPower);
        motorBR.setPower(backRightPower);
    }

    /*public void lockToApriltag(Gamepad gamepad1, double yaw){
        double drive = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = yaw;

        double frontLeftPower = strafe + drive + rotate;
        double frontRightPower = strafe - drive - rotate;
        double backLeftPower = strafe - drive + rotate;
        double backRightPower = strafe + drive - rotate;

        double maxPower = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        motorFL.setPower(Range.clip(frontLeftPower, MIN_SPEED, MAX_SPEED));
        motorFR.setPower(Range.clip(frontRightPower, MIN_SPEED, MAX_SPEED));
        motorBL.setPower(Range.clip(backLeftPower, MIN_SPEED, MAX_SPEED));
        motorBR.setPower(Range.clip(backRightPower, MIN_SPEED, MAX_SPEED));
        tele.addData("Drive Motors", "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }
    */


    // ****** GPM ******
    public boolean isPressed (int controllerNum, GamepadManager.Button b){
        if (controllerNum == 2)
            return GPM_2.isPressed(b);
        else
            return GPM_1.isPressed(b);
    }

    public boolean isHeld (int controllerNum, GamepadManager.Button b){
        if (controllerNum == 2)
            return GPM_2.isHeld(b);
        else
            return GPM_1.isHeld(b);
    }

    // ****** MISC ******
    public void update(){
        GPM_1.update();
        GPM_2.update();
        tele.update();
    }

    public void endTeleOp(){
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }

    // ****** APRIL TAG SUPPORT ******
    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        aprilTag.setDecimation(2);

        VisionPortal.Builder builder = new VisionPortal.Builder();

        if (USE_WEBCAM) {
            builder.setCamera(HM.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        builder.addProcessor(aprilTag);
        visionPortal = builder.build();

        if (USE_WEBCAM) setManualExposure(6, 250);
    }

    private void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) return;
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            tele.addData("Camera", "Waiting");
            tele.update();
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 2000 &&
                    visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
        }
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
            exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        }
    }

    public void telemetryAprilTag() {
        List<AprilTagDetection> detections = (aprilTag != null) ? aprilTag.getDetections() : null;
        if (detections == null || detections.isEmpty()) {
            tele.addData("AprilTag", "No detections");
            return;
        }

        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null) {
                tele.addData("Tag ID", detection.id);
                tele.addData("Range", "%.1f in", detection.ftcPose.range);
                tele.addData("Bearing", "%.1f deg", detection.ftcPose.bearing);
                tele.addData("Yaw", "%.1f deg", detection.ftcPose.yaw);
            } else {
                tele.addData("Unknown Tag", detection.id);
            }
        }
    }

    // ****** NEW FUNCTION: ATval ******
    public void ATval(int tagID) {
        if (aprilTag == null || aprilTag.getDetections() == null) {
            tele.addData("AprilTag", "Processor not initialized or no detections");
            return;
        }

        boolean found = false;

        for (AprilTagDetection tag : aprilTag.getDetections()) {
            if (tag.id == tagID) {
                found = true;

                double dist = tag.ftcPose.range;      // inches
                double yaw = tag.ftcPose.yaw;         // degrees
                double bearing = tag.ftcPose.bearing; // degrees

                tele.addData("Tag ID", tagID);
                tele.addData("Distance (in)", dist);
                tele.addData("Yaw (deg)", yaw);
                tele.addData("Bearing (deg)", bearing);

                ATdist = dist; // store for later use
                break;
            }
        }

        if (!found) {
            tele.addData("AprilTag", "Tag ID " + tagID + " not detected");
        }
    }

    public double alignToTag(int tagID) {
    ATval(tagID);
    if (ATdist == 0) return 0; // no tag

    for (AprilTagDetection tag : aprilTag.getDetections()) {
        if (tag.id == tagID) {
            double bearing = tag.ftcPose.bearing;
            double rotatePower = Range.clip(bearing * 0.02, -0.5, 0.5);
            manualMove(0, 0, rotatePower);
            return rotatePower;
        }
    }
    return 0;
}

public double moveToATDist(int tagID, double desiredDistance) {
    ATval(tagID);
    if (ATdist == 0) return 0; // no tag

    for (AprilTagDetection tag : aprilTag.getDetections()) {
        if (tag.id == tagID) {
            double distance = tag.ftcPose.range;
            double drivePower = Range.clip((distance - desiredDistance) * 0.01, -0.5, 0.5);
            manualMove(0, drivePower, 0);
            return drivePower;
        }
    }
    return 0;
}

public void aprilLock(int tagID, double desiredDistance) {
    double rotatePower = alignToTag(tagID);
    double drivePower = moveToATDist(tagID, desiredDistance);

    tele.addData("Rotate", rotatePower);
    tele.addData("Drive", drivePower);
    tele.update();
}



}

package org.firstinspires.ftc.teamcode.cougears.util;

import com.bylazar.camerastream.PanelsCameraStream;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.PoseHistory;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.VisionPortal;

public class PanelsFeatures {

    // ==================== CONSTANTS ====================

    public static final double ROBOT_RADIUS = 9;

    // ==================== FIELDS ====================

    public Follower follower;
    public JoinedTelemetry joinedTelemetry;

    private final Style robotStyle = new Style("", "#3F51B5", 0.75);
    private final Style historyStyle = new Style("", "#4CAF50", 0.75);
    private final FieldManager field = PanelsField.INSTANCE.getField();
    private boolean cameraStreamActive = false;

    // ==================== CONSTRUCTOR ====================

    public PanelsFeatures(Follower follower, Telemetry telemetry) {
        this.follower = follower;
        joinedTelemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
        field.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
    }

    // ==================== TELEMETRY ====================

    public Telemetry getTelemetry() {
        return joinedTelemetry;
    }

    // ==================== CAMERA STREAM ====================

    private PanelsCameraStream cameraStream;

    public void startCameraStream(VisionPortal visionPortal) {
        if (visionPortal == null) return;
        cameraStream = PanelsCameraStream.INSTANCE;
        cameraStream.startStream(visionPortal, 30);
        cameraStreamActive = true;
    }

    public void stopCameraStream() {
        if (cameraStream != null) {
            cameraStream.stopStream();
            cameraStreamActive = false;
        }
    }

    public boolean isCameraStreamActive() {
        return cameraStreamActive;
    }

    // ==================== UPDATE ====================

    public void update() {
        draw();
        joinedTelemetry.update();
    }

    // ==================== DRAWING ====================

    public void drawOnlyCurrent() {
        drawRobot(follower.getPose(), robotStyle);
        field.update();
    }

    public void draw() {
        if (follower.getCurrentPath() != null) {
            drawPath(follower.getCurrentPath(), robotStyle);
            Pose closest = follower.getPointFromPath(
                    follower.getCurrentPath().getClosestPointTValue()
            );
            drawRobot(new Pose(closest.getX(), closest.getY(),
                    follower.getCurrentPath().getHeadingGoal(
                            follower.getCurrentPath().getClosestPointTValue()
                    )), robotStyle);
        }
        drawPoseHistory(follower.getPoseHistory(), historyStyle);
        drawRobot(follower.getPose(), robotStyle);
        field.update();
    }

    private void drawRobot(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX())
                || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) return;

        double halfW = (144 - 135.53125);
        double halfH = 8.625;
        double heading = pose.getHeading();
        double cosH = Math.cos(heading);
        double sinH = Math.sin(heading);

        double[][] localCorners = {
                { halfH,  halfW},
                { halfH, -halfW},
                {-halfH, -halfW},
                {-halfH,  halfW},
        };

        double[][] world = new double[4][2];
        for (int i = 0; i < 4; i++) {
            world[i][0] = pose.getX() + localCorners[i][0] * cosH - localCorners[i][1] * sinH;
            world[i][1] = pose.getY() + localCorners[i][0] * sinH + localCorners[i][1] * cosH;
        }

        field.setStyle(style);
        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;
            field.moveCursor(world[i][0], world[i][1]);
            field.line(world[next][0], world[next][1]);
        }

        double tipX = pose.getX() + halfH * cosH;
        double tipY = pose.getY() + halfH * sinH;
        double midX = pose.getX();
        double midY = pose.getY();
        double wingSize = halfW * 0.8;
        double wingX1 = midX + (-wingSize) * (-sinH);
        double wingY1 = midY + (-wingSize) * cosH;
        double wingX2 = midX + wingSize * (-sinH);
        double wingY2 = midY + wingSize * cosH;

        field.moveCursor(wingX1, wingY1);
        field.line(tipX, tipY);
        field.moveCursor(wingX2, wingY2);
        field.line(tipX, tipY);
        field.moveCursor(wingX1, wingY1);
        field.line(wingX2, wingY2);
    }

    private void drawPath(Path path, Style style) {
        double[][] points = path.getPanelsDrawingPoints();
        for (int i = 0; i < points[0].length; i++)
            for (int j = 0; j < points.length; j++)
                if (Double.isNaN(points[j][i])) points[j][i] = 0;

        field.setStyle(style);
        field.moveCursor(points[0][0], points[0][1]);
        field.line(points[1][0], points[1][1]);
    }

    private void drawPoseHistory(PoseHistory poseHistory, Style style) {
        field.setStyle(style);
        int size = poseHistory.getXPositionsArray().length;
        for (int i = 0; i < size - 1; i++) {
            field.moveCursor(poseHistory.getXPositionsArray()[i], poseHistory.getYPositionsArray()[i]);
            field.line(poseHistory.getXPositionsArray()[i + 1], poseHistory.getYPositionsArray()[i + 1]);
        }
    }
}
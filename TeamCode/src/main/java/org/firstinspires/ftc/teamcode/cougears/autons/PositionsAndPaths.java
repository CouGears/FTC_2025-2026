package org.firstinspires.ftc.teamcode.cougears.autons;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

import java.util.HashMap;

@Configurable

public class PositionsAndPaths {
    public static final double posError = -6.5;
    public static final double headingError = 10;

    // ==========================================================
    //                      RED POSITIONS
    // ==========================================================

    // --- Starting Poses ---
    public static Pose RedStartPos         = new Pose(104.16333333, 130.81666667, Math.toRadians(-90));
    public static Pose RedStartPosFar      = new Pose(94.07,   9.71, Math.toRadians(90));
    public static Pose RedAnchorPoint      = new Pose(135.53125, 8.625, Math.toRadians(0));

    // --- Shooting Poses ---
    public static Pose RedShootTriangleClose = new Pose(96.31,  99.16, Math.toRadians(43.01));
    //93.06,96.54,41.52/92.47,96.48,42.74/92.67,95.31,45.16/91.04,96.21,43.00
    public static Pose RedShootTriangleTip   = new Pose(75.925,  71.235, Math.toRadians(46.455));
    //74.51,71.93,46.10/76.57,72.21,45.20/76.87,69.88,47.31/75.75,70.92,47.21
    public static Pose RedShootWall          = new Pose(80.3,  121.1, Math.toRadians(12.8));
    public static Pose RedShootCenterZone    = new Pose(82.6175,  120.3, Math.toRadians(19.99));
    // 82.80,116.28,20.38/83.62,115.30,20.42/82.15,115.14,19.23/81.90,11.41,19.93

    public static Pose RedShootFar           = new Pose(86.0075,  28.3925, Math.toRadians(64.9225));
    //85.19,28.72,64.96/85.42,28.83,63.77/87.12,27.97,65.66/86.30,28.05,65.30

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static Pose RedBallDepotStart1 = new Pose(102.325,  37.12 + 48,  Math.toRadians(0));
    public static Pose RedBallDepotStart2 = new Pose(102.325,  37.12 + 24,  Math.toRadians(0));
    public static Pose RedBallDepotStart3 = new Pose(102.325,  37.12,       Math.toRadians(0));
    //106.58,35.73/105.44,35.20/106.52,35.38/106.76,35.66/
    public static Pose RedBallDepotEnd1   = new Pose(126.1075, 37.12 + 48,  Math.toRadians(0));
    //127.21/128.12/128.10/129.000/
    public static Pose RedBallDepotEnd2   = new Pose(132.105, 37.12 + 24,  Math.toRadians(0));
    public static Pose RedBallDepotEnd3   = new Pose(132.105, 37.12,       Math.toRadians(0));
    //132.9/131.26/133.06/131.2/

    // --- Gate, Park, and Utility ---
    public static Pose RedGateInit        = new Pose(114.1825, 62.7775,  Math.toRadians(-90));
    //115.51,63.12/114.93,62.85/113.57,62.62/112.72,62.52/
    public static Pose RedGateOpen        = new Pose(119.195, 62.7775,   Math.toRadians(-90));
    //120.50/119.95/117.78/118.55/
    public static Pose RedGatePickupOpen  = new Pose(134.67, 65.5,  Math.toRadians(28.27));
    public static Pose RedGatePickupInit  = new Pose(127.73, 65.5,  Math.toRadians(28.27));
    public static Pose RedBasicEndClose   = new Pose(105,  77.15, Math.toRadians(45));
    public static Pose RedBasicEndFar     = new Pose(94.5 + 20,  18.64, Math.toRadians(45));
    public static Pose RedPark            = new Pose(44.649, 17.33, Math.toRadians(0));


    // ==========================================================
    //             BLUE POSITIONS (144-x, 180-heading)
    // ==========================================================

    // --- Starting Poses ---
    public static Pose BlueStartPos         = new Pose(12.5, 113.13, Math.toRadians(180 + headingError - 45));
    public static Pose BlueStartPosFar      = new Pose(144 + posError  - 96.07, 9.71, Math.toRadians(180 + headingError - 90));

    // --- Shooting Poses ---
    public static Pose BlueShootTriangleClose = new Pose(144 + posError  - 91.55, 85.3, Math.toRadians(180 + headingError - 50.6));
    public static Pose BlueShootTriangleTip   = new Pose(144 + posError  - 78.7, 59.2, Math.toRadians(180 + headingError - 53.93));
    public static Pose BlueShootWall          = new Pose(144 + posError  - 80.3, 121.1, Math.toRadians(180 + headingError - 12.8));
    public static Pose BlueShootCenterZone    = new Pose(144 + posError  - 80.12, 105.27, Math.toRadians(180 + headingError - 29.9));
    public static Pose BlueShootFar           = new Pose(144 + posError  - 94.5, 18.64, Math.toRadians(180 + headingError - 71.2));

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static Pose BlueBallDepotStart1 = new Pose(144 + posError  - 108.1, 27.63 + 48, Math.toRadians(180 + headingError - 0));
    public static Pose BlueBallDepotStart2 = new Pose(144 + posError  - 103.1, 27.63 + 24, Math.toRadians(180 + headingError - 0));
    public static Pose BlueBallDepotStart3 = new Pose(144 + posError  - 103.1, 27.63,      Math.toRadians(180 + headingError - 0));
    public static Pose BlueBallDepotEnd1   = new Pose(144 + posError  - 132.25, 27.63 + 48, Math.toRadians(180 + headingError - 0));
    public static Pose BlueBallDepotEnd2   = new Pose(144 + posError  - 132.25, 27.63 + 24, Math.toRadians(180 + headingError - 0));
    public static Pose BlueBallDepotEnd3   = new Pose(144 + posError  - 132.25, 27.63,      Math.toRadians(180 + headingError - 0));

    // --- Gate, Park, and Utility ---
    public static Pose BlueGateInit        = new Pose(144 + posError  - 128, 72.4+5, Math.toRadians(180 + headingError - 90));
    public static Pose BlueGateOpen        = new Pose(144 + posError  - 131.6, 72.4+5, Math.toRadians(180 + headingError - 90));
    public static Pose BlueBasicEndClose   = new Pose(144 + posError  - 105, 77.15, Math.toRadians(180 + headingError - 45));
    public static Pose BlueBasicEndFar     = new Pose(144 + posError  - 105,  15, Math.toRadians(45));
    public static Pose BluePark            = new Pose(101.79, 143.91, Math.toRadians(90));


    // ==========================================================
    //                    SHOOTING DATA
    // ==========================================================
    public static ShootingPosition RedWall = new ShootingPosition(RedShootWall, 1160, "Red");
    public static ShootingPosition RedCenterZone = new ShootingPosition(RedShootCenterZone, 1160, "Red");
    public static ShootingPosition RedTriangleTip = new ShootingPosition(RedShootTriangleTip, 1280, "Red");
    public static ShootingPosition RedTriangleClose = new ShootingPosition(RedShootTriangleClose, 1190, "Red");
    public static ShootingPosition RedFar = new ShootingPosition(RedShootFar, 1480, "Red");
    public static HashMap<String, ShootingPosition> redShootingPosHashMap= new HashMap<String, ShootingPosition>() {{
        put("RedWall", RedWall);
        put("RedCenterZone", RedCenterZone);
        put("RedTriangleTip", RedTriangleTip);
        put("RedTriangleClose", RedTriangleClose);
        put("RedFar", RedFar);
    }};
    public static ShootingPosition[] redShootingPosArray = new ShootingPosition[]{RedWall, RedCenterZone, RedTriangleClose, RedTriangleTip, RedFar};

    public static ShootingPosition BlueWall = new ShootingPosition(BlueShootWall, 1160, "Blue");
    public static ShootingPosition BlueCenterZone = new ShootingPosition(BlueShootCenterZone, 1160, "Blue");
    public static ShootingPosition BlueTriangleTip = new ShootingPosition(BlueShootTriangleTip, 1280, "Blue");
    public static ShootingPosition BlueTriangleClose = new ShootingPosition(BlueShootTriangleClose, 1190, "Blue");
    public static ShootingPosition BlueFar = new ShootingPosition(BlueShootFar, 1480, "Blue");
    public static HashMap<String, ShootingPosition> blueShootingPosHashMap= new HashMap<String, ShootingPosition>() {{
        put("BlueWall", BlueWall);
        put("BlueCenterZone", BlueCenterZone);
        put("BlueTriangleTip", BlueTriangleTip);
        put("BlueTriangleClose", BlueTriangleClose);
        put("BlueFar", BlueFar);
    }};
    public static ShootingPosition[] blueShootingPosArray = new ShootingPosition[]{BlueWall, BlueCenterZone, BlueTriangleClose, BlueTriangleTip, BlueFar};


    // ==========================================================
    //                      PATHCHAINS
    // ==========================================================

    // --- Red PathChains ---


    // ==========================================================
    //                      BUILDER LOGIC
    // ==========================================================
    public static void buildPaths(Follower f) {

    }

    // --- Helpers ---
    public static PathChain buildPath(Follower f, Pose start, Pose end) {
        return f.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }

    public static PathChain buildLongPath(Follower f, Pose... steps) {
        PathBuilder builder = f.pathBuilder();
        for (int i = 0; i + 1 < steps.length; i++) {
            builder.addPath(new BezierLine(steps[i], steps[i+1]))
                    .setLinearHeadingInterpolation(steps[i].getHeading(), steps[i+1].getHeading());
        }
        return builder.build();
    }
}
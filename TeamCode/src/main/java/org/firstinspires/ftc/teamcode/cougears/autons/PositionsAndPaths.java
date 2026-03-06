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
    public static double posError = -12;
    public static double headingError = 10;

    // ==========================================================
    //                      RED POSITIONS
    // ==========================================================

    // --- Starting Poses ---
    public static Pose RedStartPos         = new Pose(104.16333333, 130.81666667, Math.toRadians(-90));
    public static Pose RedStartPosFar      = new Pose(80.9475,   6.8775, Math.toRadians(90));
    //79.35,8.01/78.49,7.83/83.19,5.57/82.76,6.1
    public static Pose RedAnchorPoint      = new Pose(135.53125, 8.625, Math.toRadians(0));

    // --- Shooting Poses ---
    public static Pose RedShootTriangleClose = new Pose(88.31,  91.16, 0.7606661113);
    //93.06,96.54,41.52/92.47,96.48,42.74/92.67,95.31,45.16/91.04,96.21,43.00
    public static Pose RedShootTriangleTip   = new Pose(75.925,  71.235, Math.toRadians(46.455));
    //74.51,71.93,46.10/76.57,72.21,45.20/76.87,69.88,47.31/75.75,70.92,47.21
    public static Pose RedShootWall          = new Pose(80.3,  121.1, Math.toRadians(12.8));
    public static Pose RedShootCenterZone    = new Pose(82.6175,  120.3, Math.toRadians(19.99));
    // 82.80,116.28,20.38/83.62,115.30,20.42/82.15,115.14,19.23/81.90,11.41,19.93

    public static Pose RedShootFar           = new Pose(75.855,  12.005, Math.toRadians(66.523));
    //75.51,11.77/73.35,12.61,66.08/76.90,12.09,66.11/77.66,11.55,67.38/

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static Pose RedBallDepotStart1 = new Pose(94.325,  84.29,  Math.toRadians(0));
    public static Pose RedBallDepotStart2 = new Pose(94.325,  56.834,  Math.toRadians(0));
    public static Pose RedBallDepotStart3 = new Pose(94.325,  36.12,   Math.toRadians(0));
    //106.58,35.73/105.44,35.20/106.52,35.38/106.76,35.66/
    public static Pose RedBallDepotEnd1   = new Pose(120.1075, 83.29,  Math.toRadians(0));
    //127.21/128.12/128.10/129.000/
    public static Pose RedBallDepotEnd2   = new Pose(124.105, 56.834,  Math.toRadians(0));
    public static Pose RedBallDepotEnd3   = new Pose(125.105, 36.12,   Math.toRadians(0));
    //132.9/131.26/133.06/131.2/

    // --- Gate, Park, and Utility ---
    public static Pose RedGateInit        = new Pose(118.4, 57.834,  Math.toRadians(-45.0));
    //115.51,63.12/114.93,62.85/113.57,62.62/112.72,62.52/
    public static Pose Driver_RedGateInit        = new Pose(120.395, 67.834,  Math.toRadians(-90));
    public static Pose Driver_RedGateOpen       = new Pose(123.4, 67.834,  Math.toRadians(-90));


    public static Pose RedGateOpen        = new Pose(122.595, 61.7775,   Math.toRadians(-90));
    //120.50/119.95/117.78/118.55/
    public static Pose RedGatePickupOpen  = new Pose(135.241, 65.4,  Math.toRadians(34.97));
    public static Pose RedGatePickupInit  = new Pose(129.43, 61.78,  Math.toRadians(34.97));
    public static Pose RedBasicEndClose   = new Pose(105,  77.15, Math.toRadians(45));
    public static Pose RedBasicEndFar     = new Pose(100,  10.5, Math.toRadians(90));
    public static Pose RedPark            = new Pose(44.649, 17.33, Math.toRadians(0));

    public static Pose RedHumanZone        = new Pose(120, 120, 0);
    public static Pose RedHumanZoneInit        = new Pose(115, 115, 0);

    // 42.05,31.13/42.07,31.25/39.13,32.79/38.62,30.08

// ==========================================================
    //             BLUE POSITIONS (Mirroring Red)
    // ==========================================================
    // Formula: BlueX = 135.53125 - RedX | BlueY = RedY | BlueHeading = 180 - RedHeading

    // --- Starting Poses ---
    public static Pose BlueStartPos         = new Pose(31.3679, 130.8167, Math.toRadians(270));
    public static Pose BlueStartPosFar      = new Pose(49.76125, 18.73, Math.toRadians(90));
    public static Pose BlueAnchorPoint      = new Pose(144-135.53125, 8.625, Math.toRadians(180));

    // --- Shooting Poses ---
    // RedShootTriangleClose was 0.7606 rad (~43.58 deg) -> Blue is 136.42 deg
    public static Pose BlueShootTriangleClose = new Pose(47.22125, 91.16, Math.toRadians(136.416));
    public static Pose BlueShootTriangleTip   = new Pose(59.60625, 71.235, Math.toRadians(133.545));
    public static Pose BlueShootWall          = new Pose(55.23125, 121.1, Math.toRadians(167.2));
    public static Pose BlueShootCenterZone    = new Pose(52.91375, 120.3, Math.toRadians(160.01));
    public static Pose BlueShootFar           = new Pose(49.52375, 28.3925, Math.toRadians(114.0775));

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static Pose BlueBallDepotStart1 = new Pose(41.20625, 84.29,  Math.toRadians(180));
    public static Pose BlueBallDepotStart2 = new Pose(41.20625, 56.834, Math.toRadians(180));
    public static Pose BlueBallDepotStart3 = new Pose(41.20625, 36.12,  Math.toRadians(180));

    public static Pose BlueBallDepotEnd1   = new Pose(15.42375, 83.29,  Math.toRadians(180));
    public static Pose BlueBallDepotEnd2   = new Pose(11.42625, 56.834, Math.toRadians(180));
    public static Pose BlueBallDepotEnd3   = new Pose(10.42625, 36.12,  Math.toRadians(180));

    // --- Gate, Park, and Utility ---
    public static Pose BlueGateInit        = new Pose(17.13125, 57.834,  Math.toRadians(225.0));
    public static Pose Driver_BlueGateInit = new Pose(15.13625, 67.834,  Math.toRadians(270));
    public static Pose Driver_BlueGateOpen = new Pose(12.13125, 67.834,  Math.toRadians(270));

    public static Pose BlueGateOpen        = new Pose(12.93625, 61.7775, Math.toRadians(270));
    public static Pose BlueGatePickupOpen  = new Pose(0.29025,  65.4,     Math.toRadians(145.03));
    public static Pose BlueGatePickupInit  = new Pose(6.10125,  61.78,    Math.toRadians(145.03));

    public static Pose BlueBasicEndClose   = new Pose(30.53125, 77.15,   Math.toRadians(135));
    public static Pose BlueBasicEndFar     = new Pose(21.03125, 18.64,   Math.toRadians(135));
    public static Pose BluePark            = new Pose(90.88225, 17.33,   Math.toRadians(180));

    public static Pose BlueHumanZone       = new Pose(15.53125, 120,     Math.toRadians(180));
    public static Pose BlueHumanZoneInit   = new Pose(20.53125, 115,     Math.toRadians(180));
    // 85.30,30.63/87.22,30.95/87.29,31.92


    // ==========================================================
    //                    SHOOTING DATA
    // ==========================================================
    public static ShootingPosition RedWall = new ShootingPosition(RedShootWall, 1160, "Red");
    public static ShootingPosition RedCenterZone = new ShootingPosition(RedShootCenterZone, 1162, "Red");
    public static ShootingPosition RedTriangleTip = new ShootingPosition(RedShootTriangleTip, 1280, "Red");
    public static ShootingPosition RedTriangleClose = new ShootingPosition(RedShootTriangleClose, 1170, "Red");
    public static ShootingPosition RedFar = new ShootingPosition(RedShootFar, 1455, "Red");
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
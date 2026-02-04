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
    public static Pose RedStartPos         = new Pose(144 - 19, 119.2, Math.toRadians(38));
    public static Pose RedStartPosFar      = new Pose(94.07,   9.71, Math.toRadians(90));

    // --- Shooting Poses ---
    public static Pose RedShootTriangleClose = new Pose(91.55,  85.3, Math.toRadians(45));
    public static Pose RedShootTriangleTip   = new Pose(78.7,  59.2, Math.toRadians(53.93));
    public static Pose RedShootWall          = new Pose(80.3,  121.1, Math.toRadians(12.8));
    public static Pose RedShootCenterZone    = new Pose(80.12,  105.27, Math.toRadians(29.9));
    public static Pose RedShootFar           = new Pose(94.5,  18.64, Math.toRadians(71.2));

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static Pose RedBallDepotStart1 = new Pose(108.1, 27.63 + 48,  Math.toRadians(0));
    public static Pose RedBallDepotStart2 = new Pose(108.1, 27.63 + 24,  Math.toRadians(0));
    public static Pose RedBallDepotStart3 = new Pose(108.1, 27.63,       Math.toRadians(0));
    public static Pose RedBallDepotEnd1   = new Pose(131.25, 27.63 + 48,  Math.toRadians(0));
    public static Pose RedBallDepotEnd2   = new Pose(138.25, 27.63 + 24,  Math.toRadians(0));
    public static Pose RedBallDepotEnd3   = new Pose(140.25, 27.63,       Math.toRadians(0));

    // --- Gate, Park, and Utility ---
    public static Pose RedGateInit        = new Pose(128, 72.4,  Math.toRadians(90));
    public static Pose RedGateOpen        = new Pose(131.6, 72.4,  Math.toRadians(90));
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
    public static Pose BluePark            = new Pose(101.517, 28.384, Math.toRadians(180));


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
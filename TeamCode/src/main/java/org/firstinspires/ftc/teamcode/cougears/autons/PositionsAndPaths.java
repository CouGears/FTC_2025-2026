package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

import java.util.HashMap;

public class PositionsAndPaths {

    // ==========================================================
    //                      RED POSITIONS
    // ==========================================================

    // --- Starting Poses ---
    public static final Pose RedStartPos         = new Pose(144 - 19, 119.2, Math.toRadians(45));
    public static final Pose RedStartPosFar      = new Pose(94.07,   9.71, Math.toRadians(90));

    // --- Shooting Poses ---
    public static final Pose RedShootTriangleClose = new Pose(91.55,  85.3, Math.toRadians(50.6));
    public static final Pose RedShootTriangleTip   = new Pose(78.7,  59.2, Math.toRadians(53.93));
    public static final Pose RedShootWall          = new Pose(80.3,  121.1, Math.toRadians(12.8));
    public static final Pose RedShootCenterZone    = new Pose(80.12,  105.27, Math.toRadians(29.9));
    public static final Pose RedShootFar           = new Pose(94.5,  18.64, Math.toRadians(71.2));

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static final Pose RedBallDepotStart1 = new Pose(108.1, 27.63 + 48,  Math.toRadians(0));
    public static final Pose RedBallDepotStart2 = new Pose(108.1, 27.63 + 24,  Math.toRadians(0));
    public static final Pose RedBallDepotStart3 = new Pose(108.1, 27.63,       Math.toRadians(0));
    public static final Pose RedBallDepotEnd1   = new Pose(132.25, 27.63 + 48,  Math.toRadians(0));
    public static final Pose RedBallDepotEnd2   = new Pose(132.25, 27.63 + 24,  Math.toRadians(0));
    public static final Pose RedBallDepotEnd3   = new Pose(132.25, 27.63,       Math.toRadians(0));

    // --- Gate, Park, and Utility ---
    public static final Pose RedGateInit        = new Pose(128, 72.4,  Math.toRadians(90));
    public static final Pose RedGateOpen        = new Pose(131.6, 72.4,  Math.toRadians(90));
    public static final Pose RedBasicEndClose   = new Pose(105,  77.15, Math.toRadians(45));
    public static final Pose RedBasicEndFar     = new Pose(96.07,  25.71, Math.toRadians(45));
    public static final Pose RedPark            = new Pose(113.26, 32.68, Math.toRadians(90));


    // ==========================================================
    //             BLUE POSITIONS (144-x, 180-heading)
    // ==========================================================

    // --- Starting Poses ---
    public static final Pose BlueStartPos         = new Pose(144 - (144 - 19), 119.2, Math.toRadians(180 - 45));
    public static final Pose BlueStartPosFar      = new Pose(144 - 96.07, 9.71, Math.toRadians(180 - 90));

    // --- Shooting Poses ---
    public static final Pose BlueShootTriangleClose = new Pose(144 - 91.55, 85.3, Math.toRadians(180 - 50.6));
    public static final Pose BlueShootTriangleTip   = new Pose(144 - 78.7, 59.2, Math.toRadians(180 - 53.93));
    public static final Pose BlueShootWall          = new Pose(144 - 80.3, 121.1, Math.toRadians(180 - 12.8));
    public static final Pose BlueShootCenterZone    = new Pose(144 - 80.12, 105.27, Math.toRadians(180 - 29.9));
    public static final Pose BlueShootFar           = new Pose(144 - 94.5, 18.64, Math.toRadians(180 - 71.2));

    // --- Ball Depot Poses (Pickup & Scoring) ---
    public static final Pose BlueBallDepotStart1 = new Pose(144 - 108.1, 27.63 + 48, Math.toRadians(180 - 0));
    public static final Pose BlueBallDepotStart2 = new Pose(144 - 108.1, 27.63 + 24, Math.toRadians(180 - 0));
    public static final Pose BlueBallDepotStart3 = new Pose(144 - 108.1, 27.63,      Math.toRadians(180 - 0));
    public static final Pose BlueBallDepotEnd1   = new Pose(144 - 132.25, 27.63 + 48, Math.toRadians(180 - 0));
    public static final Pose BlueBallDepotEnd2   = new Pose(144 - 132.25, 27.63 + 24, Math.toRadians(180 - 0));
    public static final Pose BlueBallDepotEnd3   = new Pose(144 - 132.25, 27.63,      Math.toRadians(180 - 0));

    // --- Gate, Park, and Utility ---
    public static final Pose BlueGateInit        = new Pose(144 - 128, 72.4, Math.toRadians(180 - 90));
    public static final Pose BlueGateOpen        = new Pose(144 - 131.6, 72.4, Math.toRadians(180 - 90));
    public static final Pose BlueBasicEndClose = new Pose(144 - 105, 77.15, Math.toRadians(180 - 45));
    public static final Pose BlueBasicEndFar     = new Pose(144 - 96.07,  25.71, Math.toRadians(45));
    public static final Pose BluePark            = new Pose(144 - 113.26, 32.68, Math.toRadians(180 - 90));


    // ==========================================================
    //                    SHOOTING DATA
    // ==========================================================
    public static ShootingPosition RedWall = new ShootingPosition(RedShootWall, 1160, "Red");
    public static ShootingPosition RedCenterZone = new ShootingPosition(RedShootCenterZone, 1160, "Red");
    public static ShootingPosition RedTriangleTip = new ShootingPosition(RedShootTriangleTip, 1280, "Red");
    public static ShootingPosition RedTriangleClose = new ShootingPosition(RedShootTriangleClose, 1180, "Red");
    public static ShootingPosition RedFar = new ShootingPosition(RedShootFar, 1500, "Red");
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
    public static ShootingPosition BlueTriangleClose = new ShootingPosition(BlueShootTriangleClose, 1180, "Blue");
    public static ShootingPosition BlueFar = new ShootingPosition(BlueShootFar, 1450, "Blue");
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
    public static PathChain RedStartPosToRedShootTrianglePos;
    public static PathChain RedStartPosToRedShootWallPos;
    public static PathChain RedShootPosToRedBallDepotStart1;
    public static PathChain RedBallDepotStart1ToRedBallDepotEnd1;
    public static PathChain RedBallDepotEnd1ToRedShootPos;
    public static PathChain RedShootPosToRedGateInit;
    public static PathChain RedGateInitToRedGateOpen;
    public static PathChain RedGateOpenToRedBallDepotStart1;
    public static PathChain RedBallDepotStart1ToRedBallDepotStart2;
    public static PathChain RedBallDepotStart2ToRedBallDepotEnd2;
    public static PathChain RedBallDepotEnd2ToRedShootPos;
    public static PathChain RedShootPosToRedBallDepotStart3;
    public static PathChain RedBallDepotStart3ToRedBallDepotEnd3;
    public static PathChain RedBallDepotEnd3ToRedShootPos;
    public static PathChain RedShootPosToRedSweepStart;
    public static PathChain RedSweepStartToRedSweepEnd;
    public static PathChain RedSweepEndToRedShootPos;
    public static PathChain RedShootTrianglePosToRedBasicEnd;
    public static PathChain RedShootWallPosToRedBasicEnd;
    public static PathChain RedShootTrianglePosToBallDepot1Pickup;
    public static PathChain RedFarStartPosToRedShootWallPos;

    // --- Blue PathChains ---
    public static PathChain BlueStartPosToBlueShootTrianglePos;
    public static PathChain BlueStartPosToBlueShootWallPos;
    public static PathChain BlueShootPosToBlueBallDepotStart1;
    public static PathChain BlueBallDepotStart1ToBlueBallDepotEnd1;
    public static PathChain BlueBallDepotEnd1ToBlueShootPos;
    public static PathChain BlueShootPosToBlueGateInit;
    public static PathChain BlueGateInitToBlueGateOpen;
    public static PathChain BlueGateOpenToBlueBallDepotStart1;
    public static PathChain BlueBallDepotStart1ToBlueBallDepotStart2;
    public static PathChain BlueBallDepotStart2ToBlueBallDepotEnd2;
    public static PathChain BlueBallDepotEnd2ToBlueShootPos;
    public static PathChain BlueShootPosToBlueBallDepotStart3;
    public static PathChain BlueBallDepotStart3ToBlueBallDepotEnd3;
    public static PathChain BlueBallDepotEnd3ToBlueShootPos;
    public static PathChain BlueShootPosToBlueSweepStart;
    public static PathChain BlueSweepStartToBlueSweepEnd;
    public static PathChain BlueSweepEndToBlueShootPos;
    public static PathChain BlueShootTrianglePosToBlueBasicEnd;
    public static PathChain BlueShootWallPosToBlueBasicEnd;
    public static PathChain BlueShootTrianglePosToBallDepot1Pickup;
    public static PathChain BlueFarStartPosToBlueShootWallPos;


    // ==========================================================
    //                      BUILDER LOGIC
    // ==========================================================
    public static void buildPaths(Follower f) {

        // ---- RED PATHS ----
        RedStartPosToRedShootTrianglePos       = buildPath(f, RedStartPos, RedShootTriangleTip);
        RedStartPosToRedShootWallPos           = buildPath(f, RedStartPos, RedShootWall);
        RedShootPosToRedBallDepotStart1        = buildPath(f, RedShootTriangleTip, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotEnd1   = buildPath(f, RedBallDepotStart1, RedBallDepotEnd1);
        RedBallDepotEnd1ToRedShootPos          = buildPath(f, RedBallDepotEnd1, RedShootTriangleTip);
        RedShootPosToRedGateInit               = buildPath(f, RedShootTriangleTip, RedGateInit);
        RedGateInitToRedGateOpen               = buildPath(f, RedGateInit, RedGateOpen);
        RedGateOpenToRedBallDepotStart1        = buildPath(f, RedGateOpen, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotStart2 = buildPath(f, RedBallDepotStart1, RedBallDepotStart2);
        RedBallDepotStart2ToRedBallDepotEnd2   = buildPath(f, RedBallDepotStart2, RedBallDepotEnd2);
        RedBallDepotEnd2ToRedShootPos          = buildPath(f, RedBallDepotEnd2, RedShootTriangleTip);
        RedShootPosToRedBallDepotStart3        = buildPath(f, RedShootTriangleTip, RedBallDepotStart3);
        RedBallDepotStart3ToRedBallDepotEnd3   = buildPath(f, RedBallDepotStart3, RedBallDepotEnd3);
        RedBallDepotEnd3ToRedShootPos          = buildPath(f, RedBallDepotEnd3, RedShootTriangleTip);
        RedShootTrianglePosToRedBasicEnd       = buildPath(f, RedShootTriangleTip, RedBasicEndClose);
        RedShootWallPosToRedBasicEnd           = buildPath(f, RedShootWall, RedBasicEndClose);
        RedShootTrianglePosToBallDepot1Pickup  = buildLongPath(f, RedShootTriangleTip, RedBallDepotStart1, RedBallDepotEnd1);

        // ---- BLUE PATHS ----
        BlueStartPosToBlueShootTrianglePos       = buildPath(f, BlueStartPos, BlueShootTriangleTip);
        BlueStartPosToBlueShootWallPos           = buildPath(f, BlueStartPos, BlueShootWall);
        BlueShootPosToBlueBallDepotStart1        = buildPath(f, BlueShootTriangleTip, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotEnd1   = buildPath(f, BlueBallDepotStart1, BlueBallDepotEnd1);
        BlueBallDepotEnd1ToBlueShootPos          = buildPath(f, BlueBallDepotEnd1, BlueShootTriangleTip);
        BlueShootPosToBlueGateInit               = buildPath(f, BlueShootTriangleTip, BlueGateInit);
        BlueGateInitToBlueGateOpen               = buildPath(f, BlueGateInit, BlueGateOpen);
        BlueGateOpenToBlueBallDepotStart1        = buildPath(f, BlueGateOpen, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotStart2 = buildPath(f, BlueBallDepotStart1, BlueBallDepotStart2);
        BlueBallDepotStart2ToBlueBallDepotEnd2   = buildPath(f, BlueBallDepotStart2, BlueBallDepotEnd2);
        BlueBallDepotEnd2ToBlueShootPos          = buildPath(f, BlueBallDepotEnd2, BlueShootTriangleTip);
        BlueShootPosToBlueBallDepotStart3        = buildPath(f, BlueShootTriangleTip, BlueBallDepotStart3);
        BlueBallDepotStart3ToBlueBallDepotEnd3   = buildPath(f, BlueBallDepotStart3, BlueBallDepotEnd3);
        BlueBallDepotEnd3ToBlueShootPos          = buildPath(f, BlueBallDepotEnd3, BlueShootTriangleTip);
        BlueShootTrianglePosToBlueBasicEnd       = buildPath(f, BlueShootTriangleTip, BlueBasicEndClose);
        BlueShootWallPosToBlueBasicEnd           = buildPath(f, BlueShootWall, BlueBasicEndClose);
        BlueShootTrianglePosToBallDepot1Pickup   = buildLongPath(f, BlueShootTriangleTip, BlueBallDepotStart1, BlueBallDepotEnd1);

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
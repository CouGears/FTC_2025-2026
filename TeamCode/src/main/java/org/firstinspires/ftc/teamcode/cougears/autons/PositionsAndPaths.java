package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.randomVarConstant;

public class PositionsAndPaths {

    // ===== RED POSITIONS =====
    public static final Pose RedStartPos         = new Pose(144 - 21.543307086614174, 122.2047244094488, Math.toRadians(180 - 135));
    public static final Pose RedShootTrianglePos = new Pose(144 - 66.51968503937007,  77.85826771653544, Math.toRadians(180 - 135));
    public static final Pose RedShootWallPos     = new Pose(144 - 66.51968503937007,  130.0000000000000, Math.toRadians(180 - 180));

    public static final Pose RedBallDepotStart1 = new Pose(144 - 45.47819063004846, 83.74798061389338,  Math.toRadians(180 - 180));
    public static final Pose RedBallDepotStart2 = new Pose(144 - 45.47819063004846, 59.2,               Math.toRadians(180 - 180));
    public static final Pose RedBallDepotStart3 = new Pose(144 - 45.47819063004846, 35.592891760904685, Math.toRadians(180 - 180));

    public static final Pose RedBallDepotEnd1   = new Pose(144 - 12.794830371567043, 83.74798061389338,  Math.toRadians(180 - 180));
    public static final Pose RedBallDepotEnd2   = new Pose(144 - 12.794830371567043, 59.2,               Math.toRadians(180));
    public static final Pose RedBallDepotEnd3   = new Pose(144 - 12.794830371567043, 35.592891760904685, Math.toRadians(180));

    public static final Pose RedGateInit        = new Pose(144 - 17.21486268174475, 74.21001615508885,  Math.toRadians(180 - 180));
    public static final Pose RedGateOpen        = new Pose(144 - 17.21486268174475, 79.56058158319871,  Math.toRadians(180 - 180));

    public static final Pose RedSweepStart      = new Pose(144 - 7.444264943457189, 53.04038772213247,  Math.toRadians(180 - 260));
    public static final Pose RedSweepEnd        = new Pose(144 - 7.444264943457189, 17.8,               Math.toRadians(180 - 260));

    public static final Pose RedBasicEnd = new Pose(144 - 47.9579176252764,  144 - 61.95628968237839, Math.toRadians(-90));



    // ===== BLUE POSITIONS =====
    public static final Pose BlueStartPos       = new Pose(21.543307086614174, 122.20472440944881, Math.toRadians(135));
    public static final Pose BlueShootTrianglePos = new Pose(66.51968503937007,  77.85826771653544,  Math.toRadians(135));
    public static final Pose BlueShootWallPos     = new Pose(144,  130,  Math.toRadians(180));


    public static final Pose BlueBallDepotStart1 = new Pose(45.47819063004846, 83.74798061389338,  Math.toRadians(180));
    public static final Pose BlueBallDepotStart2 = new Pose(45.47819063004846, 59.2,                 Math.toRadians(180));
    public static final Pose BlueBallDepotStart3 = new Pose(45.47819063004846, 35.592891760904685, Math.toRadians(180));

    public static final Pose BlueBallDepotEnd1   = new Pose(12.794830371567043, 83.74798061389338,  Math.toRadians(180));
    public static final Pose BlueBallDepotEnd2   = new Pose(12.794830371567043, 59.2,                 Math.toRadians(180));
    public static final Pose BlueBallDepotEnd3   = new Pose(12.794830371567043, 35.592891760904685, Math.toRadians(180));

    public static final Pose BlueGateInit        = new Pose(17.21486268174475, 74.21001615508885,  Math.toRadians(180));
    public static final Pose BlueGateOpen        = new Pose(17.21486268174475, 79.56058158319871,  Math.toRadians(180));

    public static final Pose BlueSweepStart      = new Pose(7.444264943457189, 53.04038772213247,  Math.toRadians(260));
    public static final Pose BlueSweepEnd        = new Pose(7.444264943457189, 17.8,                Math.toRadians(260));

    public static final Pose BlueBasicEnd = new Pose(144,  144, Math.toRadians(90));

    // ===== RED PATHS =====
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



    // ===== BLUE PATHS =====
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


    // ===== BUILD ALL PATHS =====
    public static void buildPaths(Follower f) {

        // ---- RED PATHS ----
        RedStartPosToRedShootTrianglePos       = buildPath(f, RedStartPos, RedShootTrianglePos);
        RedStartPosToRedShootWallPos           = buildPath(f, RedStartPos, RedShootWallPos);
        RedShootPosToRedBallDepotStart1        = buildPath(f, RedShootTrianglePos, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotEnd1   = buildPath(f, RedBallDepotStart1, RedBallDepotEnd1);
        RedBallDepotEnd1ToRedShootPos          = buildPath(f, RedBallDepotEnd1, RedShootTrianglePos);
        RedShootPosToRedGateInit               = buildPath(f, RedShootTrianglePos, RedGateInit);
        RedGateInitToRedGateOpen               = buildPath(f, RedGateInit, RedGateOpen);
        RedGateOpenToRedBallDepotStart1        = buildPath(f, RedGateOpen, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotStart2 = buildPath(f, RedBallDepotStart1, RedBallDepotStart2);
        RedBallDepotStart2ToRedBallDepotEnd2   = buildPath(f, RedBallDepotStart2, RedBallDepotEnd2);
        RedBallDepotEnd2ToRedShootPos          = buildPath(f, RedBallDepotEnd2, RedShootTrianglePos);
        RedShootPosToRedBallDepotStart3        = buildPath(f, RedShootTrianglePos, RedBallDepotStart3);
        RedBallDepotStart3ToRedBallDepotEnd3   = buildPath(f, RedBallDepotStart3, RedBallDepotEnd3);
        RedBallDepotEnd3ToRedShootPos          = buildPath(f, RedBallDepotEnd3, RedShootTrianglePos);
        RedShootPosToRedSweepStart             = buildPath(f, RedShootTrianglePos, RedSweepStart);
        RedSweepStartToRedSweepEnd             = buildPath(f, RedSweepStart, RedSweepEnd);
        RedSweepEndToRedShootPos               = buildPath(f, RedSweepEnd, RedShootTrianglePos);
        RedShootTrianglePosToRedBasicEnd       = buildPath(f, RedShootTrianglePos, RedBasicEnd);
        RedShootWallPosToRedBasicEnd           = buildPath(f, RedShootWallPos, RedBasicEnd);
        RedShootTrianglePosToBallDepot1Pickup  = buildLongPath(f, RedShootTrianglePos, RedBallDepotStart1, RedBallDepotEnd1);
        // ---- BLUE PATHS ----
        BlueStartPosToBlueShootTrianglePos       = buildPath(f, BlueStartPos, BlueShootTrianglePos);
        BlueStartPosToBlueShootWallPos           = buildPath(f, BlueStartPos, BlueShootWallPos);
        BlueShootPosToBlueBallDepotStart1        = buildPath(f, BlueShootTrianglePos, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotEnd1   = buildPath(f, BlueBallDepotStart1, BlueBallDepotEnd1);
        BlueBallDepotEnd1ToBlueShootPos          = buildPath(f, BlueBallDepotEnd1, BlueShootTrianglePos);
        BlueShootPosToBlueGateInit               = buildPath(f, BlueShootTrianglePos, BlueGateInit);
        BlueGateInitToBlueGateOpen               = buildPath(f, BlueGateInit, BlueGateOpen);
        BlueGateOpenToBlueBallDepotStart1        = buildPath(f, BlueGateOpen, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotStart2 = buildPath(f, BlueBallDepotStart1, BlueBallDepotStart2);
        BlueBallDepotStart2ToBlueBallDepotEnd2   = buildPath(f, BlueBallDepotStart2, BlueBallDepotEnd2);
        BlueBallDepotEnd2ToBlueShootPos          = buildPath(f, BlueBallDepotEnd2, BlueShootTrianglePos);
        BlueShootPosToBlueBallDepotStart3        = buildPath(f, BlueShootTrianglePos, BlueBallDepotStart3);
        BlueBallDepotStart3ToBlueBallDepotEnd3   = buildPath(f, BlueBallDepotStart3, BlueBallDepotEnd3);
        BlueBallDepotEnd3ToBlueShootPos          = buildPath(f, BlueBallDepotEnd3, BlueShootTrianglePos);
        BlueShootPosToBlueSweepStart             = buildPath(f, BlueShootTrianglePos, BlueSweepStart);
        BlueSweepStartToBlueSweepEnd             = buildPath(f, BlueSweepStart, BlueSweepEnd);
        BlueSweepEndToBlueShootPos               = buildPath(f, BlueSweepEnd, BlueShootTrianglePos);
        BlueShootTrianglePosToBlueBasicEnd       = buildPath(f, BlueShootTrianglePos, BlueBasicEnd);
        BlueShootWallPosToBlueBasicEnd           = buildPath(f, BlueShootWallPos, BlueBasicEnd);
        BlueShootTrianglePosToBallDepot1Pickup   = buildLongPath(f, BlueShootTrianglePos, BlueBallDepotStart1, BlueBallDepotEnd1);
    }

    // ===== HELPER =====
    // EX: PathChain path = buildPath(follower, RedStart, RedShoot)
    public static PathChain buildPath(Follower f, Pose start, Pose end) {
        return f.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }
    // EX: PathChain path = buildLongPath(follower, RedStartPos, RedIntermidiaryPos1, RedIntermidiaryPos2, RedShootPos)
    public static PathChain buildLongPath(Follower f, Pose... steps) {
        PathBuilder builder = f.pathBuilder();
            for (int i = 0; i+1 < steps.length; i++) {
                builder
                .addPath(new BezierLine(steps[i], steps[i+1]))
                .setLinearHeadingInterpolation(steps[i].getHeading(), steps[i+1].getHeading());
            }
            return builder.build();
    }
}

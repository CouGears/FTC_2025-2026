package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

public class PositionsAndPaths {

    // ===== RED POSITIONS =====
    public static final Pose RedStartPos         = new Pose(144 - 19, 119.2, Math.toRadians(180 - 135));
    public static final Pose RedStartPosFar      = new Pose(144 - 56,   8, Math.toRadians(90));
    public static final Pose RedShootTriangleClose = new Pose(144 - 57.5,  78, Math.toRadians(180 - 135));
    public static final Pose RedShootTriangleTip = new Pose(144 - 57.5,  78, Math.toRadians(180 - 135));
    public static final Pose RedShootWall     = new Pose(144 - 58.5,  120, Math.toRadians(180 - 180));
    public static final Pose RedShootCenterZone     = new Pose(144 - 58.5,  120, Math.toRadians(180 - 180));
    public static final Pose RedShootFar     = new Pose(144 - 58.5,  120, Math.toRadians(180 - 180));


    public static final Pose RedBallDepotStart1 = new Pose(144 - 45.5, 84,  Math.toRadians(180 - 180));
    public static final Pose RedBallDepotStart2 = new Pose(144 - 45.5, 59,               Math.toRadians(180 - 180));
    public static final Pose RedBallDepotStart3 = new Pose(144 - 45.5, 35.5, Math.toRadians(180 - 180));

    public static final Pose RedBallDepotEnd1   = new Pose(144 - 13, 84,  Math.toRadians(180 - 180));
    public static final Pose RedBallDepotEnd2   = new Pose(144 - 13, 59,               Math.toRadians(180));
    public static final Pose RedBallDepotEnd3   = new Pose(144 - 13, 35.5, Math.toRadians(180));

    public static final Pose RedGateInit        = new Pose(144 - 17, 74,  Math.toRadians(180 - 180));
    public static final Pose RedGateOpen        = new Pose(144 - 17, 79.5,  Math.toRadians(180 - 180));

    public static final Pose RedSweepStart      = new Pose(144 - 7.5, 53,  Math.toRadians(180 - 260));
    public static final Pose RedSweepEnd        = new Pose(144 - 7.5, 18,               Math.toRadians(180 - 260));

    public static final Pose RedBasicEnd = new Pose(144 - 48,  144 - 70, Math.toRadians(-90));

    public static final Pose RedFarToWallIntermidate1 = new Pose(144 - 45, 60, Math.toRadians(90));
    public static final Pose RedFarToWallIntermidate2 = new Pose(144 - 45, 105, Math.toRadians(90));

    public static final Pose RedPark = new Pose(37.686591276252024, 32.568659127625196, Math.toRadians(180));




    // ===== BLUE POSITIONS =====
    public static final Pose BlueStartPos         = new Pose(19, 119.2, Math.toRadians(135));
    public static final Pose BlueStartPosFar      = new Pose(56,   8, Math.toRadians(90));
    public static final Pose BlueShootTrianglePos = new Pose(57.5,  78,  Math.toRadians(135));
    public static final Pose BlueShootWallPos     = new Pose(58.5,  120,  Math.toRadians(180));

    public static final Pose BlueBallDepotStart1 = new Pose(45.5, 84,  Math.toRadians(180));
    public static final Pose BlueBallDepotStart2 = new Pose(45.5, 59,                 Math.toRadians(180));
    public static final Pose BlueBallDepotStart3 = new Pose(45.5, 35.5, Math.toRadians(180));

    public static final Pose BlueBallDepotEnd1   = new Pose(13, 84,  Math.toRadians(180));
    public static final Pose BlueBallDepotEnd2   = new Pose(13, 59,                 Math.toRadians(180));
    public static final Pose BlueBallDepotEnd3   = new Pose(13, 35.5, Math.toRadians(180));

    public static final Pose BlueGateInit        = new Pose(17, 74.21001615508885,  Math.toRadians(180));
    public static final Pose BlueGateOpen        = new Pose(17, 79.56058158319871,  Math.toRadians(180));

    public static final Pose BlueSweepStart      = new Pose(7.5, 53,  Math.toRadians(260));
    public static final Pose BlueSweepEnd        = new Pose(7.5, 18,                Math.toRadians(260));

    public static final Pose BlueBasicEnd = new Pose(48,  70, Math.toRadians(90));

    public static final Pose BlueFarToWallIntermidate1 = new Pose(45, 60, Math.toRadians(90));
    public static final Pose BlueFarToWallIntermidate2 = new Pose(45, 105, Math.toRadians(90));

    public static final Pose BluePark = new Pose(144-37.686591276252024, 32.568659127625196, Math.toRadians(0));


    public static ShootingPosition RedWall = new ShootingPosition(RedShootWall, 1160);
    public static ShootingPosition RedCenterZone = new ShootingPosition(RedShootCenterZone, 1160);
    public static ShootingPosition RedTriangleTip = new ShootingPosition(RedShootTriangleTip, 1180);
    public static ShootingPosition RedTriangleClose = new ShootingPosition(RedShootTriangleClose, 1280);
    public static ShootingPosition RedFar = new ShootingPosition(RedShootFar, 1450);
    public static ShootingPosition[] shootingPosArray = {RedWall, RedCenterZone, RedTriangleTip, RedFar};



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
    public static PathChain RedFarStartPosToRedShootWallPos;



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
    public static PathChain BlueFarStartPosToBlueShootWallPos;


    // ===== BUILD ALL PATHS =====
    public static void buildPaths(Follower f) {

        // ---- RED PATHS ----
        RedStartPosToRedShootTrianglePos       = buildPath(f, RedStartPos, RedShootTriangleTip);
        RedStartPosToRedShootWallPos           = buildPath(f, RedStartPos, RedShootTriangleTip);
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
        RedShootPosToRedSweepStart             = buildPath(f, RedShootTriangleTip, RedSweepStart);
        RedSweepStartToRedSweepEnd             = buildPath(f, RedSweepStart, RedSweepEnd);
        RedSweepEndToRedShootPos               = buildPath(f, RedSweepEnd, RedShootTriangleTip);
        RedShootTrianglePosToRedBasicEnd       = buildPath(f, RedShootTriangleTip, RedBasicEnd);
        RedShootWallPosToRedBasicEnd           = buildPath(f, RedShootWall, RedBasicEnd);
        RedShootTrianglePosToBallDepot1Pickup  = buildLongPath(f, RedShootTriangleTip, RedBallDepotStart1, RedBallDepotEnd1);
        RedFarStartPosToRedShootWallPos        = buildLongPath(f, RedStartPosFar, RedFarToWallIntermidate1, RedFarToWallIntermidate2, RedShootTriangleTip);

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
        BlueFarStartPosToBlueShootWallPos        = buildLongPath(f, BlueStartPosFar, BlueFarToWallIntermidate1, BlueFarToWallIntermidate2, BlueShootWallPos);

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


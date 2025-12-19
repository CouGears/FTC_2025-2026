package org.firstinspires.ftc.teamcode.cougears.autons;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class PositionsAndPaths {

    // ===== RED POSITIONS =====
    static final Pose RedStartPos = new Pose(21.543307086614174, 122.20472440944881, Math.toRadians(135));
    static final Pose RedShootPos = new Pose(66.51968503937007, 77.85826771653544, Math.toRadians(135));
    static final Pose RedBallDepotStart1 = new Pose(40.47819063004846, 83.74798061389338, Math.toRadians(180));
    static final Pose RedBallDepotStart2 = new Pose(40.47819063004846, 59.2, Math.toRadians(180));
    static final Pose RedBallDepotStart3 = new Pose(40.47819063004846, 35.592891760904685, Math.toRadians(180));
    static final Pose RedBallDepotEnd1 = new Pose(12.794830371567043, 83.74798061389338, Math.toRadians(180));
    static final Pose RedBallDepotEnd2 = new Pose(12.794830371567043, 59.2, Math.toRadians(180));
    static final Pose RedBallDepotEnd3 = new Pose(12.794830371567043, 35.592891760904685, Math.toRadians(180));
    static final Pose RedGateInit = new Pose(17.21486268174475, 74.21001615508885, Math.toRadians(180));
    static final Pose RedGateOpen = new Pose(17.21486268174475, 79.56058158319871, Math.toRadians(180));
    static final Pose RedSweepStart = new Pose(7.444264943457189, 53.04038772213247, Math.toRadians(260));
    static final Pose RedSweepEnd = new Pose(7.444264943457189, 17.8, Math.toRadians(260));

    // ===== BLUE POSITIONS ===== (mirrored x = 144 - redX)
    static final Pose BlueStartPos = new Pose(144 - 21.543307086614174, 122.20472440944881, Math.toRadians(135));
    static final Pose BlueShootPos = new Pose(144 - 66.51968503937007, 77.85826771653544, Math.toRadians(135));
    static final Pose BlueBallDepotStart1 = new Pose(144 - 40.47819063004846, 83.74798061389338, Math.toRadians(180));
    static final Pose BlueBallDepotStart2 = new Pose(144 - 40.47819063004846, 59.2, Math.toRadians(180));
    static final Pose BlueBallDepotStart3 = new Pose(144 - 40.47819063004846, 35.592891760904685, Math.toRadians(180));
    static final Pose BlueBallDepotEnd1 = new Pose(144 - 12.794830371567043, 83.74798061389338, Math.toRadians(180));
    static final Pose BlueBallDepotEnd2 = new Pose(144 - 12.794830371567043, 59.2, Math.toRadians(180));
    static final Pose BlueBallDepotEnd3 = new Pose(144 - 12.794830371567043, 35.592891760904685, Math.toRadians(180));
    static final Pose BlueGateInit = new Pose(144 - 17.21486268174475, 74.21001615508885, Math.toRadians(180));
    static final Pose BlueGateOpen = new Pose(144 - 17.21486268174475, 79.56058158319871, Math.toRadians(180));
    static final Pose BlueSweepStart = new Pose(144 - 7.444264943457189, 53.04038772213247, Math.toRadians(260));
    static final Pose BlueSweepEnd = new Pose(144 - 7.444264943457189, 17.8, Math.toRadians(260));

    // ===== RED PATHS =====
    public PathChain RedStartPosToRedShootPos;
    public PathChain RedShootPosToRedBallDepotStart1;
    public PathChain RedBallDepotStart1ToRedBallDepotEnd1;
    public PathChain RedBallDepotEnd1ToRedShootPos;
    public PathChain RedShootPosToRedGateInit;
    public PathChain RedGateInitToRedGateOpen;
    public PathChain RedGateOpenToRedBallDepotStart1;
    public PathChain RedBallDepotStart1ToRedBallDepotStart2;
    public PathChain RedBallDepotStart2ToRedBallDepotEnd2;
    public PathChain RedBallDepotEnd2ToRedShootPos;
    public PathChain RedShootPosToRedBallDepotStart3;
    public PathChain RedBallDepotStart3ToRedBallDepotEnd3;
    public PathChain RedBallDepotEnd3ToRedShootPos;
    public PathChain RedShootPosToRedSweepStart;
    public PathChain RedSweepStartToRedSweepEnd;
    public PathChain RedSweepEndToRedShootPos;

    // ===== BLUE PATHS =====
    public PathChain BlueStartPosToBlueShootPos;
    public PathChain BlueShootPosToBlueBallDepotStart1;
    public PathChain BlueBallDepotStart1ToBlueBallDepotEnd1;
    public PathChain BlueBallDepotEnd1ToBlueShootPos;
    public PathChain BlueShootPosToBlueGateInit;
    public PathChain BlueGateInitToBlueGateOpen;
    public PathChain BlueGateOpenToBlueBallDepotStart1;
    public PathChain BlueBallDepotStart1ToBlueBallDepotStart2;
    public PathChain BlueBallDepotStart2ToBlueBallDepotEnd2;
    public PathChain BlueBallDepotEnd2ToBlueShootPos;
    public PathChain BlueShootPosToBlueBallDepotStart3;
    public PathChain BlueBallDepotStart3ToBlueBallDepotEnd3;
    public PathChain BlueBallDepotEnd3ToBlueShootPos;
    public PathChain BlueShootPosToBlueSweepStart;
    public PathChain BlueSweepStartToBlueSweepEnd;
    public PathChain BlueSweepEndToBlueShootPos;

    // ===== BUILD ALL PATHS =====
    public void buildPaths() {

        // ---- RED PATHS ----
        RedStartPosToRedShootPos = buildPath(RedStartPos, RedShootPos);
        RedShootPosToRedBallDepotStart1 = buildPath(RedShootPos, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotEnd1 = buildPath(RedBallDepotStart1, RedBallDepotEnd1);
        RedBallDepotEnd1ToRedShootPos = buildPath(RedBallDepotEnd1, RedShootPos);
        RedShootPosToRedGateInit = buildPath(RedShootPos, RedGateInit);
        RedGateInitToRedGateOpen = buildPath(RedGateInit, RedGateOpen);
        RedGateOpenToRedBallDepotStart1 = buildPath(RedGateOpen, RedBallDepotStart1);
        RedBallDepotStart1ToRedBallDepotStart2 = buildPath(RedBallDepotStart1, RedBallDepotStart2);
        RedBallDepotStart2ToRedBallDepotEnd2 = buildPath(RedBallDepotStart2, RedBallDepotEnd2);
        RedBallDepotEnd2ToRedShootPos = buildPath(RedBallDepotEnd2, RedShootPos);
        RedShootPosToRedBallDepotStart3 = buildPath(RedShootPos, RedBallDepotStart3);
        RedBallDepotStart3ToRedBallDepotEnd3 = buildPath(RedBallDepotStart3, RedBallDepotEnd3);
        RedBallDepotEnd3ToRedShootPos = buildPath(RedBallDepotEnd3, RedShootPos);
        RedShootPosToRedSweepStart = buildPath(RedShootPos, RedSweepStart);
        RedSweepStartToRedSweepEnd = buildPath(RedSweepStart, RedSweepEnd);
        RedSweepEndToRedShootPos = buildPath(RedSweepEnd, RedShootPos);

        // ---- BLUE PATHS ----
        BlueStartPosToBlueShootPos = buildPath(BlueStartPos, BlueShootPos);
        BlueShootPosToBlueBallDepotStart1 = buildPath(BlueShootPos, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotEnd1 = buildPath(BlueBallDepotStart1, BlueBallDepotEnd1);
        BlueBallDepotEnd1ToBlueShootPos = buildPath(BlueBallDepotEnd1, BlueShootPos);
        BlueShootPosToBlueGateInit = buildPath(BlueShootPos, BlueGateInit);
        BlueGateInitToBlueGateOpen = buildPath(BlueGateInit, BlueGateOpen);
        BlueGateOpenToBlueBallDepotStart1 = buildPath(BlueGateOpen, BlueBallDepotStart1);
        BlueBallDepotStart1ToBlueBallDepotStart2 = buildPath(BlueBallDepotStart1, BlueBallDepotStart2);
        BlueBallDepotStart2ToBlueBallDepotEnd2 = buildPath(BlueBallDepotStart2, BlueBallDepotEnd2);
        BlueBallDepotEnd2ToBlueShootPos = buildPath(BlueBallDepotEnd2, BlueShootPos);
        BlueShootPosToBlueBallDepotStart3 = buildPath(BlueShootPos, BlueBallDepotStart3);
        BlueBallDepotStart3ToBlueBallDepotEnd3 = buildPath(BlueBallDepotStart3, BlueBallDepotEnd3);
        BlueBallDepotEnd3ToBlueShootPos = buildPath(BlueBallDepotEnd3, BlueShootPos);
        BlueShootPosToBlueSweepStart = buildPath(BlueShootPos, BlueSweepStart);
        BlueSweepStartToBlueSweepEnd = buildPath(BlueSweepStart, BlueSweepEnd);
        BlueSweepEndToBlueShootPos = buildPath(BlueSweepEnd, BlueShootPos);
    }

    // ===== HELPER =====
    private PathChain buildPath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }
}

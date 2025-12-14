package org.firstinspires.ftc.teamcode.cougears.util;


import com.acmerobotics.dashboard.config.Config;

/*
EX:
// KEY: 0-init, 1-high, 2-mid, 3-low
public static int[] slidePresets = {0, 4000, 2000, 1000};

USE import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*; to import
 */

@Config
public class PresetConstants {
    // DRIVE
    public static double slowMultiplier = .25;
    public static double intakePower = .8;
    //FW
    public static double[] FW_PIDF = {5.5, 0, 4, 6.9}; // Nice
    public static double shootVel = 1600;
    public static double shootVelFar = 1800;
    public static double ejectionVel = -1000;
    public static double gateWait = 750;
    public static double shootSequenceWait = 750;
    // Turret
    public static double ticksPerDeg = 800/90.0; // ~ 2.08, 90 deg = 188 ticks
    public static int[] turretPos = {0, -(int) Math.round(ticksPerDeg * 45),
            (int) Math.round(ticksPerDeg * 45)};
    public static int[] turretLimits = {-400,400}; // CW, CCW
    public static int turretStep = 100;
    //Servos
    public static double[] transferArmPos = {.8,.4};
    public static double[] blockerPos = {.21,1};
    //AprilTag
    public static int redTag = 24;
    public static int blueTag = 20;
    public static double desiredDistClose = 67.0;
    public static double desiredDistFar = 0; // Need to find
    //Autons
    public static double spinUpTime = 0;
    public static double timeBackwardsClose = 1.4;
    public static double speedBackwardsClose = -.5;
    public static int repeatShots = 5;

    // NAMES
    public static String[] motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "TurretController", "Intake"};
    public static String[] servoNames = {"Transfer", "TransferArm"};

    /* Config
   Control hub:
       Motors: motorFL, motorBL, TurretRotator, FW
       Servos:
   Expansion hub:
       Motors: motorFR, motorFL, Intake, __
       Servos:__, Transfer

    BUTTONS
        Y -> Align to april tag
        X -> Toggles intake
        A -> Reset Turret
        Lt -> Close shot
        Lb -> Far shot
        Rb -> Reject
        Rt -> Transfer (needs to be added maybe timer?)
        Right joystick -> Cardinal directions
        Left joystick -> Turning
        L/R Dpad -> Manual control of turret
        Up Dpad -> Align turret to AT
        Down Dpad -> Full Auto to AT
    */


}
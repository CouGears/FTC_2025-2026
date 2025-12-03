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
    //FW
    public static double[] FW_PIDF = {31.4, 1, 0, 6};
    public static double shootVel = 1500;
    public static double shootVelFar = 1800;
    public static double ejectionVel = -1000;
    // Turret
    public static int[] turretPos = {0, 0, 0, 0};
    public static int[] turretLimits = {0,0};
    public static int ticksPerDeg = 0;
    //Hood
    public static int[] hoodLimits = {0,0};
    //Servos
    public static double[] feedServoPos = {0, 0};
    public static double[] hoodServoLimits = {0, 0};
    //AprilTag
    public static int redTag = 24;
    public static int blueTag = 20;
    public static double desiredDistClose = 67.0;
    public static double desiredDistFar = 0; // Need to find
    // NAMES
    public static String[] motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "TurretController", "Intake"};
    public static String[] servoNames = {"FeedServo"};

    /* Config
   Control hub:
       Motors: motorFL, motorBL, TurretRotator, FW
       Servos:
   Expansion hub:
       Motors: motorFR, motorFL, Intake, __
       Servos:

    BUTTONS
        Y -> Align to april tag
        X -> Toggles intake
        Lt -> Close shot
        Lb -> Far shot
        Rb -> Reject
        Rt -> Transfer (needs to be added maybe timer?)
        Right joystick -> Cardinal directions
        Left joystick -> Turning
    */


}
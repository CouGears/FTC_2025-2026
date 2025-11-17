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
    public static int[] TurretPos = {0, 0, 0, 0};
    //Servos
    public static double[] FeedServoPos = {0, 0};
    //AprilTag
    public static int redTag = 24;
    public static int blueTag = 20;
    public static double desiredDist = 67.0;
    // NAMES
    public static String[] motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "TurretController", "Intake"};
    public static String[] servoNames = {"FeedServo"};
}
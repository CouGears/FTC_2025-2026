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
    public static double[] GateServoPos = {.5, .8};
    public static double FWSpeed = .7;
    public static String[] motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW"};
    public static String[] servoNames = {"GateServo"};

}

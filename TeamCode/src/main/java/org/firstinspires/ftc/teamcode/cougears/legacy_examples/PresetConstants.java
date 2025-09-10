package org.firstinspires.ftc.teamcode.cougears.legacy_examples;


import com.acmerobotics.dashboard.config.Config;

@Config
public class PresetConstants {
    // 0init, 1high drop, 2high hold, 3mid, 4low, 5specimen grab, 6specimen setup, 7specimen attach, 8hang
    public static int[] slidePresets = {0, 4000, 4000, 0, 0, 0, 0, 0, 5500};
    // 0init, 1high drop, 2high hold, 3mid, 4low, 5specimen grab, 6specimen setup, 7specimen attach, 8hang
    public static int[] armThetaPresets = {0, 200, 100, 600, 700, 820, 300, 400, 0};
    // 0init, 1high drop, 2high hold, 3mid, 4low, 5specimen grab, 6specimen setup, 7specimen attach, 8hang
    public static double[] axis1Presets = {0.3, 0.42, 0.52, 0.49, 0.55, 1, 0.72, 0.72, 0.3};

    // STATES USES THIS: full left, half left, center, half right, full right
    public static int[] axis2Positions = {2, 1, 0, 3, 4};

    // STATES DOES NOT USE THIS: center, half left, full left, half right, full right
    public static double[] axis2Presets = {0.43, 0.595, 0.76, 0.265, 0.1}; // good
    // open, closed
    public static double[] clawPresets = {0.35, 0.26}; // good
}

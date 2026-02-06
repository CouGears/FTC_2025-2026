package org.firstinspires.ftc.teamcode.cougears.util;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

/*
EX:
// KEY: 0-init, 1-high, 2-mid, 3-low
public static int[] slidePresets = {0, 4000, 2000, 1000};

USE import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.*; to import
 */
@Configurable
@Config
public class PresetConstants {
    // DRIVE
    public static double Drive_slowMultiplier = .25;
    public static double Drive_intakePower = .6;
    public static double Drive_baseTransferPower = .1;
    public static double Drive_transferPower = 0.85 ;
    public static boolean Drive_switchedJoysticks = false;
    //FW
    public static double[] FW_PIDF = {214, 0, 0, 18};
    public static double FW_ejectionVel = -1000;

    //Servos
    public static double[] Servo_blockerPos = {.75,.4};

    //Autons
    public static int Auton_numberOfRepeatShots = 5;

    public static double Auton_spinupWait = 2000;
    public static double Auton_firstShotExtraSpinupWait = 2000;
    public static double Auton_gateWait = 500;
    public static double Auton_ballTransferWait = 1000;
    public static double Auton_transferResetWait = 750;
    public static double Auton_ballShootSequenceTime = 1500;
    public static double Auton_pickupSpeed = .4;

    public static double xyPoseErrorPTM = 0.5;
    public static double headingPoseErrorPTM = Math.toRadians(5);

    // NAMES
    public static String[] Names_motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "Intake"};
    public static String[] Names_servoNames = {"Transfer"};
    public static double randomVarConstant = 5; // To be used anywhere you need for testing

    /* Config

    */


}
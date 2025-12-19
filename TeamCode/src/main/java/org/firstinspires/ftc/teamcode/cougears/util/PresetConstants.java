package org.firstinspires.ftc.teamcode.cougears.util;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

/*
EX:
// KEY: 0-init, 1-high, 2-mid, 3-low
public static int[] slidePresets = {0, 4000, 2000, 1000};

USE import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*; to import
 */
@Configurable
@Config
public class PresetConstants {
    // DRIVE
    public static double Drive_slowMultiplier = .25;
    public static double Drive_intakePower = 1;
    //FW
    public static double[] FW_PIDF = {5.5, 0, 4, 6.9}; // Nice
    public static double FW_shootVel = 1600;
    public static double FW_shootVelFar = 1800;
    public static double FW_ejectionVel = -1000;


    // Turret
    public static double Turret_ticksPerDeg = 800/90.0; // ~ 2.08, 90 deg = 188 ticks
    public static int[] Turret_turretPos = {0, -(int) Math.round(Turret_ticksPerDeg * 45),
            (int) Math.round(Turret_ticksPerDeg * 45)};
    public static int[] Turret_turretLimits = {-400,400}; // CW, CCW
    public static int Turret_turretStep = 100;
    //Servos
    public static double[] Servo_transferArmPos = {.75,.17};
    public static double[] Servo_blockerPos = {.21,1};
    //AprilTag
    public static int AT_redTag = 24;
    public static int AT_blueTag = 20;
    public static double AT_desiredDistClose = 67.0;
    public static double AT_desiredDistFar = 0; // Need to find
    //Autons
    public static double spinUpTime = 0;
    public static double timeBackwardsClose = 1.4;
    public static double speedBackwardsClose = -.5;
    public static int Auton_numberOfRepeatShots = 3;

    public static double Auton_spinupWait = 2000;
    public static double Auton_firstShotExtraSpinupWait = 2000;
    public static double Auton_gateWait = 750;
    public static double Auton_ballTransferWait = 1000;
    public static double Auton_transferResetWait = 750;
    public static double Auton_pushNewBallWait = 500;
    public static double Auton_pickupSpeed = .4;

    // NAMES
    public static String[] Names_motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "TurretController", "Intake"};
    public static String[] Names_servoNames = {"Transfer", "TransferArm"};
    public static double randomVarConstant = 5; // To be used anywhere you need for testing

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
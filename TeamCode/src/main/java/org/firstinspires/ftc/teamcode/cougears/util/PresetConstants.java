package org.firstinspires.ftc.teamcode.cougears.util;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

/*USE
import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.*;
to import
 */

@Configurable
@Config
public class PresetConstants {
    // DRIVE
    public static double Drive_slowMultiplier = .25;
    public static double Drive_intakePower = .6;
    public static double Drive_baseTransferPower = .5;
    public static double Drive_transferPower = 0.80 ;
    public static double Drive_transferPowerFar = 0.7 ;

    public static boolean Drive_switchedJoysticks = false;

    //FW
    public static double[] FW_PIDF = {218, 0, 0, 17};
    public static double FW_ejectionVel = -1000;

    //Servos
    public static double[] Servo_blockerPos = {.75,.4};


    //Autons
    public static double Auton_blockerWait = 200; //time to open gate before shooting
    public static double Auton_gateIntakeWait = 3500;//time to stay at autonGateIntake
    public static double Auton_gateOpenWait = 2500;//time to wait at gate
    public static double Auton_ballShootSequenceTime = 1100;//time from start shooting to end shooting
    public static double Auton_startShootingVelocityTolerance = 30;//wait until this velocity before starting to shoot
    public static double Auton_gateWaitTime = 2000;
    public static double Auton_ATAlignemntPower = .02;

    public static double unstuckMovementAmount = 0.1;
    public static double xyPoseErrorPTM = 0.3;
    public static double headingPoseErrorPTM = Math.toRadians(3);

    public static int redTag = 24;
    public static int blueTag = 20;
    public static double ATBearingTolerance = 3;

    public static double DIST_SENSOR_BALL_DISTANCE1 = 14;
    public static double DIST_SENSOR_BALL_DISTANCE2 = 14;
    public static double DIST_SENSOR_BALL_DISTANCE3 = 14;
    public static double Sensor_distSensorWait = .500;

    public static double driverHeadingCorrectionThreshhold = 5;
    public static  double HEADING_kP = 0.002;
    public static  double HEADING_MAX_CORRECTION = 0.08;



    // NAMES
    public static String[] Names_motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "Intake, Transfer"};
    public static String[] Names_servoNames = {"Transfer"};

    /* Config

    */


}
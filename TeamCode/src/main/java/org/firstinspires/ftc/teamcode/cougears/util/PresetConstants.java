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
    public static double Auton_ballShootSequenceTime = 1000;//time from start shooting to end shooting
    public static double Auton_startShootingVelocityTolerance = 100;//wait until this velocity before starting to shoot

    public static double xyPoseErrorPTM = 0.3;
    public static double headingPoseErrorPTM = Math.toRadians(3);

    // NAMES
    public static String[] Names_motorNames = {"motorFL", "motorFR", "motorBL", "motorBR", "FW", "Intake, Transfer"};
    public static String[] Names_servoNames = {"Transfer"};

    /* Config

    */


}
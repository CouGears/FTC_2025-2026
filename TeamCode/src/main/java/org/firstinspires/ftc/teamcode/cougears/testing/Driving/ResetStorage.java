package org.firstinspires.ftc.teamcode.cougears.testing.Driving;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;

@TeleOp
public class ResetStorage extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Storage.Storage_endOfAutonPose = RedStartPos;
    }
}

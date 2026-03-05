package org.firstinspires.ftc.teamcode.cougears.autons.Red;

import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.RedStartPos;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.cougears.autons.V3AutonBase;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(group = "Red")
public class RedReset extends OpMode {
    public V3AutonBase bot;
    public Follower follower;
    @Override

    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(RedStartPos);
        bot = new V3AutonBase(hardwareMap, telemetry);
        bot.botInit();
    }

    @Override
    public void loop() {
        Storage.Storage_endOfAutonPose = follower.getPose();
        Storage.Storage_endOfAutonColor = "Red";
        terminateOpModeNow();
    }
}

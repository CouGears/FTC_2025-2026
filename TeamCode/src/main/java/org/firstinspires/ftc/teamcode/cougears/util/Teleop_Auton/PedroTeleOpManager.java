package org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PedroTeleOpManager {
    // Auton Moving
    public Follower follower;
    public PedroTeleOpManager(HardwareMap HM) {
        follower = Constants.createFollower(HM);
        follower.setPose(Storage.endOfAutonPose);
    }
    public void moveToPos(Pose targetPos){
        follower.followPath(
                follower.pathBuilder()
                        .addPath(new BezierLine(follower.getPose(), targetPos))
                        .setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPos.getHeading())
                        .build()
        );
    }
    public void update(){
        follower.update();
    }
}

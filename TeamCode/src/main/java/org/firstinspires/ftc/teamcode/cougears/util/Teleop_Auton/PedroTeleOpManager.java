package org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Turret_ticksPerDeg;
import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.Turret_turretLimits;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;

public class PedroTeleOpManager {
    // Auton Moving
    public Follower follower;
    public PedroTeleOpManager(HardwareMap HM) {
        follower = Constants.createFollower(HM);
        follower.setPose(Storage.endOfAutonPose);
    }
    public void moveToPos(Pose targetPos) {
        follower.followPath(
                follower.pathBuilder()
                        .addPath(new BezierLine(follower.getPose(), targetPos))
                        .setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPos.getHeading())
                        .build()
        );
    }


        public void alignToGoal () {
            double botX = follower.getPose().getX();
            double botY = follower.getPose().getY();
            double botHeadingDeg = follower.getHeading();

            double goalX, goalY;
            if (endOfAutonColor.equals("Red")) {
                goalX = 0;
                goalY = 144;
            } else {
                goalX = 144;
                goalY = 144;
            }

            // Calculate vector from bot to goal
            double dx = goalX - botX;
            double dy = goalY - botY;

            // Calculate angle to goal in field coordinates, use atan2 bc better??
            double targetFieldDeg = Math.toDegrees(Math.atan2(dy, dx));
            Pose poseGoalAngle = new Pose(botX, botY, targetFieldDeg);
            moveToPos(poseGoalAngle);
        }

        public void parkRobot(){
            if (endOfAutonColor.equals("Red")){
                moveToPos(RedPark);
            } else {
                moveToPos(BluePark);
            }
        }

        public void update () {
            follower.update();
        }
    }

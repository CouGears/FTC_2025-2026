package org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.*;

public class PedroTeleOpManager {
    // Auton Moving
    public Follower follower;
    public String goal = Storage_endOfAutonColor;

    public PedroTeleOpManager(HardwareMap HM) {
        follower = Constants.createFollower(HM);
        follower.setPose(Storage.Storage_endOfAutonPose);
    }

    public PedroTeleOpManager(HardwareMap HM, Pose setPose) {
        follower = Constants.createFollower(HM);
        follower.setPose(setPose);
    }
    public void moveToPos(Pose targetPos) {
        double xyPoseError = 0.5;
        double headingPoseError = Math.toRadians(5);
        if (Math.abs(follower.getPose().getX()-targetPos.getX()) < xyPoseError &&
                Math.abs(follower.getPose().getY()-targetPos.getY()) < xyPoseError &&
                Math.abs(follower.getPose().getHeading()-targetPos.getHeading()) < headingPoseError){
            return;
        }
        follower.followPath(
                follower.pathBuilder()
                        .addPath(new BezierLine(follower.getPose(), targetPos))
                        .setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPos.getHeading())
                        .build()
        );
    }

    public ShootingPosition getClosestShootingPosition(){
        ShootingPosition[] shootingPosArray;
        if (Storage_endOfAutonColor.equals("Red")) {
            shootingPosArray = redShootingPosArray;
        } else {
            shootingPosArray = blueShootingPosArray;
        }

        double closestDist = robotDistanceFromPos(shootingPosArray[0].getShootingPose());
        ShootingPosition closestShootingPosition = shootingPosArray[0];

        for(ShootingPosition shootingPos : shootingPosArray){
            double shootPosDistance = robotDistanceFromPos(shootingPos.getShootingPose());
            if (shootPosDistance < closestDist){
                closestDist = shootPosDistance;
                closestShootingPosition = shootingPos;
            }
        }
        return closestShootingPosition;
    }

    public double robotDistanceFromPos(Pose pose){
        double botX = follower.getPose().getX();
        double botY = follower.getPose().getY(); // Fixed: was getX()
        return Math.hypot(pose.getX() - botX, pose.getY() - botY);
    }
    public void alignToGoal () {
        double botX = follower.getPose().getX();
        double botY = follower.getPose().getY();

        double goalX, goalY;
        goalY = 144;
        if (goal.equals("Red")) {
            goalX = 0;
        } else {
            goalX = 144;
        }

        // Calculate vector from bot to goal
        double dx = goalX - botX;
        double dy = goalY - botY;

        double targetFieldDeg = Math.toDegrees(Math.atan2(dy, dx));
        Pose poseGoalAngle = new Pose(botX, botY, targetFieldDeg);
        moveToPos(poseGoalAngle);
    }
    public String getGoal() {return goal;}
    public Pose getCurrPos() {return follower.getPose();}
    public void switchGoal(){
        if (goal.equals("Red"))
            goal = "Blue";
        else
            goal = "Red";
    }

    public void parkRobot(){
        if (goal.equals("Red")){
            moveToPos(RedPark);
        } else {
            moveToPos(BluePark);
        }
    }

    public void updatePosAndMotors() { follower.update(); }
    public void updatePos() { follower.updatePose();}
    public boolean isBusy() { return follower.isBusy(); }
    public void breakFollower() { follower.breakFollowing(); }
}

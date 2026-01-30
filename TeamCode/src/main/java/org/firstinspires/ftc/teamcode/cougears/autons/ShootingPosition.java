package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.geometry.Pose;

public class ShootingPosition {
    private Pose shootingPose;
    private int velocity;
    public ShootingPosition(Pose shootingPose, int velocity){
        this.shootingPose = shootingPose;
        this.velocity = velocity;
    }

    public Pose getShootingPose(){
        return shootingPose;
    }
    public int getShootingVelocity(){
        return velocity;
    }


}

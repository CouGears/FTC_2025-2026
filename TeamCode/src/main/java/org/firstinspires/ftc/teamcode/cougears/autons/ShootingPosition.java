package org.firstinspires.ftc.teamcode.cougears.autons;

import com.pedropathing.geometry.Pose;

public class ShootingPosition {
    private Pose shootingPose;
    private int velocity;
    private String color;
    public ShootingPosition(Pose shootingPose, int velocity, String color){
        this.shootingPose = shootingPose;
        this.velocity = velocity;
        this.color = color;
    }

    public Pose getShootingPose(){
        return shootingPose;
    }
    public int getShootingVelocity(){
        return velocity;
    }
    public String getShootingColor(){
        return color;
    }

}

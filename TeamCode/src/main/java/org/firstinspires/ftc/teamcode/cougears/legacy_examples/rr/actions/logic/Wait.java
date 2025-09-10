package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.actions.logic;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Wait {
    public final MecanumDrive drive;
    public Wait(MecanumDrive drive) {
        this.drive = drive;
    }

    public Action waitSeconds(double time) {
        return drive.actionBuilder(new Pose2d(0, 0, 0))
                .waitSeconds(time)
                .build();
    }
    public Action waitMillieconds(double time) {
        return drive.actionBuilder(new Pose2d(0, 0, 0))
                .waitSeconds(time/1000)
                .build();
    }
}

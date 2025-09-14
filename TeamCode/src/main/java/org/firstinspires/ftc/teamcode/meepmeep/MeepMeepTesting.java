package org.firstinspires.ftc.teamcode.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800); // Set the window size

        // Declare a BotEntity. This is your virtual robot.
        // The constraints are just rough estimates, but they will still show you the path.
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> {
                            // This is where you build your trajectory sequence.
                            // The 'drive' object here is a RoadRunner drive train.
                            // You can call any of the Road Runner trajectory sequence methods on it.
                            return drive.trajectorySequenceBuilder(new Pose2d(-36, 60, Math.toRadians(270))) // Starting Pose
                                    .forward(24)
                                    .turn(Math.toRadians(90))
                                    .strafeRight(12)
                                    .waitSeconds(1)
                                    .lineToLinearHeading(new Pose2d(48, 36, Math.toRadians(0)))
                                    .build();
                        }
                );

        // Set the field image
        // Make sure you have the field image in your project's root folder,
        // or provide the full path to it.
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK) // Use the CENTERSTAGE field for now, you can change it
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot) // Add the bot to the field
                .start();
    }
}
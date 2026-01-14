package org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;

import static org.firstinspires.ftc.teamcode.cougears.legacy_examples.V2Bot.PresetConstants.*;

public class goalUtils {

    private final int[] goalTagIDs = { AT_redTag, AT_blueTag };
    private int goalIndex = 0;
    private boolean tagLockEnabled = false;
    Storage storage = new Storage();
    public void switchLockedGoal(){
        goalIndex = (goalIndex + 1 ) % goalTagIDs.length;
    }
    public void getLockedGoal() {
        if (Storage.Storage_endOfAutonColor.equals("Red")){
            goalIndex = 0;
        } else {
            goalIndex = 1;
        }
    }

    public void toggleTagLock() {
        tagLockEnabled = !tagLockEnabled;
    }

    public boolean isTagLockEnabled() {
        return tagLockEnabled;
    }

    public int getLockedTagID() {
        return goalTagIDs[goalIndex];
    }

    public int getLockedTagIndex(){
        return goalIndex;
    }

    public void displayLockedTag(Telemetry tele) {
        tele.addLine(goalIndex == 0 ? "LockedTag: RED" : "LockedTag: BLUE");
    }
}

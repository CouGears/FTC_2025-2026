package org.firstinspires.ftc.teamcode.cougears.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage;

import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;

public class goalUtils {

    private final int[] goalTagIDs = { AT_redTag, AT_blueTag };
    private int goalIndex = 0;
    private boolean tagLockEnabled = false;
    Storage storage = new Storage();

    public void getLockedGoal() {
        if (Storage.endOfAutonColor.equals("Red")){
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

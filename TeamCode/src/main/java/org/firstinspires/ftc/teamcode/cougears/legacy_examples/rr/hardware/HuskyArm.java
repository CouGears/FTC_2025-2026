package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HuskyArm {
    private HuskyLens husky;
    public HuskyArm(HardwareMap hardwareMap) {
        husky = hardwareMap.get(HuskyLens.class, "husky2");
    }
}

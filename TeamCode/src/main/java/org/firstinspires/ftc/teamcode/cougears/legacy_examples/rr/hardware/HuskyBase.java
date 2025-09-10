package org.firstinspires.ftc.teamcode.cougears.legacy_examples.rr.hardware;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HuskyBase {
    private HuskyLens husky;
    public HuskyBase(HardwareMap hardwareMap) {
        husky = hardwareMap.get(HuskyLens.class, "husky1");
    }
}

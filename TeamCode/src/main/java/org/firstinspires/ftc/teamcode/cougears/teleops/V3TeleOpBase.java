    package org.firstinspires.ftc.teamcode.cougears.teleops;

    import static org.firstinspires.ftc.teamcode.cougears.autons.PositionsAndPaths.*;
    import static org.firstinspires.ftc.teamcode.cougears.util.PresetConstants.*;
    import static org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.Storage.Storage_endOfAutonColor;

    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.Gamepad;
    import com.qualcomm.robotcore.hardware.HardwareMap;

    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.Range;

    import org.firstinspires.ftc.robotcore.external.Telemetry;
    import org.firstinspires.ftc.teamcode.cougears.autons.ShootingPosition;
    import org.firstinspires.ftc.teamcode.cougears.util.BotBase;
    import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
    import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
    import org.firstinspires.ftc.teamcode.cougears.util.GamepadManager;
    import org.firstinspires.ftc.teamcode.cougears.util.Teleop_Auton.PedroTeleOpManager;
    import com.qualcomm.robotcore.hardware.I2cDeviceSynch;




    public class V3TeleOpBase extends BotBase {
        PedroTeleOpManager PTM = new PedroTeleOpManager(HM);
        //Initializing motors
        public DcMotorEx FW, Intake, Transfer;
        public Servo Blocker;
        I2cDeviceSynch nanoI2c;
        byte[] colorByte = new byte[1]; // buffer to send 1 byte

        //initializing toggles
        public boolean IntakeSpinning, slowed;
        //intializing speed multplier for slowdrive
        public double speedMultiplier = 1;
        //Initialize heading stuff
        private double targetHeadingDeg = 0.0;
        private boolean headingLocked = false;

        // Tunables
        private static final double HEADING_kP = 0.02;
        private static final double DRIVE_DEADBAND = 0.05;

        //initialize classes
        public GoBildaPinpointDriver pinpoint;
        public String goal = Storage_endOfAutonColor;


        public V3TeleOpBase(HardwareMap HardwareMap, Telemetry Telemetry, Gamepad gamepad1, Gamepad gamepad2) {
            super(HardwareMap, Telemetry, gamepad1, gamepad2);
        }

        public boolean botInit() {
            super.botInit();
            try {
                FW = HM.get(DcMotorEx.class, "FW");
                FW.setDirection(DcMotor.Direction.REVERSE);
                FW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                FW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                FW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                FW.setVelocityPIDFCoefficients(FW_PIDF[0], FW_PIDF[1], FW_PIDF[2], FW_PIDF[3]);

                Intake = HM.get(DcMotorEx.class, "Intake");
                Intake.setDirection(DcMotor.Direction.REVERSE);
                Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                Transfer = HM.get(DcMotorEx.class, "Transfer");
                Transfer.setDirection(DcMotor.Direction.FORWARD);
                Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                Blocker = HM.get(Servo.class, "Blocker");
                Blocker.setPosition(Servo_blockerPos[0]);

                pinpoint = HM.get(GoBildaPinpointDriver.class, "pinpoint");
                // I2C device for Nano LED strip

            } catch (Exception e) {
                tele.addData("ERROR", "COULD NOT INIT");
                tele.addData("ERROR MSG:", e);
                return false;
            }
            try {
                // I2C setup
                nanoI2c = HM.get(I2cDeviceSynch.class, "nanoI2c");
                nanoI2c.engage(); // start communication
                handleLeds(goal); // sets LED to end-of-auton color
            } catch (Exception e) {
                tele.addData("LED ERROR", "Could not init I2C Nano");
                tele.addData("MSG", e);
            }

            return true;
        }



        //****** FLYWHEELS ******

        public void killFW() {
            FW.setPower(0);
        }
        public void ejectFW() {
            FW.setPower(FW_ejectionVel);
        }
        public boolean FWUpToSpeed (double speed) {
            return FW.getVelocity() >= speed;
        }
        public void FWSpinTo(double speed){
            FW.setVelocity(speed);
        }

        public void openBlocker(){
            Blocker.setPosition(Servo_blockerPos[1]);
        }
        public void closeBlocker(){
            Blocker.setPosition(Servo_blockerPos[0]);
        }
        public boolean blockerIsOpen() {return Blocker.getPosition() > Servo_blockerPos[1] - .05; }

        //****** INTAKE ******
        public void startIntake() {
            Intake.setPower(Drive_intakePower);
            IntakeSpinning = true;
        }
        public void killIntake() {
            Intake.setPower(0);
            IntakeSpinning = false;
        }
        public void ejectIntake() {
            Intake.setPower(-1);
            IntakeSpinning = false; // So next time you press X it starts spinning in
        }
        //****** Transfer ******
        public void startTransferSlow() { Transfer.setPower(Drive_baseTransferPower);}
        public void startTransfer() { Transfer.setPower(Drive_transferPower);}
        public void killTransfer() { Transfer.setPower(0);}
        public void ejectTransfer() { Transfer.setPower(-1);}

        //****** SHOOTING ******
        public void setLEDColor(int colorVal) {
            colorByte[0] = (byte) colorVal; // 1 = red, 2 = blue
            nanoI2c.write(0, colorByte);    // send byte to Nano
        }
        public void handleLeds(String color){
            if (color.equalsIgnoreCase("Red")){
                setLEDColor(1); // Red
            } else if (color.equalsIgnoreCase("Blue")){
                setLEDColor(2); // Blue
            }
        }


        //****** OTHER ******
        public void endTeleOp(){
            super.endTeleOp();
            FW.setPower(0);
            Intake.setPower(0);
        }

        public void toggleSlow(){
            slowed = !slowed;
        }
        public void RafiDrive(Gamepad gamepad1, boolean switchSticks) {

            // --- Speed scaling ---
            if (!slowed) {
                speedMultiplier = 1;
            } else {
                speedMultiplier = Drive_slowMultiplier;
            }
            speedMultiplier = -Range.clip(speedMultiplier, 0, 1);

            // --- Update odometry ---
            pinpoint.update();

            double forward, strafe, turnInput;
            // --- Driver inputs ---
            if (!switchSticks) {
                forward = gamepad1.right_stick_y * speedMultiplier;
                strafe = gamepad1.right_stick_x * speedMultiplier;
                turnInput = gamepad1.left_stick_x * speedMultiplier;
            } else {
                forward = gamepad1.left_stick_y * speedMultiplier;
                strafe = gamepad1.left_stick_x * speedMultiplier;
                turnInput = gamepad1.right_stick_x * speedMultiplier;
            }

            double turn;

            double currentHeading =
                    pinpoint.getPosition().getHeading(AngleUnit.DEGREES);

            // Are we actually translating?
            boolean driving =
                    Math.abs(forward) > DRIVE_DEADBAND ||
                            Math.abs(strafe)  > DRIVE_DEADBAND;

            // --- Heading assist logic ---
            if (!driving) {
                // Idle → zero assist
                headingLocked = false;
                turn = turnInput;
            }
            else if (Math.abs(turnInput) > 0.05) {
                // Manual turn → no assist
                headingLocked = false;
                turn = turnInput;
                targetHeadingDeg = currentHeading;
            }
            else {
                // Driving straight/strafe → assist
                if (!headingLocked) {
                    targetHeadingDeg = currentHeading;
                    headingLocked = true;
                }

                double error = targetHeadingDeg - currentHeading;

                // Wrap [-180, 180]
                while (error > 180) error -= 360;
                while (error < -180) error += 360;

                turn = error * HEADING_kP;
            }

            // --- RAFI mecanum math (unchanged) ---
            double frontLeftPower  = forward - strafe - turn;
            double frontRightPower = forward + strafe + turn;
            double backLeftPower   = forward + strafe - turn;
            double backRightPower  = forward - strafe + turn;

            double maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            maxPower = Math.max(maxPower, Math.abs(backLeftPower));
            maxPower = Math.max(maxPower, Math.abs(backRightPower));

            if (maxPower > 1.0) {
                frontLeftPower  /= maxPower;
                frontRightPower /= maxPower;
                backLeftPower   /= maxPower;
                backRightPower  /= maxPower;
            }

            motorFL.setPower(frontLeftPower);
            motorFR.setPower(frontRightPower);
            motorBL.setPower(backLeftPower);
            motorBR.setPower(backRightPower);
        }
    }

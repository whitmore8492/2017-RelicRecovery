/*

*/


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "RecoveryZone", group = "")  // @Autonomous(...) is the other common choice

public class RecoveryZone extends OpMode {

    public static int stage_0PreStart = 0;
    public static int stage_10GripBlock = 10;
    public static int stage_20StingerExtend = 20;
    public static int stage_30ReadPlatformColor = 30;
    public static int stage_40exce_jewels = 40;
    public static int stage_50Knockjewelloff = 50;
    public static int stage_60Return2Start = 60;
    public static int stage_70Turn1 = 70;
    public static int stage_80MoveFoward = 80;
    public static int stage_90Turn2 = 90;
    public static int stage_100MoveFoward7 = 100;
    public static int stage_110DropBlock = 110;
    public static int stage_120MoveBack1 = 120;
    public static int stage_150Done = 150;

    public static int color_mode_Red = 1;
    public static int color_mode_Blue = 2;

    int currentcolormode = 0;
    int CurrentStage = stage_0PreStart;


    Chassis robotChassis = new Chassis();
    Jewels jewels = new Jewels();


    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        robotChassis.hardwareMap = hardwareMap;
        robotChassis.telemetry = telemetry;
        robotChassis.init();
        //telemetry.addData("Status", "Initialized");
        jewels.hardwareMap = hardwareMap;
        jewels.telemetry = telemetry;
        jewels.init();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */


        // eg: Set the drive motor directions:
        // Reverse the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        robotChassis.init_loop();
        jewels.init_loop();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

        robotChassis.start();
        jewels.start();
        runtime.reset();
        //shootTrigger.setPosition(Settings.reset);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //telemetry.addData("Status", shootTrigger.getPosition());
        //telemetry.addData("Status", "Running: " + runtime.toString());
        robotChassis.loop();


        if (CurrentStage == stage_0PreStart) {
            //Start Stage 1
            if (robotChassis.IsBlue()) {
                currentcolormode = color_mode_Blue;
            }


            if (robotChassis.IsRed()) {
                currentcolormode = color_mode_Red;
            }
            if (currentcolormode == 0) {
                CurrentStage = stage_150Done;
            } else {
                robotChassis.gripper.cmd_Close();
                robotChassis.stinger.cmdDoExtend();
                if (robotChassis.gripper.Is_Closed() &&
                        robotChassis.stinger.IsExtended()) {
                    CurrentStage = stage_40exce_jewels;

                }
            }
        }
        //ok boi
        if (CurrentStage == stage_40exce_jewels) {
            jewels.loop();
        }
        if (CurrentStage == stage_50Knockjewelloff) {
            if (robotChassis.getcmdComplete()) {
                CurrentStage = stage_60Return2Start;
            }
        }
        if (CurrentStage == stage_60Return2Start) {
            if (robotChassis.getGyroHeading() < 0) {
                robotChassis.cmdTurn(.5, -.5, 0);
            }

            if (robotChassis.getGyroHeading() > 0) {
                robotChassis.cmdTurn(.5, -.5, 0);
            }
            CurrentStage = stage_70Turn1;
        }
        if (CurrentStage == stage_70Turn1) {
            if (currentcolormode == color_mode_Red) {
                robotChassis.cmdTurn(.5, -.5, 90);
                CurrentStage = stage_80MoveFoward;
            }
            if (currentcolormode == color_mode_Blue) {
                robotChassis.cmdTurn(-.5, .5, -90);
                CurrentStage = stage_80MoveFoward;

            }
        }
        if (CurrentStage == stage_80MoveFoward) {
            robotChassis.cmdDrive(.4, 0, 26.5);
            CurrentStage = stage_90Turn2;


        }
        if (CurrentStage == stage_90Turn2) {
            if (currentcolormode == color_mode_Red) {
                robotChassis.cmdTurn(.5, -.5, 90);
                CurrentStage = stage_100MoveFoward7;
            }
            if (currentcolormode == color_mode_Blue) {
                robotChassis.cmdTurn(-.5, .5, -90);
                CurrentStage = stage_100MoveFoward7;

            }
        }
        if (CurrentStage == stage_100MoveFoward7) {
            robotChassis.cmdDrive(.4, 0, 7.5);
            CurrentStage = stage_110DropBlock;


        }

        if (CurrentStage == stage_110DropBlock) {
            robotChassis.gripper.cmd_Open();
            CurrentStage = stage_120MoveBack1;
        }

        if (CurrentStage == stage_120MoveBack1) {
            robotChassis.cmdDrive(-.5, 0, 12);
            CurrentStage = stage_150Done;
        }
        if (CurrentStage == stage_150Done) {
            robotChassis.stop();
            jewels.stop();
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        robotChassis.stop();
        jewels.stop();
    }


}

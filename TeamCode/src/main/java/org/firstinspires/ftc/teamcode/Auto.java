package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "Auto", group = "Concept")
public class Auto extends LinearOpMode {
    
    public Core robot = new Core();

    // //Define Navigation vars
    double gx;
    double gy;
    double inputdrift;
    double inputdirection;
    double inputspeed;
    // double imudrift;
    double imudirection;
    int startpos;
    // double imuspeed;
    double targetdirection;
    double finaldrift;
    boolean red = true;
    double grabopen = 1;
    // doublfe finaldirection;
    // double finalspeed;
    int state = 0;
    JewelDetector jewelDetector;
    ClosableVuforiaLocalizer vuforia;
    RelicRecoveryVuMark vumark;

    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        ClosableVuforiaLocalizer.Parameters parameters = new ClosableVuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AbcIKVj/////AAAAGYw/uMWceEEVrpzkW/beJ/UuODI5jYy4tvhvZXDQKMa+oFXbWzH7ca4hdBYnX1pImIlXRGYwH6QH45ZknzAQOnDuJ2LTGIjXgmyDDAWXeciL+9pRIo2Q5w/Eutg7Z7LfQs3wt8Mt0RNzHSev+P1tnmFMnGGowRFaQj6GJJOL64PxvP4x1W8DQk4pbjkCv5d7A6Kn5YwFM/KmC179VOJEZMBbupx2O9WMJ5Px+TO93gabXQH3NLhsPjjFr5bJjmFfNhLWLs/4NE6JARe0u/fu+NloB1SAp+w6AWHGR8k0pK9EnGyNf1SqbjrdPVTTPUZcfEaMUg8vXmHg56eIwat3uPXZGFcXXrloQlxO2PL+cgYU";

        parameters.cameraDirection = ClosableVuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = new ClosableVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        robot.init(hardwareMap, false);
            
        // Wait for the start button
        telemetry.addData(">", "Press X(blue) for blue alliance and B(red) for red alliance" );
        telemetry.update();
        while (!robot.red && !robot.blue) {
            if(gamepad1.x) {
                robot.blue = true;
                telemetry.addData(">", "Blue" );
            }
            if(gamepad1.b) {
                robot.red = true;
                telemetry.addData(">", "Red" );
            }
        }
        telemetry.update();
        init_jewel();
        
        waitForStart();
        relicTrackables.activate();

        robot.period = new ElapsedTime();
//        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.5);
        startpos = robot.lift.getCurrentPosition();

        //close grip
        close_grabber();
        robot.lift.setTargetPosition(startpos - 2000);
        while (opModeIsActive() && robot.lift.isBusy()) {}

        align(30, 3f);
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        while (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            vumark = vuMark;
        }
        align(-30, 3f);

        hit_jewel();
        jewelDetector.disable();

        telemetry.addData("Hitit", "");
        telemetry.update();

        wait_func(1);
        if(robot.red) {
            drivedirection(2500, 180, .25f, 10);
            align(90, 3f);
        }
        if(robot.blue) {
            drivedirection(2500, 0, .25f, 10);
            align(90, 3f);
        }


        while(opModeIsActive()) {}

        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public void drivedirection(int ticks, int direction, float speed, float time) {

        robot.dtl.setMode(DcMotor.RunMode.RESET_ENCODERS);
        robot.dtr.setMode(DcMotor.RunMode.RESET_ENCODERS);
        robot.dbr.setMode(DcMotor.RunMode.RESET_ENCODERS);
        robot.dbl.setMode(DcMotor.RunMode.RESET_ENCODERS);

        robot.dtr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.dbr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.dbl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.dtl.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.dtr.setTargetPosition((int)(Math.sin(Math.toRadians(45  + direction)) * ticks));
        robot.dbr.setTargetPosition((int)(Math.sin(Math.toRadians(135 + direction)) * ticks));
        robot.dbl.setTargetPosition((int)(Math.sin(Math.toRadians(225 + direction)) * ticks));
        robot.dtl.setTargetPosition((int)(Math.sin(Math.toRadians(315 + direction)) * ticks));

        robot.period = new ElapsedTime();

        while(opModeIsActive() &&
                robot.period.seconds() < time
//                Math.abs(robot.dtr.getCurrentPosition() - robot.dtr.getTargetPosition()) > 500 &&
//                Math.abs(robot.dbr.getCurrentPosition() - robot.dbr.getTargetPosition()) > 500 &&
//                Math.abs(robot.dbl.getCurrentPosition() - robot.dbl.getTargetPosition()) > 500 &&
//                Math.abs(robot.dtl.getCurrentPosition() - robot.dtl.getTargetPosition()) > 500
        ) {
            robot.dtr.setPower(Math.sin(Math.toRadians(inputdirection + 45  + direction)) * speed);
            robot.dbr.setPower(Math.sin(Math.toRadians(inputdirection + 135 + direction)) * speed);
            robot.dbl.setPower(Math.sin(Math.toRadians(inputdirection + 225 + direction)) * speed);
            robot.dtl.setPower(Math.sin(Math.toRadians(inputdirection + 315 + direction)) * speed);
            telemetry.addData("Motor: ", Math.abs(robot.dtr.getCurrentPosition() - robot.dtr.getTargetPosition()));
            telemetry.update();
        }
        telemetry.addData("Stopped: ", Math.abs(robot.dtr.getCurrentPosition() - robot.dtr.getTargetPosition()));
        telemetry.update();

        robot.dtr.setPower(0);
        robot.dbr.setPower(0);
        robot.dbl.setPower(0);
        robot.dtl.setPower(0);

        robot.dtr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.dbr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.dbl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.dtl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void align(int targetdir, float time) {
        imudirection = robot.update_imu();

        telemetry.addData("Direction: ", imudirection);
        double target = targetdir + imudirection;
        robot.period = new ElapsedTime();
        while (opModeIsActive() && robot.period.seconds() < time) {
            finaldrift = (target - imudirection) / 50;
            imudirection = robot.update_imu();

            robot.dtr.setPower(finaldrift / 1);
            robot.dbr.setPower(finaldrift / 1);
            robot.dbl.setPower(finaldrift / 1);
            robot.dtl.setPower(finaldrift / 1);

            //telemetry.addData("Direction: ", imudirection);
        }
        robot.dtr.setPower(0);
        robot.dbr.setPower(0);
        robot.dbl.setPower(0);
        robot.dtl.setPower(0);
    }

    public void init_jewel() {
        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 1);

        //Jewel Detector Settings
        jewelDetector.areaWeight = 0.02;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA; // PERFECT_AREA
        //jewelDetector.perfectArea = 6500; <- Needed for PERFECT_AREA
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;
        jewelDetector.rotateMat = true;

        jewelDetector.enable();
    }

    public void wait_func(float seconds) {
        robot.period = new ElapsedTime();
        while (opModeIsActive() && robot.period.seconds() < seconds) {

        }
    }

    public void knock_left() {
        align(15, .5f);
        align(-15, .5f);
    }

    public void knock_right() {
        align(-15, .5f);
        align(15, .5f);
    }

    public void lower_jewel_arm() {
        robot.jewel.setPosition(.87);
    }

    public void raise_jewel_arm() {
        robot.jewel.setPosition(0.25);
    }

    public String find_jewels() {
        robot.period = new ElapsedTime();
        while (opModeIsActive() && robot.period.seconds() < 2) {}
        return jewelDetector.getLastOrder().toString();
    }

    public void hit_jewel() {
        telemetry.addData("started", "");
        telemetry.update();
        String order = "";
        order = find_jewels();
        telemetry.addData(order, "");
        telemetry.update();
        lower_jewel_arm();
        wait_func(1);
        telemetry.addData(order, "");
        telemetry.update();
        if(robot.red) {
            if (order == "RED_BLUE") {
                knock_left();
            } else {
                knock_right();
            }
        }
        else {
            if (order == "RED_BLUE") {
                knock_right();
            } else {
                knock_left();
            }
        }
        raise_jewel_arm();
        jewelDetector.disable();
    }

    public void open_grabber() {
        robot.s1.setPosition(1 / 2 + 0.3f);
        robot.s2.setPosition(.9f - 1 / 2);
        robot.s3.setPosition(.9f - 1 / 2);
        robot.s4.setPosition(1 / 2 + 0.3f);
    }

    public void close_grabber() {
        robot.s1.setPosition(0 / 2 + 0.3f);
        robot.s2.setPosition(.9f - 0 / 2);
        robot.s3.setPosition(.9f - 0 / 2);
        robot.s4.setPosition(0 / 2 + 0.3f);
    }

    public void place_vumark_glyph() {
        if(vumark == RelicRecoveryVuMark.CENTER) {
            drivedirection(150, 0, .5f, 1);
            open_grabber();
            drivedirection(150, 180, .5f, 1);
        }
        if(vumark == RelicRecoveryVuMark.LEFT) {
            align(45, 1);
            drivedirection(250, 0, .5f, 1);
            open_grabber();
            drivedirection(250, 180, .5f, 1);
            align(-45, 1);
        }
        if(vumark == RelicRecoveryVuMark.RIGHT) {
            align(-45, 1);
            drivedirection(250, 0, .5f, 1);
            open_grabber();
            drivedirection(250, 180, .5f, 1);
            align(45, 1);
        }
        align(180, 2);
    }
}
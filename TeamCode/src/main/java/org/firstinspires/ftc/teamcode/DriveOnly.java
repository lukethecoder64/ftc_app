  package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Drive_alone", group = "Concept")
public class DriveOnly extends LinearOpMode {

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
    // double finaldirection;
    // double finalspeed;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, true);

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();

//        robot.lift.setTargetPosition(0);
//        startpos = robot.lift.getCurrentPosition();

        //main loop
        while(opModeIsActive()){

            gx = -gamepad1.left_stick_y;
            gy = gamepad1.right_stick_y;

//            imudirection = robot.update_imu();

            inputdrift = -gamepad1.left_stick_x / 2;
            inputdirection = Math.toDegrees(Math.atan2(gy, gx)) + 90;
            inputspeed = Math.sqrt(gx * gx + gy * gy);

            if(targetdirection >= 180) {
                targetdirection -= 360;
            }
            if(targetdirection <= -180) {
                targetdirection += 360;
            }

            targetdirection = (-gamepad1.right_stick_y) * 1;
            finaldrift = targetdirection;// - imudirection) / 50;

            robot.dtr.setPower(Math.sin(Math.toRadians(inputdirection + 45)) * inputspeed + finaldrift);
            robot.dbr.setPower(Math.sin(Math.toRadians(inputdirection + 135)) * inputspeed + finaldrift);
            robot.dbl.setPower(Math.sin(Math.toRadians(inputdirection + 225)) * inputspeed + finaldrift);
            robot.dtl.setPower(Math.sin(Math.toRadians(inputdirection + 315)) * inputspeed + finaldrift);

//            if(gamepad1.right_trigger <= 0.05) {
//                robot.s1.setPosition(.3);
//                robot.s2.setPosition(.9);
//            }
//            else if(gamepad1.right_trigger <= 0.33) {
//                robot.s1.setPosition(.4);
//                robot.s2.setPosition(.8);
//            }
//            else {
//                robot.s1.setPosition(.8);
//                robot.s2.setPosition(.4);
//            }
//            robot.s1.setPosition(.3 + gamepad1.right_trigger / 2); //left one
//            robot.s2.setPosition(.9 - gamepad1.right_trigger / 2); // right one

            // robot.s1.setPosition(0.65 + gamepad1.right_trigger / -2);
            // robot.s2.setPosition(0.85 - gamepad1.right_trigger / -2);

//            if(gamepad1.b) {
//                robot.relicgrab.setPosition(0.5);
//            }
//            else {
//                robot.relicgrab.setPosition(0.1);
////            }
//
//            robot.relicgrab.setPosition(gamepad1.left_trigger);
//
//            if(gamepad1.a) {
//                robot.relicarm.setTargetPosition(1300);
//            }
//            else {
//                robot.relicarm.setTargetPosition(0);
//            }
//            robot.relicarm.setPower(.33);
//
//            robot.lift.setPower(.65);
//            if(gamepad1.dpad_up && robot.lift.getTargetPosition() > -2100) {
//                robot.lift.setTargetPosition(robot.lift.getTargetPosition() - 80);
//            }
//            if(gamepad1.dpad_down && robot.lift.getTargetPosition() < 0) {
//                robot.lift.setTargetPosition(robot.lift.getTargetPosition() + 80);
//            }
//            if(gamepad1.x) {
//                robot.lift.setTargetPosition(0);
//            }
            // finaldrift = (targetdirection - imudirection) * (targetdirection - imudirection) * (targetdirection - imudirection) / 10000;
            // finaldirection = inputdirection;// + imudirection;
            // finalspeed = inputspeed + imuspeed;

            // dtr.setTargetPosition(-4000);
            // dtr.setPower(1);

            // dtr.setPower(1);

            // if(finaldrift != 0) {
            //     // direction
            // }

            // finaldrift = gamepad1.left_stick_x * 3;

            // dtr.setPower(Math.sin(Math.toRadians(finaldirection + 45)) * finalspeed + finaldrift);
            // dbr.setPower(-Math.sin(Math.toRadians(finaldirection + 135)) * finalspeed - finaldrift);
            // dbl.setPower(Math.sin(Math.toRadians(finaldirection + 225)) * finalspeed + finaldrift);
            // dtl.setPower(Math.sin(Math.toRadians(finaldirection + 315)) * finalspeed + finaldrift);

            // telemetry.addData("dtl",dtl.getPower());
            // telemetry.addData("dtr",dtr.getPower());
            // telemetry.addData("dbl",dbl.getPower());
//            telemetry.addData("TargetDir", targetdirection);
//            telemetry.addData("Direction: ", imudirection);
//            telemetry.addData("Speed2: ", robot.lift.getCurrentPosition());
            // telemetry.addData("IMU Dir", formatAngle(angles.angleUnit, angles.firstAngle));
            telemetry.update();
            idle();
        }

        // Signal done;

        telemetry.addData(">", "Done");
        telemetry.update();
    }
}
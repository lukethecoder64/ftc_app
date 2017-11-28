package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    @Override
    public void runOpMode() {
        
        robot.init(hardwareMap);
        robot.s1.setPosition(.4 + 0 / 2); //left one
        robot.s2.setPosition(1 - 0 / 2); // right one
            
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
        
        waitForStart();
        robot.period = new ElapsedTime();
        robot.lift.setTargetPosition(0);
        robot.lift.setPower(.5);
        startpos = robot.lift.getCurrentPosition();
        
        //main loop
        while(opModeIsActive()){
            
            // robot.s1.setPosition(.4 + grabopen / 2); //left one
            // robot.s2.setPosition(1 - grabopen / 2); // right one
            
            if(robot.period.seconds() < 4) {
                state = 1;
            }
            else if(robot.period.seconds() < 10) {
                state = 2;
            }
            else if(robot.period.seconds() < 12) {
                state = 3;
            }
            else if(robot.period.seconds() < 14) {
                state = 4;
            }
            else if(robot.period.seconds() < 16) {
                state = 5;
            }
            else if(robot.period.seconds() < 20) {
                state = 6;
            }
            else if(robot.period.seconds() < 21) {
                state = 7;
            }
            else if(robot.period.seconds() < 24) {
                state = 8;
            }
            else {
                state = 0;
            }

            switch (state) {
                case 0:
                    robot.lift.setTargetPosition(0);
                    inputspeed = 0;
                case 1:
                    robot.lift.setTargetPosition(-1500);
                    robot.s1.setPosition(.4);
                    robot.s2.setPosition(1);
                    targetdirection = 0;
                    inputdirection = 0;
                    inputspeed = 0;
                case 2:
                    targetdirection = 0;
                    inputdirection = 0;
                    inputspeed = 1;
                case 3:
                    targetdirection = 0;
                    inputdirection = 180;
                    inputspeed = .5;
                case 4:
                    targetdirection = 0;
                    inputdirection = 0;
                    inputspeed = 1;
                case 5:
                    targetdirection = -90;
                    inputdirection = 0;
                    inputspeed = 0;
                case 6:
                    targetdirection = -90;
                    inputdirection = 0;
                    inputspeed = 0.33;
                case 7:
                    robot.s1.setPosition(.9);
                    robot.s2.setPosition(.5);
                    targetdirection = -90;
                    inputdirection = 0;
                    inputspeed = 0;
                case 8:
                    targetdirection = -90;
                    inputdirection = 180;
                    inputspeed = 0.5;
            }
            
            // inputdrift = -gamepad1.left_stick_x / 2;
            // inputdirection = Math.toDegrees(Math.atan2(gy, gx)) + 90;
            // inputspeed = Math.sqrt(gx * gx + gy * gy);
            
            imudirection = robot.update_imu();
            
            if(targetdirection >= 180) {
                targetdirection -= 360;
            }
            if(targetdirection <= -180) {
                targetdirection += 360;
            }
            
            finaldrift = (targetdirection - imudirection) / 50;
            
            robot.dtr.setPower(Math.sin(Math.toRadians(inputdirection + 45)) * inputspeed + finaldrift);
            robot.dbr.setPower(Math.sin(Math.toRadians(inputdirection + 135)) * inputspeed + finaldrift);
            robot.dbl.setPower(Math.sin(Math.toRadians(inputdirection + 225)) * inputspeed + finaldrift);
            robot.dtl.setPower(Math.sin(Math.toRadians(inputdirection + 315)) * inputspeed + finaldrift);
            
            
            
            robot.relicgrab.setPosition(0.23);
            robot.relicarm.setPower(.33);
            
            robot.lift.setPower(.45);
            
            telemetry.addData("TargetDir", targetdirection);
            telemetry.addData("Direction: ", robot.formatAngle(robot.angles.angleUnit, robot.angles.secondAngle));
            telemetry.addData("Grabopen: ", grabopen);
            telemetry.update();
            idle();
        }

        // Signal done;
        
        telemetry.addData(">", "Done");
        telemetry.update();
    }

}
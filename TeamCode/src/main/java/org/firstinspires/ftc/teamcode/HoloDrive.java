package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.hardware.DcMotorController;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.DriveState;

import org.firstinspires.ftc.teamcode.Core;

@TeleOp(name = "Drive", group = "Concept")
public class HoloDrive extends LinearOpMode {
    
    public Core robot = new Core();

    // //Define Navigation vars
    double gx;
    double gy;
    double inputdrift;
    double inputdirection;
    double inputspeed;
    double imudirection;
    int startpos;
    double targetdirection;
    double finaldrift;
    DriveState driveState;

    @Override
    public void runOpMode() {
        
        robot.init(hardwareMap, false);
        
        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();

        initRobot();
        //main loop
        while(opModeIsActive()){
            if(driveState == DriveState.CLIMB_RAMP_AUTO) {
                climbRamp();
            }
            if(driveState == DriveState.NORMAL_DRIVE || driveState == DriveState.SLOW_DRIVE) {
                if (gamepad1.y) { //climb ramp
                    driveState = DriveState.CLIMB_RAMP_AUTO;
                    robot.period = new ElapsedTime();
                }
                if (gamepad1.x) {

                    switch(driveState){
                        case SLOW_DRIVE:
                            driveState = DriveState.NORMAL_DRIVE;
                            break;
                        case NORMAL_DRIVE:
                            driveState = DriveState.SLOW_DRIVE;
                            break;
                    }

//                    if(driveState == DriveState.SLOW_DRIVE) {
//                        driveState = DriveState.NORMAL_DRIVE;
//                    }
//                    if(driveState == DriveState.NORMAL_DRIVE) {
//                        driveState = DriveState.SLOW_DRIVE;
//                    }
                }

                gx = -gamepad1.right_stick_x;
                gy = gamepad1.right_stick_y;

                imudirection = robot.update_imu();
//
//                inputdrift = -gamepad1.left_stick_x / 2;
                inputdirection = Math.toDegrees(Math.atan2(gy, gx)) + 90;

                switch(driveState) {
                    case SLOW_DRIVE:
                        inputspeed = (Math.sqrt(gx * gx + gy * gy)) * .30;
                        finaldrift = (-gamepad1.left_stick_x)       * .30;
                        break;
                    case NORMAL_DRIVE:
                        inputspeed = (Math.sqrt(gx * gx + gy * gy));
                        finaldrift = (-gamepad1.left_stick_x) * 1;
                        break;

                }

//                if(driveState == DriveState.SLOW_DRIVE){
//                    inputspeed = (Math.sqrt(gx * gx + gy * gy)) * .30;
//                }
//                else {
//                    inputspeed = (Math.sqrt(gx * gx + gy * gy));
//                }

                if (targetdirection >= 180) {
                    targetdirection -= 360;
                }
                if (targetdirection <= -180) {
                    targetdirection += 360;
                }

//                targetdirection = ;
                // - imudirection) / 50;

                robot.dtr.setPower(Math.sin(Math.toRadians(inputdirection + 45)) * inputspeed + finaldrift);
                robot.dbr.setPower(Math.sin(Math.toRadians(inputdirection + 135)) * inputspeed + finaldrift);
                robot.dbl.setPower(Math.sin(Math.toRadians(inputdirection + 225)) * inputspeed + finaldrift);
                robot.dtl.setPower(Math.sin(Math.toRadians(inputdirection + 315)) * inputspeed + finaldrift);

                robot.s1.setPosition(gamepad1.right_trigger / 2 + 0.3f);
                robot.s2.setPosition(.9f - gamepad1.right_trigger / 2);
                robot.s3.setPosition(.9f - gamepad1.right_trigger / 2);
                robot.s4.setPosition(gamepad1.right_trigger / 2 + 0.3f);

                //robot.jewel.setPosition(gamepad1.right_trigger / 2);

                robot.relicgrab.setPosition(1f - gamepad1.left_trigger / 2);
                if(gamepad1.a) {
                    if (gamepad1.dpad_up) {
                        robot.relicarm.setTargetPosition(robot.relicarm.getCurrentPosition() + 200);
                    }
                    if (gamepad1.dpad_down) {
                        robot.relicarm.setTargetPosition(robot.relicarm.getCurrentPosition() - 200);
                    }
                    if(!gamepad1.dpad_up && !gamepad1.dpad_down) {
                        robot.relicarm.setTargetPosition(robot.relicarm.getCurrentPosition());
                    }
                    robot.relicarm.setPower(.66);
                }
                else {
                    robot.lift.setPower(1);
                    if (gamepad1.dpad_up && robot.lift.getTargetPosition() > -4200) {
                        robot.lift.setTargetPosition(robot.lift.getTargetPosition() - 100);
                    }
                    if (gamepad1.dpad_down && robot.lift.getTargetPosition() < 0) {
                        robot.lift.setTargetPosition(robot.lift.getTargetPosition() + 100);
                    }
                    if(!gamepad1.dpad_down && !gamepad1.dpad_up) {
                        robot.lift.setTargetPosition(robot.lift.getCurrentPosition());
                    }
                    if (gamepad1.x) {
                        robot.lift.setTargetPosition(0);
                    }
                }
                telemetry.addData("Drive State: ", driveState);
                telemetry.addData("TargetDir: ",     targetdirection);
                telemetry.addData("Direction: ",   imudirection);
                telemetry.addData("Speed2: ",      robot.lift.getCurrentPosition());
                // telemetry.addData("IMU Dir", formatAngle(angles.angleUnit, angles.firstAngle));
                telemetry.update();
                idle();
            }
        }// Signal done;
        
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public void initRobot() {
        robot.lift.setTargetPosition(0);
        startpos = robot.lift.getCurrentPosition();

        gamepad1.setJoystickDeadzone(1);

        driveState = DriveState.NORMAL_DRIVE;
    }

    public void climbRamp() {

        if(robot.period.seconds() < .5) {
            robot.dtr.setPower(1);
            robot.dbr.setPower(1);
            robot.dbl.setPower(1);
            robot.dtl.setPower(1);
        }
        else if(robot.period.seconds() < 1) {
            robot.dtr.setPower(0);
            robot.dbr.setPower(0);
            robot.dbl.setPower(0);
            robot.dtl.setPower(0);
        }
        else if(robot.period.seconds() < 1.5) {
            robot.dtr.setPower(0);
            robot.dbr.setPower(1);
            robot.dbl.setPower(0);
            robot.dtl.setPower(-1);
        }
        else {
            robot.dtr.setPower(0);
            robot.dbr.setPower(0);
            robot.dbl.setPower(0);
            robot.dtl.setPower(0);
            driveState = DriveState.NORMAL_DRIVE;
        }
    }
}
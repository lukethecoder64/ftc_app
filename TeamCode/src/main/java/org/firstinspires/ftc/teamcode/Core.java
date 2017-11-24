package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import java.util.Locale;
import com.qualcomm.robotcore.util.Hardware;
import java.util.Map;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Core
{
    public double wheelDiam = 3;
    public double wheelCurc = 3.14159 * wheelDiam;
    public double ticksPerRev = 1120;
    public double inchPerRev = wheelCurc * ticksPerRev / wheelCurc;
    
    /* Public OpMode members. */
    public DcMotor dtl;
    public DcMotor dtr;
    public DcMotor dbl;
    public DcMotor dbr;
    
    public DcMotor lift;
    public DcMotor relicarm;
    
    public Servo jewel;
    public Servo relicgrab;
    public Servo s1;
    public Servo s2;
    
    public AnalogInput pixya;
    public AnalogInput pixyb;
    
    //Define IMU + Vars
    public BNO055IMU imu;
    public Orientation angles;
    public Acceleration gravity;

    //Define Navigation vars
    double gx;
    double gy;
    double inputdrift;
    double inputdirection;
    double inputspeed;
    double imudrift;
    double imudirection;
    double imuspeed;
    double targetdirection;
    double finaldrift;
    double finaldirection;
    double finalspeed;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    public ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Core() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        
        dtl = hwMap.get(DcMotor.class, "drive top left");
        dtr = hwMap.get(DcMotor.class, "drive top right");
        dbl = hwMap.get(DcMotor.class, "drive bottom left");
        dbr = hwMap.get(DcMotor.class, "drive bottom right");
        
        dtl.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        dtr.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        dbl.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        dbr.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        
        dtl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dtr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dbl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dbr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        lift = hwMap.get(DcMotor.class, "lift");
        lift.setMode(DcMotor.RunMode.RESET_ENCODERS);
        while (lift.getCurrentPosition() != 0) {}
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        relicarm = hwMap.get(DcMotor.class, "relic");
        relicarm.setMode(DcMotor.RunMode.RESET_ENCODERS);
        while (relicarm.getCurrentPosition() != 0) {}
        relicarm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        pixya = hwMap.get(AnalogInput.class, "pixya");
        pixyb = hwMap.get(AnalogInput.class, "pixyb");
        
        jewel = hwMap.get(Servo.class, "jewel");
        jewel.setPosition(1);
        
        s1 = hwMap.get(Servo.class, "s1");
        s2 = hwMap.get(Servo.class, "s2");
        
        relicgrab = hwMap.get(Servo.class, "relicgrab");
        
        BNO055IMU.Parameters parameters= new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        
        
        
        // dtl.setMode(DcMotor.RunMode.RESET_ENCODERS);
        // dtr.setMode(DcMotor.RunMode.RESET_ENCODERS);
        // dbr.setMode(DcMotor.RunMode.RESET_ENCODERS);
        // dbl.setMode(DcMotor.RunMode.RESET_ENCODERS);

        // dtl.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        // dtr.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        // dbl.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        // dbr.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
    }
    
    public void drive(double inputdirection, double inputspeed) {
        // dtr.setPower(Math.sin(Math.toRadians(inputdirection + 45)) * finalspeed + finaldrift);
        // dbr.setPower(-Math.sin(Math.toRadians(inputdirection + 135)) * finalspeed - finaldrift);
        // dbl.setPower(Math.sin(Math.toRadians(inputdirection + 225)) * finalspeed + finaldrift);
        // dtl.setPower(Math.sin(Math.toRadians(inputdirection + 315)) * finalspeed + finaldrift);
        
    }
    
    double update_imu() {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
        return Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
    }
    
    public String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    public String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
 }


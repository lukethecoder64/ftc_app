package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/*
 * This is an example LinearOpMode that shows how to use the digital inputs and outputs on the
 * the Modern Robotics Device Interface Module.  In addition, it shows how to use the Red and Blue LED
 *
 * This op mode assumes that there is a Device Interface Module attached, named 'dim'.
 * On this DIM there is a digital input named 'digin' and an output named 'digout'
 *
 * To fully exercise this sample, connect pin 3 of the digin connector to pin 3 of the digout.
 * Note: Pin 1 is indicated by the black stripe, so pin 3 is at the opposite end.
 *
 * The X button on the gamepad will be used to activate the digital output pin.
 * The Red/Blue LED will be used to indicate the state of the digital input pin.
 * Blue = false (0V), Red = true (5V)
 * If the two pins are linked, the gamepad will change the LED color.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
*/
@Autonomous(name = "PixyTesting", group = "Sensor")

public class PixyTest extends LinearOpMode {

final int BLUE_LED_CHANNEL = 0;
final int RED_LED_CHANNEL = 1;

  @Override
  public void runOpMode() {
    
    Core robot = new Core();
    
    robot.init(hardwareMap);
    // robot.jewel.setPosition()
    

    
    // // get a reference to a Modern Robotics DIM, and IO channels.
    // dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");   //  Use generic form of device mapping
    // digIn  = hardwareMap.get(DigitalChannel.class, "digin");     //  Use generic form of device mapping
    // digOut = hardwareMap.get(DigitalChannel.class, "digout");    //  Use generic form of device mapping

    // digIn.setMode(DigitalChannel.Mode.INPUT);          // Set the direction of each channel
    // digOut.setMode(DigitalChannel.Mode.OUTPUT);

    // wait for the start button to be pressed.
    telemetry.addData(">", "Press play, and then user X button to set DigOut");
    telemetry.update();
    waitForStart();
    
    robot.jewel.setPosition(.35);

    while (opModeIsActive())  {

        // outputPin = gamepad1.x ;        //  Set the output pin based on x button
        // digOut.setState(outputPin);
        // inputPin = digIn.getState();    //  Read the input pin

        // // Display input pin state on LEDs
        // if (inputPin) {
        //     dim.setLED(RED_LED_CHANNEL, true);
        //     dim.setLED(BLUE_LED_CHANNEL, false);
        // }
        // else {
        //     dim.setLED(RED_LED_CHANNEL, false);
        //     dim.setLED(BLUE_LED_CHANNEL, true);
        // }
        if(robot.pixyb.getVoltage() > 1.65) {
            telemetry.addData("on the ", "right");
        }
        else {
            telemetry.addData("on the ", "left");
        }
        telemetry.addData("A:", robot.pixya.getVoltage());
        telemetry.addData("B:", robot.pixyb.getVoltage());
        telemetry.update();
    }
    robot.jewel.setPosition(1);
  }
}

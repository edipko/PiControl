package com.dipko.rasberrypi;


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by edipko on 6/19/16.
 */
public class PiControlClass {

    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();

    // provision gpio pin #01 & #03 as an output pins and blink
    final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);

    /**
     * Connect to an LED attached to the default GPIO pin (6).
     *
     */
    public PiControlClass()
    {
    }

    /**
     * Turn on the LED.
     *
     */
    public String on() {

        File tmpFile = new File("/tmp/picontrol");
        if (tmpFile.exists()) {
            // Wait 15 seconds before allow the pump to be turned back on
            if (tmpFile.lastModified() < System.currentTimeMillis()-15000) {
                executeOn();
                tmpFile.setLastModified(System.currentTimeMillis());
                return String.valueOf(tmpFile.lastModified() < System.currentTimeMillis()-15000);
            } else {
                // This is to protect the motor from being switch on/off too quickly
                System.out.println("Must wait to turn on");
                return "Must wait";
            }
        } else {
            System.out.println("No temp file, creating");
            try {
                tmpFile.createNewFile();
                executeOn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "On";
    }

    private void executeOn() {
        led1.high();
    }
    /**
     * Turn off the LED.
     *
     */
    public void off() {
        // Sets the LED pin state to (low)
        led1.low();
    }

    /**
     * Checks if the the LED is on.
     *
     */
    public boolean isOn()
    {
        // Check the pin state
        return led1.isHigh();
    }
}
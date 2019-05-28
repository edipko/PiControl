package com.dipko.rasberrypi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by edipko on 6/19/16.
 */

public class CheckSchedule implements Runnable {
    PiControlClass pcc = BackgroundJobManager.pcc;

    @Override
    public void run() {
        // Do your daily job here.
       // PiControlClass pcc = new PiControlClass();

        BackgroundJobManager.readProperties();
        Properties props = BackgroundJobManager.prop;

        DateTime utc = new DateTime(DateTimeZone.UTC);
        DateTimeZone tz = DateTimeZone.forID("America/New_York");
        DateTime dt = utc.toDateTime(tz);

        int hour = dt.getHourOfDay();
        int minute = dt.getMinuteOfHour();
        //System.out.println("Time is: " + hour + ":" + minute);

        // Determine if the schedule is disabled (via checkbox)
        boolean useSchedule = true;
        if (props.containsKey("scheduleToggle")) {
                useSchedule = false;
        }

        if (useSchedule) {
            for (Map.Entry<?, ?> entry : props.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String delims = "[:]+";
                String[] time = value.split(delims);
                int scheduleHour = Integer.valueOf(time[0]);
                int scheduleMinute = Integer.valueOf(time[1]);


                if ((hour == scheduleHour) && (minute == scheduleMinute)) {
                    System.out.println("Timer match");
                    if (key.contains("on")) {
                        if (!pcc.isOn()) {
                            pcc.on();
                            System.out.println("Setting ON");
                        }
                    } else {
                        if (pcc.isOn()) {
                            pcc.off();
                            System.out.println("Setting OFF");
                        }
                    }
                } else {
                    //System.out.println("No match: " + scheduleHour + ":" + scheduleMinute);
                }

            }
        } else {
            System.out.println("Schedule is disabled");
        }

        // Get the temperature and log to file every 15 minutes
        if (minute == 0) {

            String line;
            String delims = "=";
            Double temp1 = 0.0;
            Double temp1_f = 0.0;
            Double temp2_f = 0.0;
            String histFileName = "/var/log/temphistory.txt";

            try {
            String txtFilePath = "/sys/bus/w1/devices/28-800000281f91/w1_slave";
            File file1 = new File(txtFilePath);
            if (file1.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("t=")) {
                        temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
                    }
                }
                temp1_f = (temp1 * 9 / 5) + 32;

                reader.close();
            }

            txtFilePath = "/sys/bus/w1/devices/28-800000282739/w1_slave";
            File file2 = new File(txtFilePath);
            if (file2.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("t=")) {
                        temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
                    }
                }
                temp2_f = (temp1 * 9 / 5) + 32;

                reader.close();
            }


                PrintWriter pw = new PrintWriter(new FileOutputStream(histFileName));
                pw.println(dt + "|" + temp1_f + "|" + temp2_f);
                pw.close();
            } catch(IOException e) {

            }
        }

    }

}
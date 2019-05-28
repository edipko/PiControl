package com.dipko.rasberrypi;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by edipko on 6/19/16.
 */



@WebListener
public class BackgroundJobManager implements ServletContextListener {


    public static ServletContext context;
    public static ScheduledExecutorService scheduler;
    public static SortedProperties prop = new SortedProperties();
    public static PiControlClass pcc = new PiControlClass();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        context = event.getServletContext();

        // Read the properties file
        readProperties();

        // Start a scheduler to periodically check time (like cron)
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new CheckSchedule(), 0, 30, TimeUnit.SECONDS);
    }


    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }


    public static SortedProperties readProperties() {
        System.out.println("Reading properties file");
        // Read the properties file
        prop = new SortedProperties();
        InputStream input = null;
        try {
            input = context.getResourceAsStream("/WEB-INF/config.properties");
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return prop;
        }
    }


}


package com.dipko.rasberrypi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by edipko on 6/21/16.
 */
@WebServlet(name = "PropertiesServlet")
public class PropertiesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        System.out.println("Properties Servlet called: ");

        Enumeration<String> parameterNames = request.getParameterNames();
        SortedProperties prop = new SortedProperties();
        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println("Parameters: " + paramName + "|" + paramValue);
            prop.setProperty(paramName, paramValue);
        }


        OutputStream output = null;
        // Save properties file in case changes were submitted
        try {

            URL url = BackgroundJobManager.context.getResource("/WEB-INF/config.properties");
            String path = url.getPath();
            if (prop.containsKey("on1")) {
                System.out.println("Servlet - Saving properties");
                output = new FileOutputStream(path);
                prop.store(output, null);
                output.close();
            }
            // Update memory with new properties
            BackgroundJobManager.readProperties();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        request.setAttribute("pumpStatus", BackgroundJobManager.pcc.isOn());
        request.setAttribute("prop", BackgroundJobManager.prop); // so that you can retrieve in JSP
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

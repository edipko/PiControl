<%--
  Created by IntelliJ IDEA.
  User: edipko
  Date: 6/17/16
  Time: 9:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
  <head>
    <title>PiControl</title>
    <link rel="stylesheet" href="/PiControl/css/bootstrap.min.css" />

    <style>
      input[type="text"][disabled] {
        color: lightgray;
      }
    </style>


  </head>

  <body>




  <div class="jumbotron">

    <%
      String txtFilePath = "/sys/bus/w1/devices/28-800000281f91/w1_slave";
      BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
      String line;
      String delims = "=";
      Double temp1 = 0.0;

      while((line = reader.readLine())!= null){
        if (line.contains("t=")) {
          temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
        }
      }
      Double temp1_f = (temp1 * 9 / 5) + 32;
      out.println("Air Temperature: " + temp1_f);
      reader.close();


      txtFilePath = "/sys/bus/w1/devices/28-800000282739/w1_slave";
      reader = new BufferedReader(new FileReader(txtFilePath));
      while((line = reader.readLine())!= null){
        if (line.contains("t=")) {
          temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
        }
      }
      Double temp2_f = (temp1 * 9 / 5) + 32;
      out.println("<br/>Water Temperature: " + temp2_f);
      reader.close();

    %>


  <form action="${pageContext.request.contextPath}/controlservlet" method="post">


    <button id="on" name="on" value="click" class="btn btn-lg btn-success">Turn On</button>
    <button id="off" name="off" value="click" class="btn btn-lg btn-danger">Turn Off</button>
    <button id="refresh" name="refresh" value="click" class="btn btn-lg btn-primary">Refresh Data</button>
  </form>

  The pump on: <%= request.getAttribute("pumpStatus") %>.


  <br/>
  <br/>

  <b>Pool Pump Schedule</b>

  <form action="${pageContext.request.contextPath}/propertiesservlet" method="post">
    <br/>

    <c:choose>
      <c:when test="${prop['scheduleToggle'] == 'on'}">
        <input type="checkbox" name="scheduleToggle" id="scheduleToggle" checked>Disable Schedule
      </c:when>
      <c:otherwise>
        <input type="checkbox" name="scheduleToggle" id="scheduleToggle">Disable Schedule
      </c:otherwise>
    </c:choose>


    <br/>
    <table>
      <th>
        <td colspan="1">On Time</td>
        <td colspan="1">&nbsp</td>
        <td colspan="1">Off Time</td>
      </th>

      <tr>
        <td>Schedule 1</td>
        <td><input type="text" name="on1" value="${prop['on1']}"></td>
        <td colspan="1">&nbsp</td>
        <td><input type="text" name="off1" value="${prop['off1']}"></td>
      </tr>

      <tr>
        <td>Schedule 2</td>
        <td><input type="text" name="on2" value="${prop['on2']}"></td>
        <td colspan="1">&nbsp</td>
        <td><input type="text" name="off2" value="${prop['off2']}"></td>
      </tr>

      <tr>
        <td>Schedule 3</td>
        <td><input type="text" name="on3" value="${prop['on3']}"></td>
        <td colspan="1">&nbsp</td>
        <td><input type="text" name="off3" value="${prop['off3']}"></td>
      </tr>


    </table>
    <input type="submit" id="submitProperties" value="Change Schedule" class="btn btn-lg btn-primary"/>

    </form>


</div>

  </body>

  <script src="/PiControl/js/jquery-2.1.4.js"></script>
  <script src="/PiControl/js/bootstrap.min.js" ></script>
  <script src="/PiControl/js/index.js?ver=2"></script>
</html>

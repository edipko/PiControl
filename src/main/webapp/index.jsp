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
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
  <meta name="viewport" content=" initial-scale=1">
  <title>PiControl</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

  <style>
    div {
      font-size: 24px;
      line-height: 2;
    }
    input[type="text"][disabled] {
      color: lightgray;
    }
    .btn-block {
      height: 60px !important;
    }
  </style>


</head>

<body>

<div class="container">


  <%


    String line;
    String delims = "=";
    Double temp1 = 0.0;
    String txtFilePath = "/sys/bus/w1/devices/28-01143ca246aa/w1_slave";
    File file = new File(txtFilePath);
    Double temp1_f = 0.0;
    if (file.exists()) {
      BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));

      while((line = reader.readLine())!= null){
        if (line.contains("t=")) {
          temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
        }
      }
      temp1_f = (temp1 * 9 / 5) + 32;
      reader.close();
    }

    txtFilePath = "/sys/bus/w1/devices/28-01143d0f08aa/w1_slave";
    file = new File(txtFilePath);
    Double temp2_f = 0.0;


    if (file.exists()) {

      BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
      while((line = reader.readLine())!= null){
        if (line.contains("t=")) {
          temp1 = Double.valueOf(line.split(delims)[1]) / 1000;
        }
      }
      temp2_f = (temp1 * 9 / 5) + 32;
      reader.close();
    }

  %>

  Air Temperature: <strong> <fmt:formatNumber type="number" maxFractionDigits="2" value="<%=temp1_f%>" /> </strong>
  <br/>
  Water Temperature: <strong> <fmt:formatNumber type="number" maxFractionDigits="2" value="<%=temp2_f%>" /> </strong>


  <div class="row">
    <form action="${pageContext.request.contextPath}/controlservlet" method="post">

      <div class="col-xs-4">
        <button id="on" name="on" value="click" class="btn btn-lg btn-success">Turn On</button>
      </div>

      <div class="col-xs-4">
        <button id="off" name="off" value="click" class="btn btn-lg btn-danger">Turn Off</button>
      </div>

      <div class="col-xs-4">
        <button id="refresh" name="refresh" value="click" class="btn btn-lg btn-primary">Refresh Data</button>
      </div>
    </form>
  </div>

  <br/>
  The pump on: <font color="blue"><%= request.getAttribute("pumpStatus") %></font>
  <br/>


  <b>Pool Pump Schedule</b>

  <form action="${pageContext.request.contextPath}/propertiesservlet" method="post">
    <c:choose>
      <c:when test="${prop['scheduleToggle'] == 'on'}">
        <input type="checkbox" name="scheduleToggle" id="scheduleToggle" checked>Disable Schedule
      </c:when>
      <c:otherwise>
        <input type="checkbox" name="scheduleToggle" id="scheduleToggle">Disable Schedule
      </c:otherwise>
    </c:choose>

    <table class="table table-condensed">
      <tr>
        <td>&nbsp;</td>
        <td>On Time</td>
        <td>Off Time</td>
      </tr>

      <tr>
        <td>Schedule 1</td>
        <td><input type="text" name="on1" value="${prop['on1']}"></td>
        <td><input type="text" name="off1" value="${prop['off1']}"></td>
      </tr>

      <tr>
        <td>Schedule 2</td>
        <td><input type="text" name="on2" value="${prop['on2']}"></td>
        <td><input type="text" name="off2" value="${prop['off2']}"></td>
      </tr>

      <tr>
        <td>Schedule 3</td>
        <td><input type="text" name="on3" value="${prop['on3']}"></td>
        <td><input type="text" name="off3" value="${prop['off3']}"></td>
      </tr>


    </table>
    <input type="submit" id="submitProperties" value="Change Schedule" class="btn btn-block btn-primary"/>

  </form>

</div>

</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="/PiControl/js/index.js?ver=2"></script>



</html>
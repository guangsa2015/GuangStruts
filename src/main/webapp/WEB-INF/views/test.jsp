<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %><%--
  Created by IntelliJ IDEA.
  User: poju
  Date: 2025/8/3
  Time: 下午3:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Date date = Calendar.getInstance().getTime();
    String nowdate=new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(date);
%>

当前时间：<%=nowdate%>
</body>
</html>

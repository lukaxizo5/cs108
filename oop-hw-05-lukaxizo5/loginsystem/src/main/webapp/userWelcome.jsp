<%--
  Created by IntelliJ IDEA.
  User: xizusha
  Date: 6/8/25
  Time: 12:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome <%=request.getAttribute("username")%></title>
</head>
<body>
<h1>Welcome <%=request.getAttribute("username")%></h1>
</body>
</html>

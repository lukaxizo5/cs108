<%--
  Created by IntelliJ IDEA.
  User: xizusha
  Date: 6/8/25
  Time: 12:40 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Account</title>
</head>
<body>
<h1>The Name <%=request.getAttribute("username")%> is Already In Use.</h1>
<p>Please enter another name and password.</p>
<form action="register" method = "post">
    Username: <input type="text" name="username" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    <input type="submit" value="Login">
</form>
</body>
</html>

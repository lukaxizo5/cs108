<%--
  Created by IntelliJ IDEA.
  User: xizusha
  Date: 6/8/25
  Time: 12:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Information Incorrect</title>
</head>
<body>
<h1>Please Try Again</h1>
<p>Either your username or password is incorrect. Please try again.</p>
<form action="login" method = "post">
    Username: <input type="text" name="username" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    <input type="submit" value="Login">
</form>
<a href="register">Create New Account</a>
</body>
</html>

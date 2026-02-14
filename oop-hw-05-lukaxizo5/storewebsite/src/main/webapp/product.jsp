<%--
  Created by IntelliJ IDEA.
  User: xizusha
  Date: 6/9/25
  Time: 7:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product" %>
<%@ page import="dao.ProductDAO" %>

<%
    String id = request.getParameter("id");
    ProductDAO productDAO = (ProductDAO) application.getAttribute("productDAO");
    Product product = productDAO.getProductById(id);
    String name = product.getName();
%>
<html>
<head>
    <title><%= name %></title>
</head>
<body>
    <h1><%= name %></h1>
    <img src="store-images/<%= product.getImageFile() %>" alt="<%= name %>"><br>
    <form action="addToCart" method="post">
        $<%= product.getPrice() %>
        <input type="hidden" name="id" value="<%= id %>">
        <input type="submit" value="Add to Cart">
    </form>
</body>
</html>

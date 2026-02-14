<%--
  Created by IntelliJ IDEA.
  User: xizusha
  Date: 6/9/25
  Time: 8:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.ShoppingCart" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.Map" %>

<%
    ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
    ProductDAO dao = (ProductDAO) application.getAttribute("productDAO");
%>
<html>
<head>
    <title>Shopping Cart</title>
</head>
<body>
<h1>Shopping Cart</h1>
<form action="updateCart" method="post">
    <ul>
        <%
            double total = 0;
            for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
                String productId = entry.getKey();
                Integer amount = entry.getValue();
                Product product = dao.getProductById(productId);
                double itemTotal = product.getPrice() * amount;
                total += itemTotal;
                if (amount <= 0) continue;
        %>
        <li>
            <input type="number" name="<%= productId %>" value="<%= amount %>">
            <%= product.getName()%>, <%= product.getPrice() %>
        </li>
        <%
            }
        %>
    </ul>
    <p><strong>Total: $ <%=String.format("%.2f", total)%></strong></p>
    <input type="submit" value="Update Cart">
</form>
<br>
<a href="index.jsp">Continue Shopping</a>
</body>
</html>
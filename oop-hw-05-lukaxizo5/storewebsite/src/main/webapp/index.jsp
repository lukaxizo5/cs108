<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>
<p>Items Available:</p>
<ul>
    <%
        ProductDAO dao = (ProductDAO) application.getAttribute("productDAO");
        List<Product> products = dao.getAllProducts();
        for (Product product : products) {
    %>
    <li>
        <a href="product.jsp?id=<%= product.getProductId() %>">
            <%= product.getName() %>
        </a>
    </li>
    <%
        }
    %>
</ul>
</body>
</html>
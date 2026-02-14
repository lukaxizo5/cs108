package servlet;

import model.ShoppingCart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            request.getSession().setAttribute("shoppingCart", shoppingCart);
        }
        for (String productId : shoppingCart.getItems().keySet()) {
            if (request.getParameter(productId) != null) {
                shoppingCart.setAmount(productId, Integer.parseInt(request.getParameter(productId)));
            }
        }
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}

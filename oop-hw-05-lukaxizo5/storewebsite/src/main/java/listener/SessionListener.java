package listener;

import model.ShoppingCart;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ShoppingCart shoppingCart = new ShoppingCart();
        se.getSession().setAttribute("shoppingCart", shoppingCart);
    }
}

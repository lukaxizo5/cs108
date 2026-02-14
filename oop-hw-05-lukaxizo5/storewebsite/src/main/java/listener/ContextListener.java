package listener;

import dao.ProductDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDAO productDAO = new ProductDAO();
        sce.getServletContext().setAttribute("productDAO", productDAO);
    }
}

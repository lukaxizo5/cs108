import dao.ProductDAO;
import model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ProductDAOTest {
    private static ProductDAO productDAO;

    @BeforeAll
    public static void setup() {
        productDAO = new ProductDAO();
    }

    @Test
    public void allProductsTest() {
        List<Product> products = productDAO.getAllProducts();
        assertNotNull(products);
        assertEquals(14, products.size());
    }

    @Test
    public void getProductByIdTest() {
        Product product1 = productDAO.getProductById("ALn");
        assertEquals(new Product("ALn", "Lanyard", "Lanyard.jpg", 5.95), product1);
        Product product2 = productDAO.getProductById("EG");
        assertNull(product2);
    }
}

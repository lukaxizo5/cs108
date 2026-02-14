import model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductTest {
    @Test
    public void productTest() {
        Product p1 = new Product("HC", "Classic Hoodie", "Hoodie.jpg", 40.00);
        Product p2 = new Product("AKy", "Keychain", "Keychain.jpg", 2.95);
        assertEquals("HC", p1.getProductId());
        assertEquals("Classic Hoodie", p1.getName());
        assertEquals("Hoodie.jpg", p1.getImageFile());
        assertEquals(40.00, p1.getPrice());
        assertEquals("AKy", p2.getProductId());
        assertEquals("Keychain", p2.getName());
        assertEquals("Keychain.jpg", p2.getImageFile());
        assertEquals(2.95, p2.getPrice());
        assertEquals(new Product("HC", "Classic Hoodie", "Hoodie.jpg", 40.00), p1);
        assertEquals(new Product("AKy", "Keychain", "Keychain.jpg", 2.95), p2);
        assertNotEquals(p1, p2);
    }
}

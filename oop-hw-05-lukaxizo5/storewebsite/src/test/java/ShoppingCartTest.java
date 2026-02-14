import model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {
    private ShoppingCart cart;
    private final String id1 = "HC";
    private final String id2 = "AKy";

    @BeforeEach
    public void setup() {
        cart = new ShoppingCart();
    }

    @Test
    public void addProductTest() {
        cart.addProduct(id1);
        assertEquals(1, cart.getAmount(id1));
        cart.addProduct(id1);
        assertEquals(2, cart.getAmount(id1));
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void setAmountTest() {
        cart.addProduct(id1);
        cart.setAmount(id1, 5);
        assertEquals(5, cart.getAmount(id1));
        assertEquals(1, cart.getItems().size());
        cart.addProduct(id1);
        assertEquals(6, cart.getAmount(id1));
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void setAmountToZeroTest() {
        cart.addProduct(id1);
        cart.addProduct(id2);
        cart.setAmount(id1, 2);
        cart.setAmount(id2, 3);
        assertEquals(2, cart.getAmount(id1));
        assertEquals(3, cart.getAmount(id2));
        assertEquals(2, cart.getItems().size());
        cart.setAmount(id1, 0);
        assertEquals(1, cart.getItems().size());
        assertEquals(0, cart.getAmount(id1));
    }

    @Test
    public void getItemsTest() {
        cart.addProduct(id1);
        cart.addProduct(id2);
        Map<String, Integer> items = cart.getItems();
        assertTrue(items.containsKey(id1));
        assertTrue(items.containsKey(id2));
        assertEquals(1, items.get(id1));
        assertEquals(1, items.get(id2));
        cart.addProduct(id1);
        cart.addProduct(id2);
        assertEquals(2, items.get(id1));
        assertEquals(2, items.get(id2));
    }
}

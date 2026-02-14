package model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    Map<String, Integer> items;

    public ShoppingCart() {
        items = new HashMap<>();
    }

    public void addProduct(String productId) {
        items.put(productId, items.getOrDefault(productId, 0) + 1);
    }

    public void setAmount(String productId, int amount) {
        if (amount <= 0) {
            items.remove(productId);
            return;
        }
        items.put(productId, amount);
    }

    public int getAmount(String productId) {
        return items.getOrDefault(productId, 0);
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}

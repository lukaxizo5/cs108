package model;

import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private String imageFile;
    private double price;

    public Product(String productId, String name, String imageFile, double price) {
        this.productId = productId;
        this.name = name;
        this.imageFile = imageFile;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && Objects.equals(productId, product.productId) && Objects.equals(name, product.name) && Objects.equals(imageFile, product.imageFile);
    }
}

package model;

import java.time.LocalDateTime;

public class Order {
    private final int id;
    private final String productId;
    private final int quantity;
    private final double productPrice;
    private final LocalDateTime orderDate;


    public Order(String productId, int quantity, double productPrice, LocalDateTime orderDate) {
        this(-1, productId, quantity, productPrice, orderDate);
    }

    // Class Constructor
    public Order(int id, String productId, int quantity, double productPrice, LocalDateTime orderDate) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.orderDate = orderDate;
    }

    // Class methods
    public int getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getTotalPrice() {
        return quantity * productPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getDisplayName() {
        return "Order " + id + " created for product: " + productId + " (" + quantity + " units)";
    }
}

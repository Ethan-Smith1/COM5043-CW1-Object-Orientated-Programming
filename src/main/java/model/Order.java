package model;

import java.time.LocalDateTime;

public class Order {
    private final int id;
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double productPrice;
    private final LocalDateTime orderDate;

    // Class Constructor
    public Order(int id, String productId, String productName, int quantity, double productPrice, LocalDateTime orderDate) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
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

    public String getProductName() {
        return productName;
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
        return "Order " + id + " created for the product " + productName + " [" + productId + "] (" + quantity + " units)";
    }
}

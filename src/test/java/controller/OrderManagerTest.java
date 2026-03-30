package controller;

import model.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderManagerTest {

    @Test
    public void testOrderCreationWithValidData() {
        Order order = new Order(1, "PD001", "JCB Forklift", 1, 4000.0, LocalDateTime.now());

        assertNotNull(order, "Order should be created successfully");
        assertEquals(1, order.getId(), "Order ID should match");
        assertEquals("PD001", order.getProductId(), "Product ID should match");
        assertEquals(1, order.getQuantity(), "Quantity should be 1");
        assertEquals(4000.0, order.getTotalPrice(), 0.01, "Total price should be 4000.0");
    }

    @Test
    public void testCalculateTotalPrice() {
        Order order = new Order(2, "PD002", "Power Drill", 20, 15.0, LocalDateTime.now());

        assertEquals(300.0, order.getTotalPrice(), 0.01, "Total price calculation should be 300.0");
    }

    @Test
    public void testOrderDisplayName() {
        Order order = new Order(3, "PD003", "JCB Forklift", 5, 100.0, LocalDateTime.now());

        assertTrue(order.getDisplayName().contains("PD003"), "Display name should contain product ID");
        assertTrue(order.getDisplayName().contains("JCB Forklift"), "Display name should contain product name");
    }
}

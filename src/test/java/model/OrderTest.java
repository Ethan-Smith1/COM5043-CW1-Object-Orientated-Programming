package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testOrderTotalPriceCalculation() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 3, 22, 10, 30);
        Order order = new Order(1, "PD001", "Bricks", 50, 5.0, orderDate);
        assertEquals(250.0, order.getTotalPrice(), "50 units at 5.00 should equal 250.00");
    }

    @Test
    public void testOrderTotalPriceWithDouble() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 3, 22, 10, 30);
        Order order = new Order(2, "PD002", "Power Drill", 10, 49.99, orderDate);
        assertEquals(499.9, order.getTotalPrice(), 0.01, "10 units at 49.99 should equal 499.90");
    }

    @Test
    public void testOrderDisplayName() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 3, 22, 10, 30);
        Order order = new Order(5, "PD001", "Bricks", 25, 5.0, orderDate);
        String displayName = order.getDisplayName();
        
        assertTrue(displayName.contains("Order 5"), "Display name should contain order ID");
        assertTrue(displayName.contains("Bricks"), "Display name should contain product name");
        assertTrue(displayName.contains("25 units"), "Display name should contain quantity");
    }
}

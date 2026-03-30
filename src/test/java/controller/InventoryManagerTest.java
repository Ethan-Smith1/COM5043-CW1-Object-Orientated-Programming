package controller;

import model.Bricks;
import model.Drill;
import model.WarehouseItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagerTest {

    private InventoryManager inventoryManager;

    @BeforeEach
    public void setUp() {
        inventoryManager = new InventoryManager();
    }

    @Test
    public void testCreateBricksProduct() {
        Bricks bricks = new Bricks("PD001", "Bricks", 5.0, 100, 10, "SP001", "Standard");

        assertEquals("PD001", bricks.getId());
        assertEquals("Bricks", bricks.getName());
        assertEquals("Bricks", bricks.getProductType());
    }

    @Test
    public void testProductLowStockDetection() {
        WarehouseItem item = new Bricks("PD001", "Bricks", 5.0, 8, 10, "SP001", "Standard");

        assertTrue(item.isLowStock(), "Product with 8 units and threshold 10 should be low stock");
    }

    @Test
    public void testProductNotInLowStock() {
        WarehouseItem item = new Drill("PD002", "Power Drill", 50.0, 25, 5, "SP001", 10);

        assertFalse(item.isLowStock(), "Product with 25 units and threshold 5 should not be low stock");
    }
}

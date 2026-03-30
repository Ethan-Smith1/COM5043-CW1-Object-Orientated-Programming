package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseItemTest {

    @Test
    public void testIsLowStockBelowThreshold() {
        WarehouseItem item = new Bricks("PD001", "Bricks", 5.0, 8, 10, "SP001", "Standard");
        assertTrue(item.isLowStock(), "Stock of 8 units should be low when threshold is 10");
    }

    @Test
    public void testIsLowStockAtThreshold() {
        WarehouseItem item = new Drill("PD002", "Power Drill", 50.0, 10, 10, "SP001", 10);
        assertTrue(item.isLowStock(), "Stock at threshold should be considered low");
    }

    @Test
    public void testIsNotLowStockAboveThreshold() {
        WarehouseItem item = new Drill("PD002", "Power Drill", 50.0, 25, 10, "SP001", 10);
        assertFalse(item.isLowStock(), "Stock of 25 units should not be low when threshold is 10");
    }


    @Test
    public void testGetProductTypeForMultipleTypes() {
        WarehouseItem bricks = new Bricks("PD001", "Bricks", 5.0, 100, 10, "SP001", "Standard");
        WarehouseItem drill = new Drill("PD002", "Power Drill", 50.0, 25, 5, "SP001", 10);
        WarehouseItem forklift = new Forklift("PD003", "JCB Forklift", 5000.0, 2, 1, "SP002", 2000);
        
        assertEquals("Bricks", bricks.getProductType());
        assertEquals("Drill", drill.getProductType());
        assertEquals("Forklift", forklift.getProductType());
    }
}

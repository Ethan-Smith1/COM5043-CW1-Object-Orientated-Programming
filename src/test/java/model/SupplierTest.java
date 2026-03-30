package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {

    @Test
    public void testSupplierInitiallyActive() {
        Supplier supplier = new Supplier("SP001", "Test Supplier", "contact@supplier.com", true);

        assertEquals("SP001", supplier.getId());
        assertEquals("Test Supplier", supplier.getName());
        assertTrue(supplier.isActive(), "Supplier should be initialized as active");
    }

    @Test
    public void testSupplierDeactivation() {
        Supplier supplier = new Supplier("SP001", "Test Supplier", "contact@supplier.com", true);
        supplier.setActive(false);
        
        assertFalse(supplier.isActive(), "Supplier should be deactivated after calling setActive(false)");
    }
}


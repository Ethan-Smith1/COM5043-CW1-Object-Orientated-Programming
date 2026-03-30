package controller;

import model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierManagerTest {

    private SupplierManager supplierManager;

    @BeforeEach
    public void setUp() {
        supplierManager = new SupplierManager();
    }

    @Test
    public void testCreateSupplier() {
        Supplier supplier = new Supplier("SP001", "Supplier Test", "contact@test.com", true);

        assertEquals("SP001", supplier.getId());
        assertEquals("Supplier Test", supplier.getName());
        assertTrue(supplier.isActive(), "Supplier should be created as active");
    }

    @Test
    public void testSupplierDeactivation() {
        Supplier supplier = new Supplier("SP001", "Supplier Test", "contact@test.com", true);
        supplier.setActive(false);

        assertFalse(supplier.isActive(), "Supplier should be deactivated");
    }
}

package controller;

import model.Supplier;
import java.sql.*;
import java.util.List;

public class SupplierManager extends Manager {

    private static final String SELECT_ACTIVE_SUPPLIERS_SQL =
            "SELECT supplier_id, name, contact, is_active FROM suppliers WHERE is_active = TRUE ORDER BY name";

    private static final String SELECT_SUPPLIER_BY_ID_SQL =
            "SELECT supplier_id, name, contact, is_active FROM suppliers WHERE supplier_id = ? AND is_active = TRUE";

    private static final String INSERT_SUPPLIER_SQL =
            "INSERT INTO suppliers (supplier_id, name, contact, is_active) VALUES (?, ?, ?, TRUE)";

    private static final String UPDATE_SUPPLIER_SQL =
            "UPDATE suppliers SET name = ?, contact = ? WHERE supplier_id = ? AND is_active = TRUE";

    private static final String DELETE_SUPPLIER_SQL =
            "UPDATE suppliers SET is_active = FALSE WHERE supplier_id = ? AND is_active = TRUE";

    public List<Supplier> getAllSuppliers() {
        return fetchEntities(SELECT_ACTIVE_SUPPLIERS_SQL, this::mapSupplier);
    }

    public boolean addSupplier(Supplier supplier) {
        validateNotNull(supplier, "Supplier details are required.");
        String supplierId = normalizeSupplierId(supplier.getId());

        return executeUpdate(
                INSERT_SUPPLIER_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, supplierId);
                    preparedStatement.setString(2, supplier.getName());
                    preparedStatement.setString(3, supplier.getContactInfo());
                },
                "A supplier with ID '" + supplierId + "' already exists."
        );
    }

    public boolean updateSupplier(String supplierId, String supplierName, String contactInfo) {
        validateNotBlank(supplierId, "Supplier ID");
        validateNotBlank(supplierName, "Supplier name");

        return executeUpdate(
                UPDATE_SUPPLIER_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, supplierName.trim());
                    preparedStatement.setString(2, contactInfo == null ? "" : contactInfo.trim());
                    preparedStatement.setString(3, supplierId.trim());
                }
        );
    }

    public boolean deleteSupplier(String supplierId) {
        validateNotBlank(supplierId, "Supplier ID");

        return executeUpdate(
                DELETE_SUPPLIER_SQL,
                preparedStatement -> preparedStatement.setString(1, supplierId.trim())
        );
    }

    public Supplier getSupplierById(String supplierId) {
        if (supplierId == null || supplierId.isBlank()) {
            return null;
        }

        return fetchSingleEntity(
                SELECT_SUPPLIER_BY_ID_SQL,
                preparedStatement -> preparedStatement.setString(1, supplierId.trim()),
                this::mapSupplier
        );
    }

    private Supplier mapSupplier(ResultSet result) throws SQLException {
        return new Supplier(
                result.getString("supplier_id"),
                result.getString("name"),
                result.getString("contact"),
                result.getBoolean("is_active")
        );
    }

    private String normalizeSupplierId(String supplierId) {
        validateNotBlank(supplierId, "Supplier ID");
        String userInput = supplierId.trim();
        return "SP" + userInput.replaceFirst("(?i)^SP", "");
    }
}

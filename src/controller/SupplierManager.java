package controller;

import model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierManager {

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
        return fetchSuppliers(SELECT_ACTIVE_SUPPLIERS_SQL);
    }

    public boolean addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier details are required.");
        }

        String supplierId = normalizeSupplierId(supplier.getId());

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUPPLIER_SQL)) {

            preparedStatement.setString(1, supplierId);
            preparedStatement.setString(2, supplier.getName());
            preparedStatement.setString(3, supplier.getContactInfo());
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            if ("23505".equals(sqlException.getSQLState())) {
                throw new IllegalArgumentException("A supplier with ID '" + supplierId + "' already exists.");
            }
            System.err.println("Error adding supplier: " + sqlException.getMessage());
            return false;
        }
    }

    public boolean updateSupplier(String supplierId, String supplierName, String contactInfo) {
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier ID is required.");
        }
        if (supplierName == null || supplierName.isBlank()) {
            throw new IllegalArgumentException("Supplier name is required.");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUPPLIER_SQL)) {

            preparedStatement.setString(1, supplierName.trim());
            preparedStatement.setString(2, contactInfo == null ? "" : contactInfo.trim());
            preparedStatement.setString(3, supplierId.trim());
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            System.err.println("Error updating supplier: " + sqlException.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(String supplierId) {
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier ID is required.");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUPPLIER_SQL)) {

            preparedStatement.setString(1, supplierId.trim());
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            System.err.println("Error deleting supplier: " + sqlException.getMessage());
            return false;
        }
    }

    public Supplier getSupplierById(String supplierId) {
        if (supplierId == null || supplierId.isBlank()) {
            return null;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUPPLIER_BY_ID_SQL)) {

            preparedStatement.setString(1, supplierId.trim());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapSupplier(resultSet);
                }
            }
        } catch (SQLException sqlException) {
            System.err.println("Error fetching supplier: " + sqlException.getMessage());
        }

        return null;
    }

    private List<Supplier> fetchSuppliers(String query) {
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                suppliers.add(mapSupplier(resultSet));
            }
        } catch (SQLException sqlException) {
            System.err.println("Error fetching suppliers: " + sqlException.getMessage());
        }

        return suppliers;
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
        if (supplierId == null || supplierId.isBlank()) {
            throw new IllegalArgumentException("Supplier ID is required.");
        }

        String userInput = supplierId.trim();
        return "SP" + userInput.replaceFirst("(?i)^SP", "");
    }
}

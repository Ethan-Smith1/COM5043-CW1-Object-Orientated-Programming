package controller;

import model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierManager {

    // Gets all active suppliers from the database
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT supplier_id, name, contact, is_active FROM suppliers ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                suppliers.add(mapSupplier(result));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching suppliers: " + e.getMessage());
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
}


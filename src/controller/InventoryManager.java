package controller;

import model.Bricks;
import model.Drill;
import model.Forklift;
import model.WarehouseItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager extends Manager {

    private static final String PRODUCT_COLUMNS =
            "product_id, name, price, quantity, threshold, supplier_id, product_type, " +
            "drill_size_mm, lifting_capacity_kg, brick_type, is_active";

    private static final String SELECT_ALL_PRODUCTS_SQL =
            "SELECT " + PRODUCT_COLUMNS + " FROM products WHERE is_active = TRUE ORDER BY product_id";

    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO products (product_id, name, price, quantity, threshold, supplier_id, product_type, " +
                    "drill_size_mm, lifting_capacity_kg, brick_type, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";

    private static final String SEARCH_PRODUCTS_SQL =
            "SELECT " + PRODUCT_COLUMNS + " FROM products " +
                    "WHERE (LOWER(product_id) LIKE ? OR LOWER(name) LIKE ?) AND is_active = TRUE " +
                    "ORDER BY product_id";

    private static final String UPDATE_STOCK_RELATIVE_SQL =
            "UPDATE products SET quantity = quantity + ? WHERE product_id = ? AND is_active = TRUE";

    private static final String UPDATE_STOCK_ABSOLUTE_SQL =
            "UPDATE products SET quantity = ? WHERE product_id = ? AND is_active = TRUE";

    private static final String DELETE_PRODUCT_SQL =
            "UPDATE products SET is_active = FALSE WHERE product_id = ? AND is_active = TRUE";

    public List<WarehouseItem> getAllProducts() {
        return fetchEntities(SELECT_ALL_PRODUCTS_SQL, this::mapWarehouseItem);
    }

    public boolean addProduct(WarehouseItem product) {
        validateNotNull(product, "Product details are required.");
        String normalizedProductId = normalizeProductId(product.getId());

        return executeUpdate(
                INSERT_PRODUCT_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, normalizedProductId);
                    preparedStatement.setString(2, product.getName());
                    preparedStatement.setDouble(3, product.getPrice());
                    preparedStatement.setInt(4, product.getQuantity());
                    preparedStatement.setInt(5, product.getStockThreshold());
                    preparedStatement.setString(6, product.getSupplierId());
                    preparedStatement.setString(7, product.getProductType());
                    try {
                        bindTypeSpecificColumns(preparedStatement, product);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                "A product with ID '" + normalizedProductId + "' already exists."
        );
    }

    public List<WarehouseItem> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        String normalizedSearch = "%" + searchTerm.trim().toLowerCase() + "%";

        return fetchEntities(
                SEARCH_PRODUCTS_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, normalizedSearch);
                    preparedStatement.setString(2, normalizedSearch);
                },
                this::mapWarehouseItem
        );
    }

    public List<WarehouseItem> getLowStockProducts() {
        List<WarehouseItem> lowStockProducts = new ArrayList<>();
        for (WarehouseItem product : getAllProducts()) {
            if (product.isLowStock()) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    public boolean updateProductStock(String productId, int quantityChange) {
        validateNotBlank(productId, "Select a product before updating stock");

        return executeUpdate(
                UPDATE_STOCK_RELATIVE_SQL,
                preparedStatement -> {
                    preparedStatement.setInt(1, quantityChange);
                    preparedStatement.setString(2, productId.trim());
                }
        );
    }

    public boolean setProductStock(String productId, int newQuantity) {
        validateNotBlank(productId, "Select a product before updating stock");
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity can't be negative.");
        }

        return executeUpdate(
                UPDATE_STOCK_ABSOLUTE_SQL,
                preparedStatement -> {
                    preparedStatement.setInt(1, newQuantity);
                    preparedStatement.setString(2, productId.trim());
                }
        );
    }

    private WarehouseItem mapWarehouseItem(ResultSet resultSet) throws SQLException {
        String productType = resultSet.getString("product_type");
        String productId = resultSet.getString("product_id");
        String productName = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        int quantity = resultSet.getInt("quantity");
        int threshold = resultSet.getInt("threshold");
        String supplierId = resultSet.getString("supplier_id");

        Integer drillSize = resultSet.getObject("drill_size_mm", Integer.class);
        Integer liftingCapacity = resultSet.getObject("lifting_capacity_kg", Integer.class);
        String brickType = resultSet.getString("brick_type");

        return switch (productType) {
            case "Drill" -> new Drill(productId, productName, price, quantity, threshold, supplierId,
                    drillSize != null ? drillSize : Drill.DEFAULT_DRILL_SIZE);
            case "Forklift" -> new Forklift(productId, productName, price, quantity, threshold, supplierId,
                    liftingCapacity != null ? liftingCapacity : Forklift.DEFAULT_LIFTING_CAPACITY_KG);
            case "Bricks" -> new Bricks(productId, productName, price, quantity, threshold, supplierId,
                    brickType != null ? brickType : Bricks.DEFAULT_BRICK_TYPE);
            default -> throw new IllegalStateException("Unknown product type: " + productType);
        };
    }

    public boolean deleteProduct(String productId) {
        validateNotBlank(productId, "Product ID");

        return executeUpdate(
                DELETE_PRODUCT_SQL,
                preparedStatement -> preparedStatement.setString(1, productId.trim())
        );
    }

    private String normalizeProductId(String productId) {
        validateNotBlank(productId, "Product ID");
        String userInput = productId.trim();
        return "PD" + userInput.replaceFirst("(?i)^PD", "");
    }

    private void bindTypeSpecificColumns(PreparedStatement preparedStatement, WarehouseItem product) throws SQLException {
        if (product instanceof Drill drill) {
            preparedStatement.setInt(8, drill.getDrillSize());
            preparedStatement.setNull(9, Types.INTEGER);
            preparedStatement.setNull(10, Types.VARCHAR);
            return;
        }

        if (product instanceof Forklift forklift) {
            preparedStatement.setNull(8, Types.INTEGER);
            preparedStatement.setInt(9, forklift.getLiftingCapacityKg());
            preparedStatement.setNull(10, Types.VARCHAR);
            return;
        }

        if (product instanceof Bricks bricks) {
            preparedStatement.setNull(8, Types.INTEGER);
            preparedStatement.setNull(9, Types.INTEGER);
            preparedStatement.setString(10, bricks.getBrickType());
            return;
        }

        preparedStatement.setNull(8, Types.INTEGER);
        preparedStatement.setNull(9, Types.INTEGER);
        preparedStatement.setNull(10, Types.VARCHAR);
    }
}

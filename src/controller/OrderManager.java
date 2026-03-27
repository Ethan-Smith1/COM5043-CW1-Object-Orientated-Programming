package controller;

import model.Order;
import java.sql.*;
import java.util.List;

public class OrderManager extends Manager {

    private static final String SELECT_ALL_ORDERS_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date, (SELECT name FROM products WHERE product_id = orders.product_id) as product_name " +
            "FROM orders ORDER BY order_date DESC";

    private static final String INSERT_ORDER_SQL =
            "INSERT INTO orders (product_id, quantity, product_price, order_date) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SEARCH_ORDERS_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date, (SELECT name FROM products WHERE product_id = orders.product_id) as product_name " +
            "FROM orders WHERE LOWER(CAST(order_id AS TEXT)) LIKE ? OR LOWER(product_id) LIKE ? " +
            "ORDER BY order_date DESC";

    private static final String GET_ORDER_BY_ID_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date, (SELECT name FROM products WHERE product_id = orders.product_id) as product_name " +
            "FROM orders WHERE order_id = ?";

    private static final String DELETE_ORDER_SQL =
            "DELETE FROM orders WHERE order_id = ?";

    private final InventoryManager inventoryManager;

    public OrderManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public List<Order> getAllOrders() {
        return fetchEntities(SELECT_ALL_ORDERS_SQL, this::mapOrder);
    }

    public boolean createOrder(Order order) {
        validateNotNull(order, "Order details are required.");
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than 0.");
        }

        boolean inventoryUpdated = inventoryManager.updateProductStock(
                order.getProductId(),
                -order.getQuantity()
        );

        if (!inventoryUpdated) {
            throw new IllegalArgumentException("Could not update inventory for product '" + order.getProductId() + "'. " +
                    "Check the product ID is correct and has enough stock.");
        }

        return executeUpdate(
                INSERT_ORDER_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, order.getProductId());
                    preparedStatement.setInt(2, order.getQuantity());
                    preparedStatement.setDouble(3, order.getProductPrice());
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
                }
        );
    }

    public boolean cancelOrder(int orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID '" + orderId + "' was not found.");
        }

        boolean inventoryRestored = inventoryManager.updateProductStock(
                order.getProductId(),
                order.getQuantity()
        );

        if (!inventoryRestored) {
            throw new IllegalArgumentException("Could not restore the inventory for product '" + order.getProductId() + "'.");
        }

        return executeUpdate(
                DELETE_ORDER_SQL,
                preparedStatement -> preparedStatement.setInt(1, orderId)
        );
    }

    public Order getOrderById(int orderId) {
        return fetchSingleEntity(
                GET_ORDER_BY_ID_SQL,
                preparedStatement -> preparedStatement.setInt(1, orderId),
                this::mapOrder
        );
    }

    public List<Order> searchOrders(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        String normalizedSearch = "%" + searchTerm.trim().toLowerCase() + "%";

        return fetchEntities(
                SEARCH_ORDERS_SQL,
                preparedStatement -> {
                    preparedStatement.setString(1, normalizedSearch);
                    preparedStatement.setString(2, normalizedSearch);
                },
                this::mapOrder
        );
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getInt("order_id"),
                resultSet.getString("product_id"),
                resultSet.getString("product_name"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("product_price"),
                resultSet.getTimestamp("order_date").toLocalDateTime()
        );
    }
}

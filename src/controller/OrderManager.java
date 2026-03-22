package controller;

import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final String SELECT_ALL_ORDERS_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date " +
            "FROM orders ORDER BY order_date DESC";

    private static final String INSERT_ORDER_SQL =
            "INSERT INTO orders (product_id, quantity, product_price, order_date) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SEARCH_ORDERS_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date " +
            "FROM orders WHERE LOWER(CAST(order_id AS TEXT)) LIKE ? OR LOWER(product_id) LIKE ? " +
            "ORDER BY order_date DESC";

    private static final String GET_ORDER_BY_ID_SQL =
            "SELECT order_id, product_id, quantity, product_price, order_date " +
            "FROM orders WHERE order_id = ?";

    private static final String DELETE_ORDER_SQL =
            "DELETE FROM orders WHERE order_id = ?";

    private final InventoryManager inventoryManager;

    public OrderManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public List<Order> getAllOrders() {
        return fetchOrders(SELECT_ALL_ORDERS_SQL);
    }

    public boolean createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order details are required.");
        }
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than 0.");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_SQL)) {

            preparedStatement.setString(1, order.getProductId());
            preparedStatement.setInt(2, order.getQuantity());
            preparedStatement.setDouble(3, order.getProductPrice());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));

            boolean inventoryUpdated = inventoryManager.updateProductStock(
                    order.getProductId(),
                    -order.getQuantity()
            );

            if (!inventoryUpdated) {
                throw new IllegalArgumentException("Could not update inventory for product '" + order.getProductId() + "'. " +
                        "Check the product ID is correct and has enough stock.");
            }

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            if ("23505".equals(sqlException.getSQLState())) {
                throw new IllegalArgumentException("An order with ID '" + order.getId() + "' already exists.");
            }
            System.err.println("Error creating order: " + sqlException.getMessage());
            return false;
        }
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

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER_SQL)) {

            preparedStatement.setInt(1, orderId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            System.err.println("Error deleting order: " + sqlException.getMessage());
            return false;
        }
    }

    public Order getOrderById(int orderId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID_SQL)) {

            preparedStatement.setInt(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }
        } catch (SQLException sqlException) {
            System.err.println("Error fetching order: " + sqlException.getMessage());
        }

        return null;
    }

    public List<Order> searchOrders(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        String normalizedSearch = "%" + searchTerm.trim().toLowerCase() + "%";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ORDERS_SQL)) {

            preparedStatement.setString(1, normalizedSearch);
            preparedStatement.setString(2, normalizedSearch);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        } catch (SQLException sqlException) {
            System.err.println("Error searching orders: " + sqlException.getMessage());
        }

        return orders;
    }

    private List<Order> fetchOrders(String query) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        } catch (SQLException sqlException) {
            System.err.println("Error fetching orders: " + sqlException.getMessage());
        }

        return orders;
    }

    private Order mapOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getInt("order_id"),
                resultSet.getString("product_id"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("product_price"),
                resultSet.getTimestamp("order_date").toLocalDateTime()
        );
    }
}

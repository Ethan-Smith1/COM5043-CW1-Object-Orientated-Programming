package view;

import controller.InventoryManager;
import controller.OrderManager;
import model.Order;
import model.WarehouseItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class OrderView extends CLReaderView {

    private static final String ORDER_TABLE_FORMAT = "%-12s %-12s %-12s %-12s %-20s%n";

    private final OrderManager orderManager;
    private final InventoryManager inventoryManager;

    public OrderView(Scanner scanner, OrderManager orderManager, InventoryManager inventoryManager) {
        super(scanner);
        this.orderManager = orderManager;
        this.inventoryManager = inventoryManager;
    }

    public void run() {
        boolean orderMenu = true;
        while (orderMenu) {
            printOrderMenu();
            String choice = readString("Choose an order option");

            switch (choice) {
                case "1" -> showAllOrders();
                case "2" -> searchOrders();
                case "3" -> createOrder();
                case "4" -> cancelOrder();
                case "0" -> orderMenu = false;
                default -> System.out.println("Invalid option. Please choose 0-4.");
            }
        }
    }

    private void printOrderMenu() {
        System.out.println();
        System.out.println("Order Management");
        System.out.println("1) List all orders");
        System.out.println("2) Search orders by ID or product");
        System.out.println("3) Create new order");
        System.out.println("4) Cancel order");
        System.out.println("0) Back to main menu");
    }

    private void showAllOrders() {
        printOrders(orderManager.getAllOrders());
    }

    private void searchOrders() {
        System.out.print("Search term (Order ID or Product ID): ");
        String searchTerm = scanner.nextLine();
        if (searchTerm == null || searchTerm.isBlank()) {
            System.out.println("Please enter a search term");
            return;
        }
        printOrders(orderManager.searchOrders(searchTerm));
    }

    private void createOrder() {
        try {
            String productId = readString("Product ID");

            // Verify product exists
            WarehouseItem product = verifyProductExists(productId);
            if (product == null) {
                return;
            }

            int quantity = readInt("Order quantity");
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
                return;
            }

            if (product.getQuantity() < quantity) {
                System.out.println("Insufficient stock. Available: " + product.getQuantity() +
                        ", Required: " + quantity);
                return;
            }

            Order order = new Order(
                    0,
                    productId,
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    LocalDateTime.now()
            );

            if (orderManager.createOrder(order)) {
                System.out.println("Order created: " + order.getDisplayName());
                System.out.printf("Total price: £%.2f%n", order.getTotalPrice());
            } else {
                System.out.println("Order could not be created.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Input error: " + ex.getMessage());
        }
    }

    private void cancelOrder() {
        try {
            int orderId = readInt("Order ID to cancel");

            Order order = orderManager.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order with ID '" + orderId + "' not found.");
                return;
            }

            if (orderManager.cancelOrder(orderId)) {
                System.out.println("Order " + orderId + " cancelled successfully.");
                System.out.println("Inventory restored. " + order.getQuantity() +
                        " units of product " + order.getProductId() + " added back to stock.");
            } else {
                System.out.println("Order could not be cancelled.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Input error: " + ex.getMessage());
        }
    }

    private WarehouseItem verifyProductExists(String productId) {
        List<WarehouseItem> products = inventoryManager.searchProducts(productId);
        if (products.isEmpty()) {
            System.out.println("Product with ID '" + productId + "' not found in inventory.");
            return null;
        }
        return products.get(0);
    }

    private void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.printf(ORDER_TABLE_FORMAT, "Order ID", "Product ID", "Qty", "Unit Price",
                "Total Price");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Order order : orders) {
            System.out.printf(ORDER_TABLE_FORMAT,
                    order.getId(),
                    order.getProductId(),
                    order.getQuantity(),
                    String.format("£%.2f", order.getProductPrice()),
                    String.format("£%.2f", order.getTotalPrice()));
        }
    }
}


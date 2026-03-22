package view;

import controller.InventoryManager;
import controller.SupplierManager;
import model.Bricks;
import model.Drill;
import model.Forklift;
import model.Supplier;
import model.WarehouseItem;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InventoryView extends CLReaderView {

    private static final String PRODUCT_TABLE_FORMAT = "%-12s %-24s %-12s %-10s %-8s %-12s %-14s %-30s%n";

    private final InventoryManager inventoryManager;
    private final SupplierManager supplierManager;

    public InventoryView(InventoryManager inventoryManager, SupplierManager supplierManager, Scanner scanner) {
        super(scanner);
        this.inventoryManager = inventoryManager;
        this.supplierManager = supplierManager;
    }

    public void run() {
        boolean productMenu = true;
        while (productMenu) {
            printProductMenu();
            String choice = readString("Choose a product option");

            switch (choice) {
                case "1" -> showAllProducts();
                case "2" -> searchProducts();
                case "3" -> addProduct();
                case "4" -> updateStock();
                case "5" -> showLowStockReport();
                case "0" -> productMenu = false;
                default -> System.out.println("Invalid option. Please choose 0-5.");
            }
        }
    }

    private void printProductMenu() {
        System.out.println();
        System.out.println("Product Management");
        System.out.println("1) List all products");
        System.out.println("2) Search products by ID or name");
        System.out.println("3) Add product");
        System.out.println("4) Update stock quantity");
        System.out.println("5) Low stock report");
        System.out.println("0) Back to main menu");
    }

    private void showAllProducts() {
        printProducts(inventoryManager.getAllProducts());
    }

    private void searchProducts() {
        System.out.print("Search term (ID or name): ");
        String searchTerm = scanner.nextLine();
        if (searchTerm == null || searchTerm.isBlank()) {
            System.out.println("Please enter a product ID or name");
            return;
        }
        printProducts(inventoryManager.searchProducts(searchTerm));
    }

    private void addProduct() {
        try {
            String productId = readString("Product ID");
            String productName = readString("Product name");
            double price = readPrice();
            int quantity = readInt("Quantity");
            int threshold = readInt("Low stock threshold");

            Supplier supplier = selectSupplier();
            if (supplier == null) {
                System.out.println("No suppliers found. Add a supplier first.");
                return;
            }

            String type = selectProductType();
            WarehouseItem newProduct = buildProductByType(
                    type,
                    productId,
                    productName,
                    price,
                    quantity,
                    threshold,
                    supplier.getId()
            );

            boolean added = inventoryManager.addProduct(newProduct);
            if (added) {
                System.out.println("Product added: " + newProduct.getName() + " (" + newProduct.getId() + ")");
            } else {
                System.out.println("Product was not added.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Input error: " + ex.getMessage());
        }
    }

    private void updateStock() {
        try {
            String productId = readString("Product ID to update");
            int quantity = readInt("New quantity");

            boolean updated = inventoryManager.setProductStock(productId, quantity);
            if (updated) {
                System.out.println("Stock updated for product ID " + productId + ".");
            } else {
                System.out.println("Product could not be updated. Check the product id and try again");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Input error: " + ex.getMessage());
        }
    }

    private void showLowStockReport() {
        List<WarehouseItem> lowStockProducts = inventoryManager.getLowStockProducts();
        if (lowStockProducts.isEmpty()) {
            System.out.println("No products are currently low on stock.");
            return;
        }

        System.out.println("Low stock products:");
        printProducts(lowStockProducts);
    }

    private Supplier selectSupplier() {
        List<Supplier> suppliers = supplierManager.getAllSuppliers();
        if (suppliers.isEmpty()) {
            return null;
        }

        System.out.println("Available suppliers:");
        for (Supplier supplier : suppliers) {
            System.out.println("- " + supplier.getDisplayName());
        }

        while (true) {
            String supplierId = readString("Supplier ID");
            for (Supplier supplier : suppliers) {
                if (supplier.getId().equalsIgnoreCase(supplierId.trim())) {
                    return supplier;
                }
            }
            System.out.println("Supplier ID not found. Please try again.");
        }
    }

    private String selectProductType() {
        while (true) {
            System.out.println("Product type options: Drill, Forklift, Bricks");
            String typeInput = readString("Type").toLowerCase(Locale.ROOT);

            switch (typeInput) {
                case "drill":
                    return "Drill";
                case "forklift":
                    return "Forklift";
                case "bricks":
                    return "Bricks";
                default:
                    System.out.println("Invalid type. Enter Drill, Forklift, or Bricks.");
            }
        }
    }

    private WarehouseItem buildProductByType(String type,
                                             String productId,
                                             String productName,
                                             double price,
                                             int quantity,
                                             int threshold,
                                             String supplierId) {
        return switch (type) {
            case "Drill" -> {
                int drillSize = readInt("Drill size (mm)");
                yield new Drill(productId, productName, price, quantity, threshold, supplierId, drillSize);
            }
            case "Forklift" -> {
                int liftingCapacity = readInt("Lifting capacity (kg)");
                yield new Forklift(productId, productName, price, quantity, threshold, supplierId, liftingCapacity);
            }
            case "Bricks" -> {
                String brickType = readString("Brick type");
                yield new Bricks(productId, productName, price, quantity, threshold, supplierId, brickType);
            }
            default -> throw new IllegalArgumentException("Unsupported product type: " + type);
        };
    }

    private void printProducts(List<WarehouseItem> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.printf(PRODUCT_TABLE_FORMAT,"ID", "Name", "Type", "Price", "Qty", "Threshold", "Supplier", "Details");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        for (WarehouseItem product : products) {
            System.out.printf(PRODUCT_TABLE_FORMAT,
                    product.getId(),
                    product.getName(),
                    product.getProductType(),
                    String.format("%.2f", product.getPrice()),
                    product.getQuantity(),
                    product.getStockThreshold(),
                    product.getSupplierId(),
                    buildProductDetails(product));
        }
    }

    private String buildProductDetails(WarehouseItem product) {
        if (product instanceof Drill drill) {
            return "drill size=" + drill.getDrillSize() + "mm";
        }
        if (product instanceof Forklift forklift) {
            return "lifting capacity=" + forklift.getLiftingCapacityKg() + "kg";
        }
        if (product instanceof Bricks bricks) {
            return "brick type=" + bricks.getBrickType();
        }
        return "";
    }
}


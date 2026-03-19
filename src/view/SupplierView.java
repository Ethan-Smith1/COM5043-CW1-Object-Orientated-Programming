package view;

import controller.SupplierManager;
import model.Supplier;

import java.util.List;
import java.util.Scanner;

public class SupplierView extends CLReaderView {

    private static final String SUPPLIER_TABLE_FORMAT = "%-12s %-24s %-30s%n";

    private final SupplierManager supplierManager;

    public SupplierView(Scanner scanner, SupplierManager supplierManager) {
        super(scanner);
        this.supplierManager = supplierManager;
    }

    public void run() {
        boolean supplierMenu = true;
        while (supplierMenu) {
            printSupplierMenu();
            String choice = readString("Choose a supplier option");

            switch (choice) {
                case "1" -> showSuppliers();
                case "2" -> addSupplier();
                case "3" -> updateSupplier();
                case "4" -> deleteSupplier();
                case "0" -> supplierMenu = false;
                default -> System.out.println("Invalid option. Please choose 0-4.");
            }
        }
    }

    private void printSupplierMenu() {
        System.out.println();
        System.out.println("Supplier Management");
        System.out.println("1) List active suppliers");
        System.out.println("2) Add supplier");
        System.out.println("3) Update supplier details");
        System.out.println("4) Delete supplier");
        System.out.println("0) Back to main menu");
    }

    private void showSuppliers() {
        List<Supplier> suppliers = supplierManager.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No active suppliers found.");
            return;
        }

        System.out.printf(SUPPLIER_TABLE_FORMAT, "ID", "Name", "Contact");
        System.out.println("-----------------------------------------------------------------------");
        for (Supplier supplier : suppliers) {
            System.out.printf(SUPPLIER_TABLE_FORMAT,
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactInfo() == null ? "" : supplier.getContactInfo());
        }
    }

    private void addSupplier() {
        try {
            String supplierId = readString("Supplier ID");
            String supplierName = readString("Supplier name");
            String contactInfo = readString("Contact info");

            Supplier supplier = new Supplier(supplierId, supplierName, contactInfo, true);
            if (supplierManager.addSupplier(supplier)) {
                System.out.println("Supplier added: " + supplier.getDisplayName());
            } else {
                System.out.println("Supplier could not be added.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Input error: " + exception.getMessage());
        }
    }

    private void updateSupplier() {
        try {
            String supplierId = readString("ID of supplier that you want to update");
            String supplierName = readString("New supplier name");
            String contactInfo = readString("New contact info");

            if (supplierManager.updateSupplier(supplierId, supplierName, contactInfo)) {
                System.out.println("Supplier updated successfully.");
            } else {
                System.out.println("Supplier could not be updated. Check the supplier ID is valid.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Input error: " + exception.getMessage());
        }
    }

    private void deleteSupplier() {
        try {
            String supplierId = readString("ID of supplier that you want to delete");
            if (supplierManager.deleteSupplier(supplierId)) {
                System.out.println("Supplier " + supplierId + " deleted.");
            } else {
                System.out.println("Supplier could not be deleted. Check the supplier ID is valid.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Input error: " + exception.getMessage());
        }
    }
}


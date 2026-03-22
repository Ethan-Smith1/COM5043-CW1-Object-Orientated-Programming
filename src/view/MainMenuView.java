package view;

import controller.InventoryManager;
import controller.OrderManager;
import controller.SupplierManager;

import java.util.Scanner;

public class MainMenuView extends CLReaderView {

    private final InventoryView inventoryView;
    private final SupplierView supplierView;
    private final OrderView orderView;

    public MainMenuView() {
        super(new Scanner(System.in));

        InventoryManager inventoryManager = new InventoryManager();
        SupplierManager supplierManager = new SupplierManager();
        OrderManager orderManager = new OrderManager(inventoryManager);

        this.inventoryView = new InventoryView(inventoryManager, supplierManager, scanner);
        this.supplierView = new SupplierView(scanner, supplierManager);
        this.orderView = new OrderView(scanner, orderManager, inventoryManager);
    }

    public void run() {
        System.out.println("Welcome to BNU Warehouse Management System");
        System.out.println("-----------------------------------");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readString("Choose an option");

            switch (choice) {
                case "1" -> inventoryView.run();
                case "2" -> supplierView.run();
                case "3" -> orderView.run();
                case "0" -> running = false;
                default -> System.out.println("Please choose an option between 0-3.");
            }
        }

        System.out.println("Exiting Warehouse Management System .");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("What do you want to manage?");
        System.out.println("1) Products");
        System.out.println("2) Suppliers");
        System.out.println("3) Orders");
        System.out.println("0) Exit");
    }
}


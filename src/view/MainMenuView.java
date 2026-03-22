package view;

import controller.FinanceManager;
import controller.InventoryManager;
import controller.OrderManager;
import controller.SupplierManager;

import java.util.Scanner;

public class MainMenuView extends CLReaderView {

    private final InventoryView inventoryView;
    private final SupplierView supplierView;
    private final OrderView orderView;
    private final FinanceView financeView;

    public MainMenuView() {
        super(new Scanner(System.in));

        InventoryManager inventoryManager = new InventoryManager();
        SupplierManager supplierManager = new SupplierManager();
        OrderManager orderManager = new OrderManager(inventoryManager);
        FinanceManager financeManager = new FinanceManager(orderManager);

        this.inventoryView = new InventoryView(inventoryManager, supplierManager, scanner);
        this.supplierView = new SupplierView(scanner, supplierManager);
        this.orderView = new OrderView(scanner, orderManager, inventoryManager);
        this.financeView = new FinanceView(scanner, financeManager);
    }

    public void run() {
        System.out.println();
        System.out.println("Welcome to the BNU Industry Solutions Ltd Warehouse Management System");
        System.out.println("----------------------------------------------------------------------");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readString("Choose an option");

            switch (choice) {
                case "1" -> inventoryView.run();
                case "2" -> supplierView.run();
                case "3" -> orderView.run();
                case "4" -> financeView.run();
                case "0" -> running = false;
                default -> System.out.println("Please choose an option between 0-4.");
            }
        }
        System.out.println();
        System.out.println("Exiting Warehouse Management System...");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("What do you want to manage?");
        System.out.println("1) Products");
        System.out.println("2) Suppliers");
        System.out.println("3) Orders");
        System.out.println("4) Finances");
        System.out.println("0) Exit");
    }
}


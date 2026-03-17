package view;

import controller.ProductManager;
import controller.SupplierManager;

import java.util.Scanner;

public class MainMenuView extends CLReaderView {

    private final ProductView productView;
    private final SupplierView supplierView;
    private final OrderView orderView;

    public MainMenuView() {
        super(new Scanner(System.in));

        ProductManager productManager = new ProductManager();
        SupplierManager supplierManager = new SupplierManager();

        this.productView = new ProductView(scanner);
        this.supplierView = new SupplierView(scanner);
        this.orderView = new OrderView(scanner);
    }

    public void run() {
        System.out.println("BNU Warehouse Management System (CLI)");
        System.out.println("-----------------------------------");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readString("Choose an option");

            switch (choice) {
                case "1" -> productView.run();
                case "2" -> supplierView.run();
                case "3" -> orderView.run();
                case "0" -> running = false;
                default -> System.out.println("Please choose an option between 0-3.");
            }
        }

        System.out.println("Exiting CLI.");
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


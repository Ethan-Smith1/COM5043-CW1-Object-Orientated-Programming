package view;

import java.util.Scanner;

public class SupplierView extends CLReaderView {

    public SupplierView(Scanner scanner) {
        super(scanner);
    }

    public void run() {
        System.out.println();
        System.out.println("Supplier Management");
        System.out.print("Press Enter to go back to the main menu...");
        scanner.nextLine();
    }
}


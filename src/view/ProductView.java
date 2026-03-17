package view;

import java.util.Scanner;

public class ProductView extends CLReaderView {

    public ProductView(Scanner scanner) {
        super(scanner);
    }

    public void run() {
        System.out.println();
        System.out.println("Product Management");
        System.out.print("Press Enter to go back to the main menu...");
        scanner.nextLine();
    }
}

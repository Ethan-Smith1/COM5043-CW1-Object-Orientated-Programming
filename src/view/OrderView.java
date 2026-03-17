package view;

import java.util.Scanner;

public class OrderView extends CLReaderView {

    public OrderView(Scanner scanner) {
        super(scanner);
    }

    public void run() {
        System.out.println();
        System.out.println("Order Management");
        System.out.print("Press Enter to go back to the main menu...");
        scanner.nextLine();
    }
}


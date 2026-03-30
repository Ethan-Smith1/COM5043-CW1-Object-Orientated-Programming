package view;

import java.util.Scanner;

public abstract class CLReaderView {

    protected final Scanner scanner;

    protected CLReaderView(Scanner scanner) {
        this.scanner = scanner;
    }

    protected String readString(String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine();
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
            System.out.println(label + " is required.");
        }
    }

    protected int readInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine();
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    protected double readPrice() {
        while (true) {
            System.out.print("Price: ");
            String value = scanner.nextLine();
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid price.");
            }
        }
    }
}



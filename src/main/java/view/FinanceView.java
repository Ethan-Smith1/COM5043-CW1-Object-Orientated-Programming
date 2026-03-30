package view;

import controller.FinanceManager;
import model.Transaction;

import java.util.List;
import java.util.Scanner;

public class FinanceView extends CLReaderView {

    private static final String TRANSACTION_TABLE_FORMAT = "%-5s %-12s %-12s %-40s%n";

    private final FinanceManager financeManager;

    public FinanceView(Scanner scanner, FinanceManager financeManager) {
        super(scanner);
        this.financeManager = financeManager;
    }

    public void run() {
        boolean financialMenu = true;
        while (financialMenu) {
            printFinancialMenu();
            String choice = readString("Choose a financial option");

            switch (choice) {
                case "1" -> showAllTransactions();
                case "2" -> generateSalesReportFile();
                case "3" -> generateFinancialSummaryFile();
                case "0" -> financialMenu = false;
                default -> System.out.println("Invalid option. Please choose 0-3.");
            }
        }
    }

    private void printFinancialMenu() {
        System.out.println();
        System.out.println("Financial Management");
        System.out.println("1) View all transactions");
        System.out.println("2) Generate sales report");
        System.out.println("3) Generate financial summary");
        System.out.println("0) Back to main menu");
    }

    private void showAllTransactions() {
        printTransactions(financeManager.getAllTransactions());
    }

    private void generateSalesReportFile() {
        if (financeManager.generateSalesReport()) {
            System.out.println("Sales report generated successfully.");
        } else {
            System.out.println("Failed to generate sales report.");
        }
    }

    private void generateFinancialSummaryFile() {
        if (financeManager.generateFinancialSummary()) {
            System.out.println("Financial summary generated successfully.");
        } else {
            System.out.println("Failed to generate financial summary.");
        }
    }

    private void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf(TRANSACTION_TABLE_FORMAT, "ID", "Date", "Amount", "Description");
        System.out.println("===================================================================================================================================");

        for (Transaction transaction : transactions) {
            System.out.printf(TRANSACTION_TABLE_FORMAT,
                    transaction.getId(),
                    transaction.getDate().toLocalDate(),
                    String.format("£%.2f", transaction.getAmount()),
                    transaction.getDescription());
        }
    }
}

package controller;

import model.Order;
import model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceManager {

    private static final String REPORTS_DIR = "financialReports";
    private final OrderManager orderManager;

    public FinanceManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public List<Transaction> getSales() {
        List<Transaction> sales = new ArrayList<>();
        List<Order> orders = orderManager.getAllOrders();

        for (Order order : orders) {
            String description = "Order " + order.getId() + " - " + order.getQuantity() + " x " + order.getProductName() + "(" + order.getProductId()+")";
            Transaction transaction = new Transaction(order.getId(), description, order.getTotalPrice(), order.getOrderDate());
            sales.add(transaction);
        }

        return sales;
    }

    public List<Transaction> getAllTransactions() {
        return getSales();
    }

    public double getTotalSales() {
        return calculateTotal(getSales());
    }

    public double getTotalCost() {
        // Items cost 30% of the selling price
        return getTotalSales() * 0.30;
    }

    public double getNetProfit() {
        // Net profit is 70% of sales
        return getTotalSales() * 0.70;
    }

    private double calculateTotal(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public boolean generateSalesReport() {
        try {
            String filename = REPORTS_DIR + "/sales_report_" + LocalDate.now() + ".txt";
            List<Transaction> sales = getSales();
            double totalSales = getTotalSales();

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("SALES REPORT\n");
                writer.write("Generated: " + LocalDate.now() + "\n");
                writer.write("================================\n\n");

                if (sales.isEmpty()) {
                    writer.write("No sales found.\n");
                } else {
                    for (Transaction transaction : sales) {
                        writer.write("Order ID: " + transaction.getId() + "\n");
                        writer.write("Amount: £" + String.format("%.2f", transaction.getAmount()) + "\n");
                        writer.write("Date: " + transaction.getDate().toLocalDate() + "\n");
                        writer.write("---\n");
                    }
                    writer.write("\nTOTAL SALES: £" + String.format("%.2f", totalSales) + "\n");
                }
                writer.write("\n================================\n");
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error generating sales report: " + e.getMessage());
            return false;
        }
    }

    public boolean generateFinancialSummary() {
        try {
            String filename = REPORTS_DIR + "/financial_summary_" + LocalDate.now() + ".txt";
            double totalSales = getTotalSales();
            double totalCost = getTotalCost();
            double netProfit = getNetProfit();

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("FINANCIAL SUMMARY\n");
                writer.write("Generated: " + LocalDate.now() + "\n");
                writer.write("================================\n\n");

                writer.write("Total Sales: £" + String.format("%.2f", totalSales) + "\n");
                writer.write("Cost of Goods: £" + String.format("%.2f", totalCost) + "\n");
                writer.write("Net Profit: £" + String.format("%.2f", netProfit) + "\n");
                writer.write("Number of Orders: " + getSales().size() + "\n");

                writer.write("\n================================\n");
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error generating financial summary: " + e.getMessage());
            return false;
        }
    }
}

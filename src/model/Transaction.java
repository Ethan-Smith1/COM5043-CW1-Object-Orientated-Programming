package model;

import java.time.LocalDateTime;

public class Transaction {
    private final int id;
    private final String description;
    private final double amount;
    private final LocalDateTime date;

    // Class constructor
    public Transaction(int id, String description, double amount, LocalDateTime date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Class methods
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}

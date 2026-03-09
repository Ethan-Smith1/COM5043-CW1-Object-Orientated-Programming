package model;

public abstract class WarehouseItem {
    private final String id;
    private final String name;
    private double price;

    //Class Constructor
    public WarehouseItem(String id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Class methods
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price=price;
    }
}

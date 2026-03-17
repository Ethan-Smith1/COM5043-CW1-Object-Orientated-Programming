package model;

public abstract class WarehouseItem {
    private final String id;
    private final String name;
    private double price;
    private int stockQuantity;
    private final int stockThreshold;
    private String supplierId;

    //Class Constructor
    protected WarehouseItem(String id, String name, double price, int stockQuantity, int stockThreshold, String supplierId){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.stockThreshold = stockThreshold;
        this.supplierId = supplierId;
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

    public int getQuantity() {
        return stockQuantity;
    }

    public boolean isLowStock(){
        return stockQuantity <= stockThreshold;
    }

    public int getStockThreshold(){
        return stockThreshold;
    }

    public String getSupplierId() {
        return supplierId;
    }


    public abstract String getProductType();
}


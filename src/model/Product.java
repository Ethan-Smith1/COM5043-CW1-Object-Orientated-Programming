package model;

public class Product extends WarehouseItem {

    private int stockQuantity;
    private final int stockThreshold;

    //Class constructor
    public Product(String id, String name, double price, int stockQuantity, int stockThreshold){
        super(id,name,price);
        this.stockQuantity = stockQuantity;
        this.stockThreshold = stockThreshold;
    }

    //Class methods
    public int getQuantity() {
        return stockQuantity;
    }
    public void setQuantity(int stockQuantity){
        this.stockQuantity = stockQuantity;
    }

    public boolean isLowStock(){
        return stockQuantity <= stockThreshold;
    }
}

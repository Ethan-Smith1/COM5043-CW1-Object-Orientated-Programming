package model;

public class Drill extends WarehouseItem {

    public static final int DEFAULT_DRILL_SIZE = 10;

    private int drillSize;


    public Drill(String id,
                 String name,
                 double price,
                 int stockQuantity,
                 int stockThreshold,
                 String supplierId,
                 int drillSize) {
        super(id, name, price, stockQuantity, stockThreshold, supplierId);
        this.drillSize = drillSize;
    }

    @Override
    public String getProductType() {
        return "Drill";
    }

    public int getDrillSize() {
        return drillSize;
    }
}


package model;

public class Forklift extends WarehouseItem {

    public static final int DEFAULT_LIFTING_CAPACITY_KG = 1000;

    private int liftingCapacityKg;


    public Forklift(String id,
                    String name,
                    double price,
                    int stockQuantity,
                    int stockThreshold,
                    String supplierId,
                    int liftingCapacityKg) {
        super(id, name, price, stockQuantity, stockThreshold, supplierId);
        this.liftingCapacityKg = liftingCapacityKg;
    }

    @Override
    public String getProductType() {
        return "Forklift";
    }

    public int getLiftingCapacityKg() {
        return liftingCapacityKg;
    }
}


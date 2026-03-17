package model;

public class Bricks extends WarehouseItem {

    public static final String DEFAULT_BRICK_TYPE = "Standard";

    private String brickType;


    public Bricks(String id,
                  String name,
                  double price,
                  int stockQuantity,
                  int stockThreshold,
                  String supplierId,
                  String brickType) {
        super(id, name, price, stockQuantity, stockThreshold, supplierId);
        this.brickType = brickType;
    }

    @Override
    public String getProductType() {
        return "Bricks";
    }

    public String getBrickType() {
        return brickType;
    }
}



package model;

public class Bricks extends WarehouseItem {

    private String brickType;

    public Bricks(String id, String name, double price, int stockQuantity, int stockThreshold, String supplierId) {
        this(id, name, price, stockQuantity, stockThreshold, supplierId, "Standard");
    }

    public Bricks(String id,
                  String name,
                  double price,
                  int stockQuantity,
                  int stockThreshold,
                  String supplierId,
                  String brickType) {
        super(id, name, price, stockQuantity, stockThreshold, supplierId);
        setBrickType(brickType);
    }

    @Override
    public String getProductType() {
        return "Bricks";
    }

    public String getBrickType() {
        return brickType;
    }

    public void setBrickType(String brickType) {
        if (brickType == null || brickType.isBlank()) {
            throw new IllegalArgumentException("Brick type is required.");
        }
        this.brickType = brickType.trim();
    }
}



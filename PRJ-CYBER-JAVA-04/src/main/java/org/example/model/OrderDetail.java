package org.example.model;

/**
 * OrderDetail - Entity đại diện cho chi tiết 1 dòng trong đơn hàng F&B
 *
 * Quan hệ: Order (1) --- (N) OrderDetail (N) --- (1) Food
 */
public class OrderDetail {

    private int id;
    private int orderId;
    private int foodId;
    private String foodName; // Transient - để hiển thị
    private int quantity;
    private double unitPrice; // Giá tại thời điểm đặt (snapshot)
    private double subtotal; // = quantity × unitPrice

    public OrderDetail() {
    }

    public OrderDetail(int id, int orderId, int foodId, String foodName,
            int quantity, double unitPrice, double subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public OrderDetail(int foodId, String foodName, int quantity, double unitPrice) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return String.format("OrderDetail{foodId=%d, name='%s', qty=%d, unit=%.0f, subtotal=%.0f}",
                foodId, foodName, quantity, unitPrice, subtotal);
    }
}

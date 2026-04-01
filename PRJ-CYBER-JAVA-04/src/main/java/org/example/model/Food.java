package org.example.model;

/**
 * Food - Entity đại diện cho món ăn/thức uống trong menu F&B
 *
 * Trạng thái: AVAILABLE / UNAVAILABLE
 */
public class Food {

    private int id;
    private String name; // "Mì tôm trứng", "Sting dâu"
    private String description; // Mô tả sản phẩm
    private double price; // Giá bán (VNĐ)
    private int stockQuantity; // Số lượng tồn kho
    private String status; // "AVAILABLE" | "UNAVAILABLE"

    public Food() {
    }

    public Food(int id, String name, String description, double price, int stockQuantity, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    public Food(String name, String description, double price, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = "AVAILABLE";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Food{id=%d, name='%s', price=%.0f, stock=%d, status='%s'}",
                id, name, price, stockQuantity, status);
    }
}

package bai5.model;

public class Product {
    private final String code;
    private final String name;
    private final long price;
    private final String category;

    public Product(String code, String name, long price, String category) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return code + " - " + name + " - " + price + " - " + category;
    }
}

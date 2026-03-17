package bai1;

public class Product {
    private final String code;
    private final String name;
    private final long price;

    public Product(String code, String name, long price) {
        this.code = code;
        this.name = name;
        this.price = price;
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

    @Override
    public String toString() {
        return code + " - " + name + " - " + price;
    }
}

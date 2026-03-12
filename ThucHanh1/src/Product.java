public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String category;

    public Product() {
    }

    public Product(int id, String name, double price, int quantity, String category) {
        setId(id);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setCategory(category);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Danh mục không được để trống.");
        }
        this.category = category.trim();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Số lượng không được âm.");
        }
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Giá phải lớn hơn 0.");
        }
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        this.name = name.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phải lớn hơn 0.");
        }
        this.id = id;
    }

    public String toTableRow() {
        return String.format("| %-5d | %-20s | %-12.2f | %-10d | %-15s |", id, name, price, quantity, category);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                '}';
    }
}

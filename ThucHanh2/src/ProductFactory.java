public class ProductFactory {
    private ProductFactory() {
    }

    public static Product createProduct(int type, String id, String name, double price, double extra) {
        switch (type) {
            case 1:
                return new PhysicalProduct(id, name, price, extra);
            case 2:
                return new DigitalProduct(id, name, price, extra);
            default:
                throw new IllegalArgumentException("Invalid product type: " + type);
        }
    }
}

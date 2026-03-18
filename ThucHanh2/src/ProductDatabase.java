import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDatabase {
    private static final ProductDatabase INSTANCE = new ProductDatabase();

    private final List<Product> products = new ArrayList<>();

    private ProductDatabase() {
    }

    public static ProductDatabase getInstance() {
        return INSTANCE;
    }

    public boolean addProduct(Product product) {
        if (product == null) return false;
        if (findById(product.getId()) != null) return false;
        products.add(product);
        return true;
    }

    public boolean updateProduct(Product updatedProduct) {
        if (updatedProduct == null) return false;
        int idx = indexOfId(updatedProduct.getId());
        if (idx < 0) return false;
        products.set(idx, updatedProduct);
        return true;
    }

    public boolean deleteProduct(String id) {
        int idx = indexOfId(id);
        if (idx < 0) return false;
        products.remove(idx);
        return true;
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product findById(String id) {
        int idx = indexOfId(id);
        return idx < 0 ? null : products.get(idx);
    }

    private int indexOfId(String id) {
        if (id == null) return -1;
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p != null && id.equalsIgnoreCase(p.getId())) {
                return i;
            }
        }
        return -1;
    }
}

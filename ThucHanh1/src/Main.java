import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<Product> PRODUCTS = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    displayProducts();
                    break;
                case 3:
                    updateQuantityById();
                    break;
                case 4:
                    deleteOutOfStockProducts();
                    break;
                case 5:
                    running = false;
                    System.out.println("Đã thoát chương trình.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn từ 1 đến 5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========== PRODUCT MANAGEMENT SYSTEM ==========");
        System.out.println("1. Thêm sản phẩm mới");
        System.out.println("2. Hiển thị danh sách sản phẩm");
        System.out.println("3. Cập nhật số lượng theo ID");
        System.out.println("4. Xóa sản phẩm đã hết hàng");
        System.out.println("5. Thoát chương trình");
        System.out.println("===============================================");
    }

    private static void addProduct() {
        try {
            int id = readInt("Nhập ID sản phẩm: ");
            ensureIdDoesNotExist(id);

            String name = readNonBlank("Nhập tên sản phẩm: ");
            double price = readDouble();
            int quantity = readInt("Nhập số lượng: ");
            String category = readNonBlank("Nhập danh mục: ");

            Product product = new Product(id, name, price, quantity, category);
            PRODUCTS.add(product);
            System.out.println("Thêm sản phẩm thành công.");
        } catch (InvalidProductException | IllegalArgumentException exception) {
            System.out.println("Lỗi: " + exception.getMessage());
        }
    }

    private static void displayProducts() {
        if (PRODUCTS.isEmpty()) {
            System.out.println("Danh sách sản phẩm đang trống.");
            return;
        }

        System.out.println("| ID    | Tên sản phẩm         | Giá          | Số lượng   | Danh mục        |");
        PRODUCTS.stream()
                .map(Product::toTableRow)
                .forEach(System.out::println);
        System.out.println("Tổng số sản phẩm: " + PRODUCTS.size());
    }

    private static void updateQuantityById() {
        try {
            int id = readInt("Nhập ID sản phẩm cần cập nhật: ");
            Optional<Product> productOptional = findProductById(id);

            if (productOptional.isEmpty()) {
                throw new InvalidProductException("Không tìm thấy sản phẩm có ID = " + id + ".");
            }

            int newQuantity = readInt("Nhập số lượng mới: ");
            productOptional.get().setQuantity(newQuantity);
            System.out.println("Cập nhật số lượng thành công.");
        } catch (InvalidProductException | IllegalArgumentException exception) {
            System.out.println("Lỗi: " + exception.getMessage());
        }
    }

    private static void deleteOutOfStockProducts() {
        long deletedCount = PRODUCTS.stream()
                .filter(product -> product.getQuantity() == 0)
                .count();

        if (deletedCount == 0) {
            System.out.println("Không có sản phẩm nào hết hàng để xóa.");
            return;
        }

        PRODUCTS.removeIf(product -> product.getQuantity() == 0);
        System.out.println("Đã xóa " + deletedCount + " sản phẩm hết hàng.");
    }

    private static Optional<Product> findProductById(int id) {
        return PRODUCTS.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    private static void ensureIdDoesNotExist(int id) throws InvalidProductException {
        if (findProductById(id).isPresent()) {
            throw new InvalidProductException("ID " + id + " đã tồn tại trong danh sách.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ.");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            System.out.print("Nhập giá sản phẩm: ");
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số thực hợp lệ.");
            }
        }
    }

    private static String readNonBlank(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Dữ liệu không được để trống.");
        }
    }
}
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductDatabase db = ProductDatabase.getInstance();
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = readInt(sc, "Lựa chọn của bạn: ", 1, 5);

            switch (choice) {
                case 1:
                    handleAdd(sc, db);
                    break;
                case 2:
                    handleList(db);
                    break;
                case 3:
                    handleUpdate(sc, db);
                    break;
                case 4:
                    handleDelete(sc, db);
                    break;
                case 5:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------------- QUẢN LÝ SẢN PHẨM ----------------------");
        System.out.println("1. Thêm mới sản phẩm");
        System.out.println("2. Xem danh sách sản phẩm");
        System.out.println("3. Cập nhật thông tin sản phẩm");
        System.out.println("4. Xoá sản phẩm");
        System.out.println("5. Thoát");
        System.out.println("-----------------------------------------------------------------------");
    }

    private static void handleAdd(Scanner sc, ProductDatabase db) {
        System.out.println("\n[Thêm mới sản phẩm]");
        int type = readInt(sc, "Chọn loại (1: Physical, 2: Digital): ", 1, 2);

        String id;
        while (true) {
            id = readNonEmptyString(sc, "Nhập id: ");
            if (db.findById(id) != null) {
                System.out.println("Id đã tồn tại. Vui lòng nhập id khác.");
            } else {
                break;
            }
        }

        String name = readNonEmptyString(sc, "Nhập tên: ");
        double price = readDouble(sc, "Nhập giá: ", 0, Double.MAX_VALUE);

        double extra;
        if (type == 1) {
            extra = readDouble(sc, "Nhập trọng lượng (kg): ", 0, Double.MAX_VALUE);
        } else {
            extra = readDouble(sc, "Nhập dung lượng (MB): ", 0, Double.MAX_VALUE);
        }

        Product p = ProductFactory.createProduct(type, id, name, price, extra);
        boolean ok = db.addProduct(p);
        System.out.println(ok ? "Thêm sản phẩm thành công." : "Thêm sản phẩm thất bại.");
    }

    private static void handleList(ProductDatabase db) {
        System.out.println("\n[Danh sách sản phẩm]");
        List<Product> all = db.getAllProducts();
        if (all.isEmpty()) {
            System.out.println("Chưa có sản phẩm nào.");
            return;
        }
        for (int i = 0; i < all.size(); i++) {
            System.out.print((i + 1) + ". ");
            all.get(i).displayInfo();
        }
    }

    private static void handleUpdate(Scanner sc, ProductDatabase db) {
        System.out.println("\n[Cập nhật sản phẩm]");
        String id = readNonEmptyString(sc, "Nhập id cần cập nhật: ");
        Product existing = db.findById(id);
        if (existing == null) {
            System.out.println("Không tìm thấy sản phẩm với id: " + id);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        existing.displayInfo();

        int type;
        if (existing instanceof PhysicalProduct) {
            type = 1;
        } else if (existing instanceof DigitalProduct) {
            type = 2;
        } else {
            System.out.println("Loại sản phẩm không hợp lệ (không hỗ trợ cập nhật).");
            return;
        }

        String name = readNonEmptyString(sc, "Nhập tên mới: ");
        double price = readDouble(sc, "Nhập giá mới: ", 0, Double.MAX_VALUE);

        double extra;
        if (type == 1) {
            extra = readDouble(sc, "Nhập trọng lượng mới (kg): ", 0, Double.MAX_VALUE);
        } else {
            extra = readDouble(sc, "Nhập dung lượng mới (MB): ", 0, Double.MAX_VALUE);
        }

        Product updated = ProductFactory.createProduct(type, existing.getId(), name, price, extra);
        boolean ok = db.updateProduct(updated);
        System.out.println(ok ? "Cập nhật thành công." : "Cập nhật thất bại.");
    }

    private static void handleDelete(Scanner sc, ProductDatabase db) {
        System.out.println("\n[Xoá sản phẩm]");
        String id = readNonEmptyString(sc, "Nhập id cần xoá: ");
        boolean ok = db.deleteProduct(id);
        System.out.println(ok ? "Xoá thành công." : "Không tìm thấy sản phẩm để xoá.");
    }

    private static String readNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null) {
                s = s.trim();
            }
            if (s != null && !s.isEmpty()) return s;
            System.out.println("Giá trị không được để trống. Vui lòng thử lại.");
        }
    }

    private static int readInt(Scanner sc, String prompt, int minInclusive, int maxInclusive) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine();
            try {
                int val = Integer.parseInt(raw.trim());
                if (val < minInclusive || val > maxInclusive) {
                    System.out.println("Vui lòng nhập số trong khoảng [" + minInclusive + ", " + maxInclusive + "]");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("Dữ liệu không hợp lệ. Vui lòng nhập số nguyên.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt, double minInclusive, double maxInclusive) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine();
            try {
                double val = Double.parseDouble(raw.trim());
                if (val < minInclusive || val > maxInclusive) {
                    System.out.println("Vui lòng nhập số trong khoảng [" + minInclusive + ", " + maxInclusive + "]");
                    continue;
                }
                return val;
            } catch (Exception e) {
                System.out.println("Dữ liệu không hợp lệ. Vui lòng nhập số.");
            }
        }
    }
}
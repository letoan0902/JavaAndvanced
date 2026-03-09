import java.io.IOException;

public class Bai4 {
    public static void main(String[] args) {
        try {
            processUserData();
            System.out.println("Dữ liệu đã được xử lý thành công.");
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi khi lưu dữ liệu người dùng: " + e.getMessage());
        }
        System.out.println("Chương trình kết thúc an toàn.");
    }

    public static void processUserData() throws IOException {
        saveToFile();
    }

    public static void saveToFile() throws IOException {
        throw new IOException("Không thể ghi dữ liệu xuống file.");
    }
}

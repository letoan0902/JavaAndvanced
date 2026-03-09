import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Nhập năm sinh: ");
            String birthYearText = scanner.nextLine();
            int birthYear = Integer.parseInt(birthYearText);
            int age = java.time.Year.now().getValue() - birthYear;
            System.out.println("Tuổi của bạn là: " + age);
        } catch (NumberFormatException e) {
            System.out.println("Dữ liệu không hợp lệ. Vui lòng nhập năm sinh bằng số.");
        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally...");
        }
    }
}

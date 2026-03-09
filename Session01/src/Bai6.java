import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Bai6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            handleBirthYear(scanner);
            divideUsers(scanner);
            handleUserValidation();
            try {
                processUserData();
            } catch (IOException e) {
                logError("Lỗi khi lưu dữ liệu người dùng", e);
            }
            printUserName("Nguyen Van A");
            printUserName(null);
        } finally {
            scanner.close();
            System.out.println("Thực hiện dọn dẹp tài nguyên trong finally...");
        }
    }

    private static void handleBirthYear(Scanner scanner) {
        try {
            System.out.print("Nhập năm sinh: ");
            String birthYearText = scanner.nextLine();
            int birthYear = Integer.parseInt(birthYearText);
            int age = java.time.Year.now().getValue() - birthYear;
            System.out.println("Tuổi hiện tại: " + age);
        } catch (NumberFormatException e) {
            logError("Năm sinh không hợp lệ", e);
        }
    }

    private static void divideUsers(Scanner scanner) {
        try {
            System.out.print("Nhập tổng số người dùng: ");
            int totalUsers = Integer.parseInt(scanner.nextLine());
            System.out.print("Nhập số lượng nhóm: ");
            int groups = Integer.parseInt(scanner.nextLine());
            int result = totalUsers / groups;
            System.out.println("Mỗi nhóm có: " + result + " người");
        } catch (ArithmeticException e) {
            logError("Không thể chia cho 0", e);
        } catch (NumberFormatException e) {
            logError("Dữ liệu chia nhóm không hợp lệ", e);
        }
    }

    private static void handleUserValidation() {
        UserBai6 user = new UserBai6();
        try {
            user.setAge(-2);
            System.out.println("Tuổi người dùng: " + user.getAge());
        } catch (InvalidAgeExceptionBai6 e) {
            logError("Tuổi không hợp lệ theo quy tắc nghiệp vụ", e);
        }
    }

    private static void processUserData() throws IOException {
        saveToFile();
    }

    private static void saveToFile() throws IOException {
        throw new IOException("Môi trường ghi file không sẵn sàng.");
    }

    private static void printUserName(String userName) {
        if (userName != null) {
            System.out.println("Tên người dùng: " + userName);
        } else {
            System.out.println("Tên người dùng chưa được cung cấp.");
        }
    }

    private static void logError(String message, Exception e) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[ERROR] " + timestamp + " - " + message + ": " + e.getMessage());
    }
}

class UserBai6 {
    private int age;

    public void setAge(int age) throws InvalidAgeExceptionBai6 {
        if (age < 0) {
            throw new InvalidAgeExceptionBai6("Tuổi không thể âm!");
        }
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

class InvalidAgeExceptionBai6 extends Exception {
    public InvalidAgeExceptionBai6(String msg) {
        super(msg);
    }
}

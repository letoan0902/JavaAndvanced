import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bai5 {
    public static void main(String[] args) {
        UserBai5 user = new UserBai5();
        try {
            user.setAge(-10);
            System.out.println("Tuổi hợp lệ: " + user.getAge());
        } catch (InvalidAgeException e) {
            logError("Không thể cập nhật tuổi người dùng", e);
        }
    }

    private static void logError(String message, Exception e) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[ERROR] " + timestamp + " - " + message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }
}

class UserBai5 {
    private int age;

    public void setAge(int age) throws InvalidAgeException {
        if (age < 0) {
            throw new InvalidAgeException("Tuổi không thể âm!");
        }
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

class InvalidAgeException extends Exception {
    public InvalidAgeException(String msg) {
        super(msg);
    }
}

@FunctionalInterface
interface PasswordValidator {
    boolean isValid(String password);
}

public class Bai2 {
    public static void main(String[] args) {
        PasswordValidator validator = password -> password.length() >= 8;

        System.out.println("12345678 -> " + validator.isValid("12345678"));
        System.out.println("1234 -> " + validator.isValid("1234"));
    }
}

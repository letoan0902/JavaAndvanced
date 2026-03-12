package bai4;

public class PasswordValidator {
    public String evaluatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "Yếu";
        }

        if (password.length() < 8) {
            return "Yếu";
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.contains(String.valueOf(c))) hasSpecial = true;
        }

        int score = 0;
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;

        if (score == 4) {
            return "Mạnh";
        } else if (score >= 3) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}

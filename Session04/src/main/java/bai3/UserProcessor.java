package bai3;

public class UserProcessor {
    public String processEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }

        String[] parts = email.split("@");
        if (parts.length != 2 || parts[1].isEmpty() || !parts[1].contains(".")) {
             int atIndex = email.indexOf("@");
             if (atIndex == email.length() - 1) {
                 throw new IllegalArgumentException("Email must contain domain");
             }
             String domain = email.substring(atIndex + 1);
             if (!domain.contains(".")) {
                 throw new IllegalArgumentException("Email domain must contain a dot");
             }
        }

        return email.toLowerCase();
    }
}

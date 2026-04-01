package org.example.util;

import java.util.Scanner;

/**
 * InputValidator - Kiểm tra và xác thực dữ liệu đầu vào từ Console
 *
 * Áp dụng:
 * - Exception Handling (Session 1-3): try-catch cho NumberFormatException
 * - Validation (Yêu cầu phi chức năng): Mật khẩu ≥ 6 ký tự, giá không âm, SĐT
 * đúng format
 */
public class InputValidator {

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[Lỗi] Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[Lỗi] Vui lòng nhập số hợp lệ!");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[Lỗi] Không được để trống!");
        }
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Validate số điện thoại: 10-11 chữ số, bắt đầu bằng 0
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^0\\d{9,10}$");
    }

    // Validate giá tiền: không âm
    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    // Validate số lượng: không âm
    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    // Đọc xác nhận Y/N từ người dùng
    public static boolean readConfirmation(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y"))
                return true;
            if (input.equals("N"))
                return false;
            System.out.println("[Lỗi] Vui lòng nhập Y hoặc N!");
        }
    }

    // Đọc số nguyên dương (> 0)
    public static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value > 0)
                return value;
            System.out.println("[Lỗi] Vui lòng nhập số lớn hơn 0!");
        }
    }

    // Đọc số thực dương (> 0)
    public static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            double value = readDouble(scanner, prompt);
            if (value > 0)
                return value;
            System.out.println("[Lỗi] Vui lòng nhập số lớn hơn 0!");
        }
    }
}

package org.example.presentation;

import org.example.model.User;
import org.example.service.IAuthService;
import org.example.service.AuthService;
import org.example.util.ConsoleFormatter;
import org.example.util.InputValidator;

import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner;
    private final IAuthService authService;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }

    public void show() {
        while (true) {
            ConsoleFormatter.printGridMenu("HỆ THỐNG QUẢN LÝ CYBER GAMING", new String[]{
                    "1. Đăng nhập",
                    "2. Đăng ký",
                    "0. Thoát"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 0:
                    System.out.println("Tạm biệt! Hẹn gặp lại.");
                    return;
                default:
                    ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleLogin() {
        ConsoleFormatter.printSubHeader("ĐĂNG NHẬP");

        String username = InputValidator.readNonEmptyString(scanner, "Tên đăng nhập: ");
        String password = InputValidator.readNonEmptyString(scanner, "Mật khẩu: ");

        User user = authService.login(username, password);

        if (user != null) {
            ConsoleFormatter.printSuccess("Đăng nhập thành công! Xin chào, " + user.getFullName());
            System.out.println("Vai trò: " + user.getRole());
            System.out.println();

            switch (user.getRole()) {
                case "ADMIN":
                    new AdminMenu(scanner, user).show();
                    break;
                case "STAFF":
                    new StaffMenu(scanner, user).show();
                    break;
                case "CUSTOMER":
                    new CustomerMenu(scanner, user).show();
                    break;
                default:
                    ConsoleFormatter.printError("Vai trò không hợp lệ!");
            }
        } else {
            ConsoleFormatter.printError("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private void handleRegister() {
        ConsoleFormatter.printSubHeader("ĐĂNG KÝ TÀI KHOẢN");

        String username = InputValidator.readNonEmptyString(scanner, "Tên đăng nhập: ");

        String password;
        while (true) {
            password = InputValidator.readNonEmptyString(scanner, "Mật khẩu (tối thiểu 6 ký tự): ");
            if (InputValidator.isValidPassword(password)) {
                break;
            }
            ConsoleFormatter.printError("Mật khẩu phải có ít nhất 6 ký tự!");
        }

        String fullName = InputValidator.readNonEmptyString(scanner, "Họ và tên: ");

        String phone;
        while (true) {
            phone = InputValidator.readNonEmptyString(scanner, "Số điện thoại: ");
            if (InputValidator.isValidPhone(phone)) {
                break;
            }
            ConsoleFormatter.printError("Số điện thoại không hợp lệ! (10-11 số, bắt đầu bằng 0)");
        }

        boolean success = authService.register(username, password, fullName, phone);

        if (success) {
            ConsoleFormatter.printSuccess("Đăng ký thành công! Vui lòng đăng nhập.");
        } else {
            ConsoleFormatter.printError("Đăng ký thất bại! Tên đăng nhập đã tồn tại.");
        }
    }
}

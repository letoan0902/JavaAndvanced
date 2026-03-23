package org.example.bai5.presentation;

import org.example.bai5.dao.DoctorDao;
import org.example.bai5.model.Doctor;
import org.example.bai5.service.DoctorService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Presentation layer: menu console chạy liên tục.
 */
public class ConsoleMenu {

    private final DoctorService doctorService;

    public ConsoleMenu(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public void start() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                printMenu();
                System.out.print("Chọn chức năng (1-4): ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1" -> handleListDoctors();
                    case "2" -> handleAddDoctor(sc);
                    case "3" -> handleSpecialtyStats();
                    case "4" -> {
                        System.out.println("Thoát chương trình.");
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 1-4.");
                }

                System.out.println();
            }
        }
    }

    private void printMenu() {
        System.out.println("===== Rikkei-Care | Doctor On-call Management =====");
        System.out.println("1. Xem danh sách bác sĩ");
        System.out.println("2. Thêm bác sĩ mới");
        System.out.println("3. Thống kê chuyên khoa");
        System.out.println("4. Thoát");
    }

    private void handleListDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            if (doctors.isEmpty()) {
                System.out.println("Danh sách bác sĩ đang trống.");
                return;
            }

            System.out.printf("%-15s | %-30s | %-20s%n", "Mã số", "Họ tên", "Chuyên khoa");
            System.out.println("---------------------------------------------------------------------");
            for (Doctor d : doctors) {
                System.out.printf("%-15s | %-30s | %-20s%n",
                        d.getId(),
                        d.getFullName(),
                        d.getSpecialty());
            }
            System.out.println("Tổng: " + doctors.size());
        } catch (SQLException e) {
            System.err.println("Lỗi DB khi lấy danh sách bác sĩ: " + e.getMessage());
        }
    }

    private void handleAddDoctor(Scanner sc) {
        System.out.print("Nhập mã bác sĩ: ");
        String id = sc.nextLine();
        System.out.print("Nhập họ tên: ");
        String fullName = sc.nextLine();
        System.out.print("Nhập chuyên khoa: ");
        String specialty = sc.nextLine();

        try {
            int affected = doctorService.addDoctor(id, fullName, specialty);
            if (affected == 1) {
                System.out.println("Thêm bác sĩ thành công.");
            } else {
                // Hiếm gặp: insert không tác động dòng nào
                System.out.println("Thêm bác sĩ thất bại (không có dòng nào được thêm).");
            }
        } catch (DoctorService.ValidationException e) {
            System.out.println("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (SQLException e) {
            // Kịch bản lỗi thường gặp: trùng PK, vượt độ dài cột, lỗi kiểu dữ liệu...
            System.err.println("Lỗi DB khi thêm bác sĩ: " + e.getMessage());
        }
    }

    private void handleSpecialtyStats() {
        try {
            List<DoctorDao.SpecialtyStat> stats = doctorService.getSpecialtyStats();
            if (stats.isEmpty()) {
                System.out.println("Chưa có dữ liệu để thống kê.");
                return;
            }

            System.out.printf("%-25s | %s%n", "Chuyên khoa", "Số lượng");
            System.out.println("--------------------------------------");
            for (DoctorDao.SpecialtyStat s : stats) {
                System.out.printf("%-25s | %d%n", s.getSpecialty(), s.getTotal());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi DB khi thống kê chuyên khoa: " + e.getMessage());
        }
    }

    /**
     * Liệt kê kịch bản lỗi (để nộp bài):
     *
     * 1) Nhập trùng mã bác sĩ (Primary Key) -> INSERT báo lỗi duplicate key.
     * 2) Nhập chuyên khoa/họ tên quá dài so với độ rộng cột DB -> Data too long.
     * 3) Nhập rỗng (id/name/specialty) -> nghiệp vụ từ chối (ValidationException).
     * 4) Sai định dạng ngày tháng (nếu DB có cột date và nhập từ console) -> parse error / SQL type mismatch.
     * 5) Mất kết nối DB / timeout -> SQLException communications link failure.
     * 6) Không đủ quyền truy cập bảng Doctors -> SQLException permission denied.
     */
}


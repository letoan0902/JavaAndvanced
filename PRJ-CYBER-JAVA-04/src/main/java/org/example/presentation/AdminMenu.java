package org.example.presentation;

import org.example.model.Category;
import org.example.model.Food;
import org.example.model.PC;
import org.example.model.User;
import org.example.service.IFoodService;
import org.example.service.FoodService;
import org.example.service.IPCService;
import org.example.service.PCService;
import org.example.service.IReportService;
import org.example.service.ReportService;
import org.example.util.ConsoleFormatter;
import org.example.util.InputValidator;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

// AdminMenu - Menu quan tri: Quan ly may tram + Quan ly F&B
public class AdminMenu {

    private final Scanner scanner;
    private final User currentUser;
    private final IPCService pcService;
    private final IFoodService foodService;
    private final IReportService reportService;

    public AdminMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.pcService = new PCService();
        this.foodService = new FoodService();
        this.reportService = new ReportService();
    }

    public void show() {
        while (true) {
            System.out.println("  Xin chào, " + currentUser.getFullName());
            ConsoleFormatter.printGridMenu("QUẢN TRỊ VIÊN", new String[]{
                    "1. Quản lý máy trạm",
                    "2. Quản lý dịch vụ F&B",
                    "3. Báo cáo & Thống kê",
                    "0. Đăng xuất"
            }, 4);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: showPCManagement(); break;
                case 2: showFoodManagement(); break;
                case 3: showReportMenu(); break;
                case 0:
                    ConsoleFormatter.printInfo("Đã đăng xuất.");
                    return;
                default:
                    ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ========== QUAN LY MAY TRAM ==========

    private void showPCManagement() {
        while (true) {
            ConsoleFormatter.printGridMenu("QUẢN LÝ MÁY TRẠM", new String[]{
                    "1. Xem danh sách máy",
                    "2. Thêm máy mới",
                    "3. Sửa thông tin máy",
                    "4. Xóa máy trạm",
                    "0. Quay lại"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: displayAllPCs(); break;
                case 2: addNewPC(); break;
                case 3: updatePC(); break;
                case 4: deletePC(); break;
                case 0: return;
                default: ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllPCs() {
        List<PC> pcs = pcService.getAllPCs();
        if (pcs.isEmpty()) {
            ConsoleFormatter.printInfo("Chưa có máy trạm nào trong hệ thống.");
            return;
        }

        ConsoleFormatter.printSubHeader("DANH SÁCH MÁY TRẠM");

        int[] widths = {4, 8, 12, 35, 15, 12};
        String[] headers = {"ID", "Số máy", "Khu vực", "Cấu hình", "Giá/giờ", "Trạng thái"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (PC pc : pcs) {
            String[] row = {
                    String.valueOf(pc.getId()),
                    pc.getPcNumber(),
                    pc.getCategoryName(),
                    pc.getConfigInfo(),
                    ConsoleFormatter.formatCurrency(pc.getPricePerHour()),
                    ConsoleFormatter.translatePCStatus(pc.getStatus())
            };
            ConsoleFormatter.printTableRow(row, widths);
        }

        ConsoleFormatter.printTableBottom(widths);
        System.out.println("Tổng: " + pcs.size() + " máy trạm");
    }

    private void addNewPC() {
        ConsoleFormatter.printSubHeader("THÊM MÁY TRẠM MỚI");

        String pcNumber = InputValidator.readNonEmptyString(scanner, "Số máy (VD: PC-09): ");

        List<Category> categories = pcService.getAllCategories();
        System.out.println("Danh sách khu vực:");
        for (Category cat : categories) {
            System.out.println("  " + cat.getId() + ". " + cat.getName() + " - " + cat.getDescription());
        }
        int categoryId = InputValidator.readPositiveInt(scanner, "Chọn khu vực (ID): ");

        String configInfo = InputValidator.readNonEmptyString(scanner, "Cấu hình (VD: i7-12700K, 32GB RAM, RTX 4070): ");
        double pricePerHour = InputValidator.readPositiveDouble(scanner, "Giá mỗi giờ (VND): ");

        PC newPC = new PC(pcNumber, categoryId, configInfo, pricePerHour);
        boolean success = pcService.addPC(newPC);

        if (success) {
            ConsoleFormatter.printSuccess("Thêm máy trạm " + pcNumber + " thành công!");
        } else {
            ConsoleFormatter.printError("Thêm thất bại! Kiểm tra ID khu vực hoặc số máy bị trùng.");
        }
    }

    private void updatePC() {
        ConsoleFormatter.printSubHeader("SỬA THÔNG TIN MÁY TRẠM");

        displayAllPCs();
        int id = InputValidator.readPositiveInt(scanner, "Nhập ID máy cần sửa: ");

        PC existingPC = pcService.getPCById(id);
        if (existingPC == null) {
            ConsoleFormatter.printError("Không tìm thấy máy trạm với ID = " + id);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println("  Số máy: " + existingPC.getPcNumber());
        System.out.println("  Khu vực: " + existingPC.getCategoryName() + " (ID: " + existingPC.getCategoryId() + ")");
        System.out.println("  Cấu hình: " + existingPC.getConfigInfo());
        System.out.println("  Giá/giờ: " + ConsoleFormatter.formatCurrency(existingPC.getPricePerHour()));
        System.out.println("  Trạng thái: " + ConsoleFormatter.translatePCStatus(existingPC.getStatus()));
        System.out.println();
        System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

        System.out.print("Số máy mới [" + existingPC.getPcNumber() + "]: ");
        String pcNumber = scanner.nextLine().trim();
        if (pcNumber.isEmpty()) pcNumber = existingPC.getPcNumber();

        List<Category> categories = pcService.getAllCategories();
        System.out.println("Danh sách khu vực:");
        for (Category cat : categories) {
            System.out.println("  " + cat.getId() + ". " + cat.getName());
        }
        System.out.print("Khu vực mới [" + existingPC.getCategoryId() + "]: ");
        String catInput = scanner.nextLine().trim();
        int categoryId = catInput.isEmpty() ? existingPC.getCategoryId() : Integer.parseInt(catInput);

        System.out.print("Cấu hình mới [" + existingPC.getConfigInfo() + "]: ");
        String configInfo = scanner.nextLine().trim();
        if (configInfo.isEmpty()) configInfo = existingPC.getConfigInfo();

        System.out.print("Giá/giờ mới [" + existingPC.getPricePerHour() + "]: ");
        String priceInput = scanner.nextLine().trim();
        double price = priceInput.isEmpty() ? existingPC.getPricePerHour() : Double.parseDouble(priceInput);

        System.out.print("Trạng thái (AVAILABLE/IN_USE/MAINTENANCE) [" + existingPC.getStatus() + "]: ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) status = existingPC.getStatus();

        PC updatedPC = new PC(existingPC.getId(), pcNumber, categoryId, null, configInfo, price, status);

        if (pcService.updatePC(updatedPC)) {
            ConsoleFormatter.printSuccess("Cập nhật máy trạm thành công!");
        } else {
            ConsoleFormatter.printError("Cập nhật thất bại!");
        }
    }

    private void deletePC() {
        ConsoleFormatter.printSubHeader("XÓA MÁY TRẠM");

        displayAllPCs();
        int id = InputValidator.readPositiveInt(scanner, "Nhập ID máy cần xóa: ");

        PC pc = pcService.getPCById(id);
        if (pc == null) {
            ConsoleFormatter.printError("Không tìm thấy máy trạm với ID = " + id);
            return;
        }

        System.out.println("Bạn chuẩn bị xóa: " + pc.getPcNumber() + " (" + pc.getCategoryName() + ")");
        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận xóa?");

        if (confirm) {
            if (pcService.deletePC(id)) {
                ConsoleFormatter.printSuccess("Đã xóa máy trạm " + pc.getPcNumber() + " thành công!");
            } else {
                ConsoleFormatter.printError("Xóa thất bại! Máy trạm có thể đang được sử dụng.");
            }
        } else {
            ConsoleFormatter.printInfo("Đã hủy thao tác xóa.");
        }
    }

    // ========== QUAN LY DICH VU F&B ==========

    private void showFoodManagement() {
        while (true) {
            ConsoleFormatter.printGridMenu("QUẢN LÝ DỊCH VỤ F&B", new String[]{
                    "1. Xem danh sách món",
                    "2. Thêm món mới",
                    "3. Sửa thông tin món",
                    "4. Xóa món",
                    "0. Quay lại"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: displayAllFoods(); break;
                case 2: addNewFood(); break;
                case 3: updateFood(); break;
                case 4: deleteFood(); break;
                case 0: return;
                default: ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        if (foods.isEmpty()) {
            ConsoleFormatter.printInfo("Chưa có món nào trong menu.");
            return;
        }

        ConsoleFormatter.printSubHeader("MENU ĐỒ ĂN & THỨC UỐNG");

        int[] widths = {4, 25, 30, 15, 8, 12};
        String[] headers = {"ID", "Tên món", "Mô tả", "Giá", "Tồn kho", "Trạng thái"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Food food : foods) {
            String statusVi = food.getStatus().equals("AVAILABLE") ? "Có sẵn" : "Hết hàng";
            String[] row = {
                    String.valueOf(food.getId()),
                    food.getName(),
                    food.getDescription(),
                    ConsoleFormatter.formatCurrency(food.getPrice()),
                    String.valueOf(food.getStockQuantity()),
                    statusVi
            };
            ConsoleFormatter.printTableRow(row, widths);
        }

        ConsoleFormatter.printTableBottom(widths);
        System.out.println("Tổng: " + foods.size() + " món");
    }

    private void addNewFood() {
        ConsoleFormatter.printSubHeader("THÊM MÓN MỚI");

        String name = InputValidator.readNonEmptyString(scanner, "Tên món: ");
        String description = InputValidator.readNonEmptyString(scanner, "Mô tả: ");
        double price = InputValidator.readPositiveDouble(scanner, "Giá bán (VND): ");
        int stock = InputValidator.readPositiveInt(scanner, "Số lượng tồn kho: ");

        Food newFood = new Food(name, description, price, stock);
        boolean success = foodService.addFood(newFood);

        if (success) {
            ConsoleFormatter.printSuccess("Thêm món \"" + name + "\" thành công!");
        } else {
            ConsoleFormatter.printError("Thêm món thất bại!");
        }
    }

    private void updateFood() {
        ConsoleFormatter.printSubHeader("SỬA THÔNG TIN MÓN");

        displayAllFoods();
        int id = InputValidator.readPositiveInt(scanner, "Nhập ID món cần sửa: ");

        Food existingFood = foodService.getFoodById(id);
        if (existingFood == null) {
            ConsoleFormatter.printError("Không tìm thấy món với ID = " + id);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println("  Tên: " + existingFood.getName());
        System.out.println("  Mô tả: " + existingFood.getDescription());
        System.out.println("  Giá: " + ConsoleFormatter.formatCurrency(existingFood.getPrice()));
        System.out.println("  Tồn kho: " + existingFood.getStockQuantity());
        System.out.println("  Trạng thái: " + existingFood.getStatus());
        System.out.println();
        System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)");

        System.out.print("Tên mới [" + existingFood.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existingFood.getName();

        System.out.print("Mô tả mới [" + existingFood.getDescription() + "]: ");
        String desc = scanner.nextLine().trim();
        if (desc.isEmpty()) desc = existingFood.getDescription();

        System.out.print("Giá mới [" + existingFood.getPrice() + "]: ");
        String priceInput = scanner.nextLine().trim();
        double price = priceInput.isEmpty() ? existingFood.getPrice() : Double.parseDouble(priceInput);

        System.out.print("Tồn kho mới [" + existingFood.getStockQuantity() + "]: ");
        String stockInput = scanner.nextLine().trim();
        int stock = stockInput.isEmpty() ? existingFood.getStockQuantity() : Integer.parseInt(stockInput);

        System.out.print("Trạng thái (AVAILABLE/UNAVAILABLE) [" + existingFood.getStatus() + "]: ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) status = existingFood.getStatus();

        Food updatedFood = new Food(id, name, desc, price, stock, status);

        if (foodService.updateFood(updatedFood)) {
            ConsoleFormatter.printSuccess("Cập nhật món thành công!");
        } else {
            ConsoleFormatter.printError("Cập nhật thất bại!");
        }
    }

    private void deleteFood() {
        ConsoleFormatter.printSubHeader("XÓA MÓN");

        displayAllFoods();
        int id = InputValidator.readPositiveInt(scanner, "Nhập ID món cần xóa: ");

        Food food = foodService.getFoodById(id);
        if (food == null) {
            ConsoleFormatter.printError("Không tìm thấy món với ID = " + id);
            return;
        }

        System.out.println("Bạn chuẩn bị xóa: " + food.getName() + " - " + ConsoleFormatter.formatCurrency(food.getPrice()));
        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận xóa?");
        if (confirm) {
            if (foodService.deleteFood(id)) {
                ConsoleFormatter.printSuccess("Đã xóa món \"" + food.getName() + "\" thành công!");
            } else {
                ConsoleFormatter.printError("Xóa thất bại!");
            }
        } else {
            ConsoleFormatter.printInfo("Đã hủy thao tác xóa.");
        }
    }

    // ========== BAO CAO & THONG KE ==========

    private void showReportMenu() {
        while (true) {
            ConsoleFormatter.printGridMenu("BÁO CÁO & THỐNG KÊ", new String[]{
                    "1. Doanh thu hôm nay",
                    "2. Theo khoảng thời gian",
                    "3. Thống kê tổng quát",
                    "0. Quay lại"
            }, 4);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: showDailyReport(); break;
                case 2: showRangeReport(); break;
                case 3: showOverallStats(); break;
                case 0: return;
                default: ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void showDailyReport() {
        ConsoleFormatter.printSubHeader("BÁO CÁO DOANH THU HÔM NAY");

        String today = java.time.LocalDate.now().toString();
        Map<String, Object> report = reportService.getDailyReport(today);

        System.out.println("  Ngày: " + ConsoleFormatter.formatDate(java.time.LocalDate.now()));
        System.out.println();
        ConsoleFormatter.printReportLine("Số lượt đặt máy hoàn thành",
                String.valueOf(report.getOrDefault("totalBookings", 0)));
        ConsoleFormatter.printReportLine("Doanh thu đặt máy",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("bookingRevenue", 0.0)));
        System.out.println();
        ConsoleFormatter.printReportLine("Số đơn hàng hoàn thành",
                String.valueOf(report.getOrDefault("totalOrders", 0)));
        ConsoleFormatter.printReportLine("Doanh thu F&B",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("orderRevenue", 0.0)));
        System.out.println();
        ConsoleFormatter.printReportLine("Tổng nạp tiền trong ngày",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("totalDeposit", 0.0)));
        System.out.println("  --------------------------------------------");
        ConsoleFormatter.printReportLine("TỔNG DOANH THU",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("totalRevenue", 0.0)));
    }

    private void showRangeReport() {
        ConsoleFormatter.printSubHeader("BÁO CÁO THEO KHOẢNG THỜI GIAN");

        String fromDate = InputValidator.readNonEmptyString(scanner, "Từ ngày (yyyy-MM-dd): ");
        String toDate = InputValidator.readNonEmptyString(scanner, "Đến ngày (yyyy-MM-dd): ");

        Map<String, Object> report = reportService.getRevenueReport(fromDate, toDate);

        System.out.println();
        System.out.println("  Kỳ báo cáo: " + fromDate + " -> " + toDate);
        System.out.println();
        ConsoleFormatter.printReportLine("Số lượt đặt máy hoàn thành",
                String.valueOf(report.getOrDefault("totalBookings", 0)));
        ConsoleFormatter.printReportLine("Doanh thu đặt máy",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("bookingRevenue", 0.0)));
        System.out.println();
        ConsoleFormatter.printReportLine("Số đơn hàng hoàn thành",
                String.valueOf(report.getOrDefault("totalOrders", 0)));
        ConsoleFormatter.printReportLine("Doanh thu F&B",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("orderRevenue", 0.0)));
        System.out.println();
        ConsoleFormatter.printReportLine("Tổng nạp tiền",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("totalDeposit", 0.0)));
        System.out.println("  --------------------------------------------");
        ConsoleFormatter.printReportLine("TỔNG DOANH THU",
                ConsoleFormatter.formatCurrency((double) report.getOrDefault("totalRevenue", 0.0)));
    }

    private void showOverallStats() {
        ConsoleFormatter.printSubHeader("THỐNG KÊ TỔNG QUÁT");

        Map<String, Object> stats = reportService.getOverallStats();

        System.out.println("  --- Người dùng ---");
        ConsoleFormatter.printReportLine("Quản trị viên",
                String.valueOf(stats.getOrDefault("users_admin", 0)));
        ConsoleFormatter.printReportLine("Nhân viên",
                String.valueOf(stats.getOrDefault("users_staff", 0)));
        ConsoleFormatter.printReportLine("Khách hàng",
                String.valueOf(stats.getOrDefault("users_customer", 0)));

        System.out.println();
        System.out.println("  --- Máy trạm ---");
        ConsoleFormatter.printReportLine("Có sẵn",
                String.valueOf(stats.getOrDefault("pcs_available", 0)));
        ConsoleFormatter.printReportLine("Đang thuê",
                String.valueOf(stats.getOrDefault("pcs_in_use", 0)));
        ConsoleFormatter.printReportLine("Bảo trì",
                String.valueOf(stats.getOrDefault("pcs_maintenance", 0)));

        System.out.println();
        System.out.println("  --- Đặt máy ---");
        ConsoleFormatter.printReportLine("Tổng số booking",
                String.valueOf(stats.getOrDefault("bookings_total", 0)));
        ConsoleFormatter.printReportLine("Đã hoàn thành",
                String.valueOf(stats.getOrDefault("bookings_completed", 0)));
        ConsoleFormatter.printReportLine("Doanh thu đặt máy",
                ConsoleFormatter.formatCurrency((double) stats.getOrDefault("bookings_revenue", 0.0)));

        System.out.println();
        System.out.println("  --- Đơn hàng F&B ---");
        ConsoleFormatter.printReportLine("Tổng số đơn",
                String.valueOf(stats.getOrDefault("orders_total", 0)));
        ConsoleFormatter.printReportLine("Đã hoàn thành",
                String.valueOf(stats.getOrDefault("orders_completed", 0)));
        ConsoleFormatter.printReportLine("Doanh thu F&B",
                ConsoleFormatter.formatCurrency((double) stats.getOrDefault("orders_revenue", 0.0)));

        String topFoods = (String) stats.getOrDefault("topFoods", "");
        if (!topFoods.isEmpty()) {
            System.out.println();
            System.out.println("  --- Top món bán chạy ---");
            System.out.print(topFoods);
        }

        double totalRev = (double) stats.getOrDefault("bookings_revenue", 0.0)
                + (double) stats.getOrDefault("orders_revenue", 0.0);
        System.out.println();
        System.out.println("  ============================================");
        ConsoleFormatter.printReportLine("TỔNG DOANH THU HỆ THỐNG",
                ConsoleFormatter.formatCurrency(totalRev));
    }
}

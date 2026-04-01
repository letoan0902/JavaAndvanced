package org.example.presentation;

import org.example.model.*;
import org.example.service.*;
import org.example.util.ConsoleFormatter;
import org.example.util.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private final Scanner scanner;
    private final User currentUser;
    private final IBookingService bookingService;
    private final IOrderService orderService;
    private final ITransactionService transactionService;

    public CustomerMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.bookingService = new BookingService();
        this.orderService = new OrderService();
        this.transactionService = new TransactionService();
    }

    public void show() {
        while (true) {
            System.out.println("  Xin chào, " + currentUser.getFullName()
                    + " | Số dư: " + ConsoleFormatter.formatCurrency(transactionService.getBalance(currentUser.getId())));
            ConsoleFormatter.printGridMenu("MENU KHÁCH HÀNG", new String[]{
                    "1. Đặt máy trạm",
                    "2. Đặt đồ ăn / thức uống",
                    "3. Lịch sử đặt máy",
                    "4. Lịch sử đơn hàng F&B",
                    "5. Hủy đặt máy",
                    "6. Hủy đơn hàng F&B",
                    "7. Nạp tiền vào ví",
                    "8. Lịch sử giao dịch",
                    "0. Đăng xuất"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: handleBookPC(); break;
                case 2: handleOrderFood(); break;
                case 3: viewBookingHistory(); break;
                case 4: viewOrderHistory(); break;
                case 5: handleCancelBooking(); break;
                case 6: handleCancelOrder(); break;
                case 7: handleDeposit(); break;
                case 8: viewTransactionHistory(); break;
                case 0:
                    ConsoleFormatter.printInfo("Đã đăng xuất.");
                    return;
                default:
                    ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ========== ĐẶT MÁY TRẠM ==========

    private void handleBookPC() {
        ConsoleFormatter.printSubHeader("ĐẶT MÁY TRẠM");

        List<Category> categories = bookingService.getAllCategories();
        System.out.println("Các khu vực:");
        for (Category cat : categories) {
            System.out.println("  " + cat.getId() + ". " + cat.getName() + " - " + cat.getDescription());
        }
        System.out.println();

        List<PC> availablePCs = bookingService.getAvailablePCs();
        if (availablePCs.isEmpty()) {
            ConsoleFormatter.printWarning("Hiện tại không có máy trạm nào trống!");
            return;
        }

        int[] widths = {4, 8, 12, 30, 15};
        String[] headers = {"ID", "Số máy", "Khu vực", "Cấu hình", "Giá/giờ"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (PC pc : availablePCs) {
            String[] row = {
                    String.valueOf(pc.getId()),
                    pc.getPcNumber(),
                    pc.getCategoryName(),
                    pc.getConfigInfo(),
                    ConsoleFormatter.formatCurrency(pc.getPricePerHour())
            };
            ConsoleFormatter.printTableRow(row, widths);
        }
        ConsoleFormatter.printTableBottom(widths);
        System.out.println();

        int pcId = InputValidator.readPositiveInt(scanner, "Chọn ID máy trạm: ");
        System.out.println("Nhập thời gian bắt đầu (định dạng: dd-MM-yyyy HH:mm)");
        String startTime = InputValidator.readNonEmptyString(scanner, "Thời gian bắt đầu: ");
        int minutes = InputValidator.readPositiveInt(scanner, "Số phút thuê: ");

        System.out.println("Số dư hiện tại: " + ConsoleFormatter.formatCurrency(
                transactionService.getBalance(currentUser.getId())));

        String result = bookingService.bookPC(currentUser.getId(), pcId, startTime, minutes);

        if (result.contains("thành công")) {
            ConsoleFormatter.printSuccess(result);
        } else {
            ConsoleFormatter.printError(result);
        }
    }

    // ========== ĐẶT ĐỒ ĂN / THỨC UỐNG ==========

    private void handleOrderFood() {
        ConsoleFormatter.printSubHeader("ĐẶT ĐỒ ĂN / THỨC UỐNG");

        List<Food> foods = orderService.getAvailableFoods();
        if (foods.isEmpty()) {
            ConsoleFormatter.printWarning("Menu hiện tại không có món nào!");
            return;
        }

        int[] widths = {4, 25, 15, 8};
        String[] headers = {"ID", "Tên món", "Giá", "Còn lại"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Food food : foods) {
            String[] row = {
                    String.valueOf(food.getId()),
                    food.getName(),
                    ConsoleFormatter.formatCurrency(food.getPrice()),
                    String.valueOf(food.getStockQuantity())
            };
            ConsoleFormatter.printTableRow(row, widths);
        }
        ConsoleFormatter.printTableBottom(widths);
        System.out.println();

        List<OrderDetail> items = new ArrayList<>();
        while (true) {
            int foodId = InputValidator.readInt(scanner, "Nhập ID món (0 để kết thúc): ");
            if (foodId == 0) break;

            Food selectedFood = null;
            for (Food f : foods) {
                if (f.getId() == foodId) {
                    selectedFood = f;
                    break;
                }
            }

            if (selectedFood == null) {
                ConsoleFormatter.printError("Không tìm thấy món với ID = " + foodId);
                continue;
            }

            int quantity = InputValidator.readPositiveInt(scanner, "Số lượng: ");

            OrderDetail detail = new OrderDetail(
                    selectedFood.getId(),
                    selectedFood.getName(),
                    quantity,
                    selectedFood.getPrice()
            );
            items.add(detail);
            System.out.println("  + " + selectedFood.getName() + " x" + quantity
                    + " = " + ConsoleFormatter.formatCurrency(detail.getSubtotal()));
        }

        if (items.isEmpty()) {
            ConsoleFormatter.printInfo("Không có món nào được chọn.");
            return;
        }

        double total = items.stream().mapToDouble(OrderDetail::getSubtotal).sum();
        System.out.println();
        System.out.println("Tổng tiền: " + ConsoleFormatter.formatCurrency(total));

        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận đặt đơn?");
        if (!confirm) {
            ConsoleFormatter.printInfo("Đã hủy đặt đơn.");
            return;
        }

        String result = orderService.createOrder(currentUser.getId(), null, items);

        if (result.contains("thành công")) {
            ConsoleFormatter.printSuccess(result);
        } else {
            ConsoleFormatter.printError(result);
        }
    }

    // ========== XEM LỊCH SỬ ==========

    private void viewBookingHistory() {
        ConsoleFormatter.printSubHeader("LỊCH SỬ ĐẶT MÁY");

        List<Booking> bookings = bookingService.getBookingsByUser(currentUser.getId());
        if (bookings.isEmpty()) {
            ConsoleFormatter.printInfo("Bạn chưa có lượt đặt máy nào.");
            return;
        }

        int[] widths = {4, 8, 12, 16, 16, 15, 14};
        String[] headers = {"ID", "Máy", "Khu vực", "Bắt đầu", "Kết thúc", "Tổng tiền", "Trạng thái"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Booking b : bookings) {
            String start = b.getStartTime() != null ? b.getStartTime().toString().replace("T", " ") : "";
            String end = b.getEndTime() != null ? b.getEndTime().toString().replace("T", " ") : "";
            String[] row = {
                    String.valueOf(b.getId()),
                    b.getPcNumber(),
                    b.getCategoryName(),
                    start.length() > 16 ? start.substring(0, 16) : start,
                    end.length() > 16 ? end.substring(0, 16) : end,
                    ConsoleFormatter.formatCurrency(b.getTotalPrice()),
                    ConsoleFormatter.translateBookingStatus(b.getStatus())
            };
            ConsoleFormatter.printTableRow(row, widths);
        }
        ConsoleFormatter.printTableBottom(widths);
    }

    private void viewOrderHistory() {
        ConsoleFormatter.printSubHeader("LỊCH SỬ ĐƠN HÀNG F&B");

        List<Order> orders = orderService.getOrdersByUser(currentUser.getId());
        if (orders.isEmpty()) {
            ConsoleFormatter.printInfo("Bạn chưa có đơn hàng nào.");
            return;
        }

        for (Order order : orders) {
            System.out.println("--- Đơn hàng #" + order.getId()
                    + " | Trạng thái: " + ConsoleFormatter.translateOrderStatus(order.getStatus())
                    + " | Tổng: " + ConsoleFormatter.formatCurrency(order.getTotalPrice()));

            if (!order.getOrderDetails().isEmpty()) {
                for (OrderDetail d : order.getOrderDetails()) {
                    System.out.printf("    - %s x%d = %s%n",
                            d.getFoodName(), d.getQuantity(),
                            ConsoleFormatter.formatCurrency(d.getSubtotal()));
                }
            }
            System.out.println();
        }
    }

    // ========== HỦY ==========

    private void handleCancelBooking() {
        ConsoleFormatter.printSubHeader("HỦY ĐẶT MÁY");
        viewBookingHistory();
        int bookingId = InputValidator.readPositiveInt(scanner, "Nhập ID booking cần hủy: ");

        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận hủy booking #" + bookingId + "?");
        if (!confirm) {
            ConsoleFormatter.printInfo("Đã hủy thao tác.");
            return;
        }

        if (bookingService.cancelBooking(bookingId, currentUser.getId())) {
            ConsoleFormatter.printSuccess("Đã hủy booking #" + bookingId + " thành công!");
        } else {
            ConsoleFormatter.printError("Không thể hủy! Booking không tồn tại, không phải của bạn, hoặc đã được xử lý.");
        }
    }

    private void handleCancelOrder() {
        ConsoleFormatter.printSubHeader("HỦY ĐƠN HÀNG F&B");
        viewOrderHistory();
        int orderId = InputValidator.readPositiveInt(scanner, "Nhập ID đơn hàng cần hủy: ");

        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận hủy đơn #" + orderId + "?");
        if (!confirm) {
            ConsoleFormatter.printInfo("Đã hủy thao tác.");
            return;
        }

        if (orderService.cancelOrder(orderId, currentUser.getId())) {
            ConsoleFormatter.printSuccess("Đã hủy đơn hàng #" + orderId + " thành công! Tồn kho đã được hoàn lại.");
        } else {
            ConsoleFormatter.printError("Không thể hủy! Đơn không tồn tại, không phải của bạn, hoặc đã được xử lý.");
        }
    }

    // ========== VÍ ĐIỆN TỬ ==========

    private void handleDeposit() {
        ConsoleFormatter.printSubHeader("NẠP TIỀN VÀO VÍ");

        System.out.println("Số dư hiện tại: " + ConsoleFormatter.formatCurrency(
                transactionService.getBalance(currentUser.getId())));
        System.out.println();

        double amount = InputValidator.readPositiveDouble(scanner, "Số tiền muốn nạp (VND): ");

        boolean confirm = InputValidator.readConfirmation(scanner,
                "Xác nhận nạp " + ConsoleFormatter.formatCurrency(amount) + "?");
        if (!confirm) {
            ConsoleFormatter.printInfo("Đã hủy nạp tiền.");
            return;
        }

        String result = transactionService.deposit(currentUser.getId(), amount);
        if (result.contains("thành công")) {
            ConsoleFormatter.printSuccess(result);
        } else {
            ConsoleFormatter.printError(result);
        }
    }

    private void viewTransactionHistory() {
        ConsoleFormatter.printSubHeader("LỊCH SỬ GIAO DỊCH");

        System.out.println("Số dư hiện tại: " + ConsoleFormatter.formatCurrency(
                transactionService.getBalance(currentUser.getId())));
        System.out.println();

        List<Transaction> transactions = transactionService.getTransactionsByUser(currentUser.getId());
        if (transactions.isEmpty()) {
            ConsoleFormatter.printInfo("Bạn chưa có giao dịch nào.");
            return;
        }

        int[] widths = {4, 18, 15, 15, 30};
        String[] headers = {"ID", "Loại", "Số tiền", "Số dư sau", "Mô tả"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Transaction t : transactions) {
            String[] row = {
                    String.valueOf(t.getId()),
                    ConsoleFormatter.translateTransactionType(t.getType()),
                    ConsoleFormatter.formatCurrency(t.getAmount()),
                    ConsoleFormatter.formatCurrency(t.getBalanceAfter()),
                    t.getDescription() != null ? t.getDescription() : ""
            };
            ConsoleFormatter.printTableRow(row, widths);
        }
        ConsoleFormatter.printTableBottom(widths);
    }
}

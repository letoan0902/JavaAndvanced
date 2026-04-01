package org.example.presentation;

import org.example.model.*;
import org.example.service.*;
import org.example.util.ConsoleFormatter;
import org.example.util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class StaffMenu {

    private final Scanner scanner;
    private final User currentUser;
    private final IBookingService bookingService;
    private final IOrderService orderService;
    private final ITransactionService transactionService;

    public StaffMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.bookingService = new BookingService();
        this.orderService = new OrderService();
        this.transactionService = new TransactionService();
    }

    public void show() {
        while (true) {
            System.out.println("  Xin chào, " + currentUser.getFullName());
            ConsoleFormatter.printGridMenu("MENU NHÂN VIÊN", new String[]{
                    "1. Quản lý đặt máy",
                    "2. Quản lý đơn hàng F&B",
                    "3. Lịch sử giao dịch",
                    "0. Đăng xuất"
            }, 4);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: showBookingManagement(); break;
                case 2: showOrderManagement(); break;
                case 3: viewAllTransactions(); break;
                case 0:
                    ConsoleFormatter.printInfo("Đã đăng xuất.");
                    return;
                default:
                    ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    // ========== QUẢN LÝ BOOKING ==========

    private void showBookingManagement() {
        while (true) {
            ConsoleFormatter.printGridMenu("QUẢN LÝ ĐẶT MÁY", new String[]{
                    "1. Xem tất cả booking",
                    "2. Xác nhận",
                    "3. Bắt đầu",
                    "4. Hoàn thành",
                    "5. Hủy booking",
                    "0. Quay lại"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: displayAllBookings(); break;
                case 2: updateBookingStatus("PENDING", "CONFIRMED"); break;
                case 3: updateBookingStatus("CONFIRMED", "IN_USE"); break;
                case 4: updateBookingStatus("IN_USE", "COMPLETED"); break;
                case 5: updateBookingStatus("PENDING", "CANCELLED"); break;
                case 0: return;
                default: ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            ConsoleFormatter.printInfo("Chưa có booking nào.");
            return;
        }

        ConsoleFormatter.printSubHeader("TẤT CẢ BOOKING");

        int[] widths = {4, 15, 8, 12, 16, 16, 12, 14};
        String[] headers = {"ID", "Khách hàng", "Máy", "Khu vực", "Bắt đầu", "Kết thúc", "Tổng tiền", "Trạng thái"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Booking b : bookings) {
            String start = b.getStartTime() != null ? b.getStartTime().toString().replace("T", " ") : "";
            String end = b.getEndTime() != null ? b.getEndTime().toString().replace("T", " ") : "";
            String[] row = {
                    String.valueOf(b.getId()),
                    b.getCustomerName(),
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

    private void updateBookingStatus(String fromStatus, String toStatus) {
        displayAllBookings();
        System.out.println();
        System.out.println("Chỉ cập nhật booking có trạng thái: "
                + ConsoleFormatter.translateBookingStatus(fromStatus) + " -> "
                + ConsoleFormatter.translateBookingStatus(toStatus));
        int bookingId = InputValidator.readPositiveInt(scanner, "Nhập ID booking: ");

        List<Booking> all = bookingService.getAllBookings();
        Booking target = null;
        for (Booking b : all) {
            if (b.getId() == bookingId) {
                target = b;
                break;
            }
        }

        if (target == null) {
            ConsoleFormatter.printError("Không tìm thấy booking #" + bookingId);
            return;
        }

        if (!target.getStatus().equals(fromStatus)) {
            ConsoleFormatter.printError("Booking #" + bookingId + " đang ở trạng thái "
                    + ConsoleFormatter.translateBookingStatus(target.getStatus())
                    + ", không thể chuyển sang "
                    + ConsoleFormatter.translateBookingStatus(toStatus));
            return;
        }

        if (toStatus.equals("CANCELLED")) {
            boolean success = bookingService.cancelBooking(bookingId, target.getUserId());
            if (success) {
                ConsoleFormatter.printSuccess("Đã hủy booking #" + bookingId);
            } else {
                ConsoleFormatter.printError("Hủy booking thất bại!");
            }
        } else {
            // Khi xác nhận (PENDING -> CONFIRMED): trừ tiền khách
            if (fromStatus.equals("PENDING") && toStatus.equals("CONFIRMED")) {
                String payResult = transactionService.payBooking(
                        target.getUserId(), target.getTotalPrice(),
                        "Thanh toán đặt máy #" + bookingId);
                if (!payResult.contains("thành công")) {
                    ConsoleFormatter.printError("Không thể trừ tiền khách: " + payResult);
                    return;
                }
                ConsoleFormatter.printInfo(payResult);
            }

            boolean success = updateBookingStatusDirect(bookingId, toStatus);
            if (success) {
                ConsoleFormatter.printSuccess("Đã cập nhật booking #" + bookingId + " -> "
                        + ConsoleFormatter.translateBookingStatus(toStatus));
            } else {
                ConsoleFormatter.printError("Cập nhật thất bại!");
            }
        }
    }

    private boolean updateBookingStatusDirect(int bookingId, String status) {
        org.example.dao.BookingDAO dao = new org.example.dao.impl.BookingDAOImpl();
        return dao.updateStatus(bookingId, status);
    }

    // ========== QUẢN LÝ ĐƠN HÀNG F&B ==========

    private void showOrderManagement() {
        while (true) {
            ConsoleFormatter.printGridMenu("QUẢN LÝ ĐƠN HÀNG F&B", new String[]{
                    "1. Xem tất cả đơn hàng",
                    "2. Chuẩn bị",
                    "3. Phục vụ",
                    "4. Hoàn thành",
                    "5. Hủy đơn hàng",
                    "0. Quay lại"
            }, 3);

            int choice = InputValidator.readInt(scanner, "Lựa chọn: ");

            switch (choice) {
                case 1: displayAllOrders(); break;
                case 2: updateOrderStatus("PENDING", "PREPARING"); break;
                case 3: updateOrderStatus("PREPARING", "SERVED"); break;
                case 4: updateOrderStatus("SERVED", "COMPLETED"); break;
                case 5: handleCancelOrderByStaff(); break;
                case 0: return;
                default: ConsoleFormatter.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            ConsoleFormatter.printInfo("Chưa có đơn hàng nào.");
            return;
        }

        ConsoleFormatter.printSubHeader("TẤT CẢ ĐƠN HÀNG F&B");

        for (Order order : orders) {
            System.out.println("--- Đơn #" + order.getId()
                    + " | KH: " + order.getCustomerName()
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

    private void updateOrderStatus(String fromStatus, String toStatus) {
        displayAllOrders();
        System.out.println("Chỉ cập nhật đơn có trạng thái: "
                + ConsoleFormatter.translateOrderStatus(fromStatus) + " -> "
                + ConsoleFormatter.translateOrderStatus(toStatus));
        int orderId = InputValidator.readPositiveInt(scanner, "Nhập ID đơn hàng: ");

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ConsoleFormatter.printError("Không tìm thấy đơn hàng #" + orderId);
            return;
        }

        if (!order.getStatus().equals(fromStatus)) {
            ConsoleFormatter.printError("Đơn #" + orderId + " đang ở trạng thái "
                    + ConsoleFormatter.translateOrderStatus(order.getStatus())
                    + ", không thể chuyển sang "
                    + ConsoleFormatter.translateOrderStatus(toStatus));
            return;
        }

        // Khi chuẩn bị (PENDING -> PREPARING): trừ tiền khách
        if (fromStatus.equals("PENDING") && toStatus.equals("PREPARING")) {
            String payResult = transactionService.payOrder(
                    order.getUserId(), order.getTotalPrice(),
                    "Thanh toán đơn hàng F&B #" + orderId);
            if (!payResult.contains("thành công")) {
                ConsoleFormatter.printError("Không thể trừ tiền khách: " + payResult);
                return;
            }
            ConsoleFormatter.printInfo(payResult);
        }

        org.example.dao.OrderDAO dao = new org.example.dao.impl.OrderDAOImpl();
        if (dao.updateStatus(orderId, toStatus)) {
            ConsoleFormatter.printSuccess("Đã cập nhật đơn #" + orderId + " -> "
                    + ConsoleFormatter.translateOrderStatus(toStatus));
        } else {
            ConsoleFormatter.printError("Cập nhật thất bại!");
        }
    }

    private void handleCancelOrderByStaff() {
        displayAllOrders();
        int orderId = InputValidator.readPositiveInt(scanner, "Nhập ID đơn hàng cần hủy: ");

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ConsoleFormatter.printError("Không tìm thấy đơn hàng #" + orderId);
            return;
        }

        if (order.getStatus().equals("COMPLETED") || order.getStatus().equals("CANCELLED")) {
            ConsoleFormatter.printError("Đơn #" + orderId + " đã "
                    + ConsoleFormatter.translateOrderStatus(order.getStatus()) + ", không thể hủy!");
            return;
        }

        boolean confirm = InputValidator.readConfirmation(scanner, "Xác nhận hủy đơn #" + orderId + "?");
        if (!confirm) {
            ConsoleFormatter.printInfo("Đã hủy thao tác.");
            return;
        }

        if (orderService.cancelOrder(orderId, order.getUserId())) {
            ConsoleFormatter.printSuccess("Đã hủy đơn #" + orderId + " và hoàn tồn kho!");
        } else {
            ConsoleFormatter.printError("Hủy đơn thất bại!");
        }
    }

    // ========== LỊCH SỬ GIAO DỊCH ==========

    private void viewAllTransactions() {
        ConsoleFormatter.printSubHeader("LỊCH SỬ GIAO DỊCH");

        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            ConsoleFormatter.printInfo("Chưa có giao dịch nào.");
            return;
        }

        int[] widths = {4, 15, 18, 15, 15, 30};
        String[] headers = {"ID", "Khách hàng", "Loại", "Số tiền", "Số dư sau", "Mô tả"};

        ConsoleFormatter.printTableTop(widths);
        ConsoleFormatter.printTableRow(headers, widths);
        ConsoleFormatter.printTableSeparator(widths);

        for (Transaction t : transactions) {
            String[] row = {
                    String.valueOf(t.getId()),
                    t.getCustomerName(),
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

package org.example.util;

/**
 * ConsoleFormatter - Dinh dang hien thi Console cho chuyen nghiep
 *
 * Su dung ky tu ASCII thuan de tuong thich voi tat ca console Windows/IntelliJ
 */
public class ConsoleFormatter {


    // ===================================================
    //  HEADER & SUB HEADER
    // ===================================================

    // In tieu de lon (header) dang grid
    public static void printHeader(String title) {
        int width = 60;
        String line = repeat('=', width);
        System.out.println();
        System.out.println("+" + line + "+");
        int leftPad = (width - title.length()) / 2;
        int rightPad = width - title.length() - leftPad;
        System.out.println("|" + repeat(' ', leftPad) + title + repeat(' ', rightPad) + "|");
        System.out.println("+" + line + "+");
        System.out.println();
    }

    // In tieu de phu (sub header)
    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println("--- " + title + " ---");
        System.out.println();
    }

    // ===================================================
    //  GRID MENU - Menu dang luoi dep nhu vi du
    // ===================================================

    /**
     * In menu dang luoi (grid) voi tieu de
     * Chia cac option thanh hang, moi hang co itemsPerRow o
     *
     * @param title       Tieu de menu
     * @param options     Mang cac lua chon (vi du: "1. Dang nhap", "2. Dang ky", "0. Thoat")
     * @param itemsPerRow So option tren moi hang
     */
    public static void printGridMenu(String title, String[] options, int itemsPerRow) {
        if (options == null || options.length == 0) return;

        // Tinh do rong moi o
        int cellWidth = 30;
        int totalWidth = cellWidth * itemsPerRow + itemsPerRow + 1;

        // --- Dong tieu de ---
        // Tinh lai so ky tu '=' cho phan tieu de
        int titlePart = title.length() + 2; // khoang trong 2 ben
        int sideLen = (totalWidth - 2 - titlePart) / 2;
        int sideLen2 = totalWidth - 2 - titlePart - sideLen;

        System.out.println("+" + repeat('=', sideLen) + " " + title + " " + repeat('=', sideLen2) + "+");

        // --- Cac hang option ---
        int totalOptions = options.length;
        int row = 0;
        while (row * itemsPerRow < totalOptions) {
            int startIdx = row * itemsPerRow;
            int endIdx = Math.min(startIdx + itemsPerRow, totalOptions);
            int colsInRow = endIdx - startIdx;

            // Tinh do rong cho moi cot trong hang nay
            int[] colWidths = new int[colsInRow];
            int remaining = totalWidth - colsInRow - 1;
            int baseW = remaining / colsInRow;
            int extra = remaining % colsInRow;
            for (int i = 0; i < colsInRow; i++) {
                colWidths[i] = baseW + (i < extra ? 1 : 0);
            }

            // In duong phan cach tren
            StringBuilder sep = new StringBuilder("+");
            for (int i = 0; i < colsInRow; i++) {
                sep.append(repeat('-', colWidths[i]));
                sep.append('+');
            }
            System.out.println(sep);

            // In dong trong tren
            StringBuilder emptyRow = new StringBuilder("|");
            for (int i = 0; i < colsInRow; i++) {
                emptyRow.append(repeat(' ', colWidths[i]));
                emptyRow.append('|');
            }
            System.out.println(emptyRow);

            // In dong option (can giua)
            StringBuilder optRow = new StringBuilder("|");
            for (int i = 0; i < colsInRow; i++) {
                String opt = options[startIdx + i];
                int padLeft = (colWidths[i] - opt.length()) / 2;
                int padRight = colWidths[i] - opt.length() - padLeft;
                if (padLeft < 0) {
                    // Truncate neu qua dai
                    opt = opt.substring(0, colWidths[i] - 2) + "..";
                    padLeft = 0;
                    padRight = 0;
                }
                optRow.append(repeat(' ', padLeft)).append(opt).append(repeat(' ', padRight));
                optRow.append('|');
            }
            System.out.println(optRow);

            // In dong trong duoi
            System.out.println(emptyRow);

            row++;
        }

        // In duong dong bang cuoi
        int colsLastRow = Math.min(itemsPerRow, totalOptions - (row - 1) * itemsPerRow);
        int remaining = totalWidth - colsLastRow - 1;
        int baseW = remaining / colsLastRow;
        int extra2 = remaining % colsLastRow;
        StringBuilder bottom = new StringBuilder("+");
        for (int i = 0; i < colsLastRow; i++) {
            int w = baseW + (i < extra2 ? 1 : 0);
            bottom.append(repeat('=', w));
            bottom.append('+');
        }
        System.out.println(bottom);
        System.out.println();
    }

    // ===================================================
    //  TABLE - Bang du lieu
    // ===================================================

    // In duong phan cach bang voi cac cot
    public static void printTableSeparator(int[] columnWidths) {
        System.out.println(buildTableLine(columnWidths, '+', '-'));
    }

    // In dong tren cung cua bang
    public static void printTableTop(int[] columnWidths) {
        System.out.println(buildTableLine(columnWidths, '+', '-'));
    }

    // In dong duoi cung cua bang
    public static void printTableBottom(int[] columnWidths) {
        System.out.println(buildTableLine(columnWidths, '+', '-'));
    }

    // Build 1 dong vien bang
    private static String buildTableLine(int[] columnWidths, char corner, char fill) {
        StringBuilder sb = new StringBuilder();
        sb.append(corner);
        for (int i = 0; i < columnWidths.length; i++) {
            sb.append(repeat(fill, columnWidths[i] + 2));
            sb.append(corner);
        }
        return sb.toString();
    }

    /**
     * In 1 dong du lieu trong bang
     *
     * @param values       Gia tri tung cot
     * @param columnWidths Do rong tung cot
     */
    public static void printTableRow(String[] values, int[] columnWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append('|');
        for (int i = 0; i < values.length && i < columnWidths.length; i++) {
            String val = values[i] != null ? values[i] : "";
            // Truncate neu qua dai
            if (val.length() > columnWidths[i]) {
                val = val.substring(0, columnWidths[i] - 2) + "..";
            }
            sb.append(String.format(" %-" + columnWidths[i] + "s ", val));
            sb.append('|');
        }
        System.out.println(sb);
    }

    // ===================================================
    //  DUONG PHAN CACH
    // ===================================================

    public static void printSeparator(int width) {
        System.out.println(repeat('-', width));
    }

    // ===================================================
    //  THONG BAO
    // ===================================================

    public static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    public static void printError(String message) {
        System.out.println("[LỖI] " + message);
    }

    public static void printWarning(String message) {
        System.out.println("[CẢNH BÁO] " + message);
    }

    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    // ===================================================
    //  FORMAT TIEN TE & NGAY GIO
    // ===================================================

    // Format so tien VND (khong dung ky tu Unicode)
    public static String formatCurrency(double amount) {
        return String.format("%,.0f VND", amount);
    }

    // Format ngay thang
    public static String formatDate(java.time.LocalDate date) {
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Format ngay gio
    public static String formatDateTime(java.time.LocalDateTime dateTime) {
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // In bao cao doanh thu dep
    public static void printReportLine(String label, String value) {
        System.out.printf("  %-30s : %s%n", label, value);
    }

    // ===================================================
    //  UTILITY
    // ===================================================

    // Lap lai ky tu n lan (thay cho String.repeat de tuong thich)
    // Lap lai ky tu n lan
    public static String repeat(char ch, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    // ===================================================
    //  DICH TRANG THAI SANG TIENG VIET
    // ===================================================

    // Trang thai may tram
    public static String translatePCStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "AVAILABLE": return "Có sẵn";
            case "IN_USE": return "Đang thuê";
            case "MAINTENANCE": return "Bảo trì";
            default: return status;
        }
    }

    // Trang thai booking
    public static String translateBookingStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "IN_USE": return "Đang sử dụng";
            case "COMPLETED": return "Hoàn thành";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }

    // Trang thai don hang
    public static String translateOrderStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "Chờ xử lý";
            case "PREPARING": return "Đang chuẩn bị";
            case "SERVED": return "Đã phục vụ";
            case "COMPLETED": return "Hoàn thành";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }

    // Loai giao dich
    public static String translateTransactionType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DEPOSIT": return "Nạp tiền";
            case "BOOKING_PAYMENT": return "TT đặt máy";
            case "ORDER_PAYMENT": return "TT đơn hàng";
            case "REFUND": return "Hoàn tiền";
            default: return type;
        }
    }
}
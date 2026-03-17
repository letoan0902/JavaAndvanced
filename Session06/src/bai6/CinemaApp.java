package bai6;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CinemaApp {
    private final Scanner scanner = new Scanner(System.in);

    private final Object pauseLock = new Object();
    private final AtomicBoolean paused = new AtomicBoolean(false);

    private volatile CinemaSystem system;

    public void runMenu() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> startSimulation();
                case "2" -> pauseSimulation();
                case "3" -> resumeSimulation();
                case "4" -> addTickets();
                case "5" -> showStats();
                case "6" -> detectDeadlock();
                case "7" -> {
                    stopSimulation();
                    System.out.println("Kết thúc chương trình.");
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1. Bắt đầu mô phỏng");
        System.out.println("2. Tạm dừng mô phỏng");
        System.out.println("3. Tiếp tục mô phỏng");
        System.out.println("4. Thêm vé vào phòng");
        System.out.println("5. Xem thống kê");
        System.out.println("6. Phát hiện deadlock");
        System.out.println("7. Thoát");
        System.out.print("Chọn: ");
    }

    private void startSimulation() {
        if (system != null && system.isRunning()) {
            System.out.println("Mô phỏng đang chạy.");
            return;
        }

        System.out.print("Nhập số phòng: ");
        int rooms = readIntDefault(2);
        System.out.print("Nhập số vé/phòng: ");
        int ticketsPerRoom = readIntDefault(10);
        System.out.print("Nhập số quầy: ");
        int counters = readIntDefault(3);

        List<String> roomNames = new ArrayList<>();
        for (int i = 0; i < rooms; i++) {
            roomNames.add(String.valueOf((char) ('A' + i)));
        }

        system = new CinemaSystem(roomNames, ticketsPerRoom, counters, pauseLock, paused);
        system.start();

        System.out.println("Đã khởi tạo hệ thống với " + rooms + " phòng, " + (rooms * ticketsPerRoom) +
                " vé, " + counters + " quầy");
    }

    private void pauseSimulation() {
        if (system == null || !system.isRunning()) {
            System.out.println("Chưa có mô phỏng nào đang chạy.");
            return;
        }
        paused.set(true);
        System.out.println("Đã tạm dừng tất cả quầy bán vé.");
    }

    private void resumeSimulation() {
        if (system == null || !system.isRunning()) {
            System.out.println("Chưa có mô phỏng nào đang chạy.");
            return;
        }
        paused.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
        System.out.println("Đã tiếp tục hoạt động.");
    }

    private void addTickets() {
        if (system == null) {
            System.out.println("Chưa khởi tạo hệ thống.");
            return;
        }
        System.out.print("Nhập tên phòng (vd: A): ");
        String room = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        System.out.print("Nhập số vé cần thêm: ");
        int count = readIntDefault(1);

        boolean ok = system.addTickets(room, count);
        if (ok) System.out.println("Đã thêm " + count + " vé vào phòng " + room);
        else System.out.println("Không tìm thấy phòng " + room);
    }

    private void showStats() {
        if (system == null) {
            System.out.println("Chưa khởi tạo hệ thống.");
            return;
        }
        System.out.println(system.stats());
    }

    private void detectDeadlock() {
        System.out.println("Đang quét deadlock...");
        String report = DeadlockDetector.detectOnce();
        System.out.println(report);
    }

    private void stopSimulation() {
        if (system != null) {
            System.out.println("Đang dừng hệ thống...");
            system.stop();
            system = null;
        }
    }

    private int readIntDefault(int def) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return def;
        }
    }
}

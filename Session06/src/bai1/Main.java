package bai1;

/**
 * Bài 1: Bán vé combo + minh hoạ deadlock và cách phòng tránh.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== BAI 1 - DEADLOCK DEMO (co the bi treo) ===");

        TicketPool roomA = new TicketPool("A", 2);
        TicketPool roomB = new TicketPool("B", 2);

        // Giai đoạn 1: cố tình tạo deadlock
        BookingCounter counter1 = new BookingCounter("Quầy 1", roomA, roomB, LockOrder.A_THEN_B);
        BookingCounter counter2 = new BookingCounter("Quầy 2", roomA, roomB, LockOrder.B_THEN_A);

        Thread t1 = new Thread(counter1::sellComboOnce, "Counter-1");
        Thread t2 = new Thread(counter2::sellComboOnce, "Counter-2");

        t1.start();
        t2.start();

        // Chờ một chút để quan sát; nếu deadlock xảy ra chương trình sẽ treo ở đây.
        t1.join(3000);
        t2.join(3000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("\n(Chương trình đang treo do DEADLOCK như mong đợi).\n" +
                    "Hãy dừng Run và chạy lại bản FIX ở dưới (lock order đồng nhất).\n");

            // Không join vô hạn nữa, để bạn còn thấy phần FIX khi tắt/chạy lại.
        }

        System.out.println("=== BAI 1 - FIX (lock order nhất quán) ===");
        TicketPool roomA2 = new TicketPool("A", 2);
        TicketPool roomB2 = new TicketPool("B", 2);

        BookingCounter counter1Fixed = new BookingCounter("Quầy 1", roomA2, roomB2, LockOrder.BY_ROOM_NAME);
        BookingCounter counter2Fixed = new BookingCounter("Quầy 2", roomA2, roomB2, LockOrder.BY_ROOM_NAME);

        Thread f1 = new Thread(() -> {
            while (counter1Fixed.sellCombo()) {
                // loop
            }
        }, "Counter-1-Fixed");

        Thread f2 = new Thread(() -> {
            while (counter2Fixed.sellCombo()) {
                // loop
            }
        }, "Counter-2-Fixed");

        f1.start();
        f2.start();
        f1.join();
        f2.join();

        System.out.println("Kết thúc. Vé còn lại phòng A: " + roomA2.remaining() + ", phòng B: " + roomB2.remaining());
    }
}

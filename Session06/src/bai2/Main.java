package bai2;

/**
 * Bài 2: Dùng wait/notifyAll để tránh busy-wait khi hết vé.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool roomA = new TicketPool("A", 3);
        TicketPool roomB = new TicketPool("B", 6);

        BookingCounter c1 = new BookingCounter("Quầy 1", roomA);
        BookingCounter c2 = new BookingCounter("Quầy 2", roomB);

        Thread t1 = new Thread(c1, "Counter-A");
        Thread t2 = new Thread(c2, "Counter-B");

        t1.start();
        t2.start();

        // Sau 3s, thêm vé vào A để đánh thức quầy 1
        Thread supplier = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            roomA.addTickets(3);
            System.out.println("Nhà cung cấp: Đã thêm 3 vé vào phòng A");
        }, "Supplier");
        supplier.start();

        Thread.sleep(7000);

        // Dừng demo
        t1.interrupt();
        t2.interrupt();
        supplier.join();
        t1.join();
        t2.join();

        System.out.println("Kết thúc demo. Vé còn lại A: " + roomA.remaining() + ", B: " + roomB.remaining());
    }
}

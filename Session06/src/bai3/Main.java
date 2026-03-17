package bai3;

/**
 * Bài 3: Thêm TicketSupplier chạy riêng, cứ mỗi interval thêm vé vào mỗi phòng.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool roomA = new TicketPool("A", 10);
        TicketPool roomB = new TicketPool("B", 10);

        BookingCounter c1 = new BookingCounter("Quầy 1", roomA, roomB);
        BookingCounter c2 = new BookingCounter("Quầy 2", roomA, roomB);

        TicketSupplier supplier = new TicketSupplier(roomA, roomB, 3, 3000, 2);

        Thread t1 = new Thread(c1, "Counter-1");
        Thread t2 = new Thread(c2, "Counter-2");
        Thread ts = new Thread(supplier, "Supplier");

        t1.start();
        t2.start();
        ts.start();

        t1.join();
        t2.join();
        ts.join();

        System.out.println("Kết thúc chương trình");
        System.out.println("Quầy 1 bán được: " + c1.soldCount + " vé");
        System.out.println("Quầy 2 bán được: " + c2.soldCount + " vé");
        System.out.println("Vé còn lại phòng A: " + roomA.remaining());
        System.out.println("Vé còn lại phòng B: " + roomB.remaining());
    }
}

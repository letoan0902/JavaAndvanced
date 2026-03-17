package bai5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bài 5: 3 phòng + 5 quầy + vé VIP/Thường + hold 5s + TimeoutManager.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool roomA = new TicketPool("A", 8);
        TicketPool roomB = new TicketPool("B", 6);
        TicketPool roomC = new TicketPool("C", 5);

        List<TicketPool> pools = Arrays.asList(roomA, roomB, roomC);

        TimeoutManager timeoutManager = new TimeoutManager(pools, 1000);
        Thread tmThread = new Thread(timeoutManager, "TimeoutManager");
        tmThread.start();

        List<Thread> counters = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            BookingCounter counter = new BookingCounter("Quầy " + i, pools);
            Thread t = new Thread(counter, "Counter-" + i);
            counters.add(t);
            t.start();
        }

        // Chạy demo 15s rồi dừng
        Thread.sleep(15000);
        for (Thread t : counters) t.interrupt();
        tmThread.interrupt();

        for (Thread t : counters) t.join();
        tmThread.join();

        System.out.println("=== THỐNG KÊ CUỐI ===");
        for (TicketPool p : pools) {
            System.out.println("Phòng " + p.roomName + ": còn " + p.remainingAvailable() + " vé available, " + p.countSold() + " vé sold");
        }
    }
}

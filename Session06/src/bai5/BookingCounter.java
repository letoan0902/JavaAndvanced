package bai5;

import java.util.List;
import java.util.Random;

public class BookingCounter implements Runnable {
    final String counterName;
    final List<TicketPool> pools;
    final Random rnd = new Random();

    BookingCounter(String counterName, List<TicketPool> pools) {
        this.counterName = counterName;
        this.pools = pools;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                boolean wantVIP = rnd.nextInt(100) < 30;
                TicketPool pool = pools.get(rnd.nextInt(pools.size()));

                Ticket held = pool.holdTicket(counterName, wantVIP);
                if (held == null) {
                    Thread.sleep(200);
                    continue;
                }

                System.out.println(counterName + ": Đã giữ vé " + held + ". Vui lòng thanh toán trong 5s");

                // khách suy nghĩ
                Thread.sleep(3000);

                boolean paid = rnd.nextInt(100) < 80; // 80% thanh toán
                if (!paid) {
                    // không thanh toán -> để timeout manager xử lý
                    Thread.sleep(300);
                    continue;
                }

                boolean ok = pool.sellHeldTicket(counterName, held);
                if (ok) {
                    System.out.println(counterName + ": Thanh toán thành công vé " + held.roomName + "-" + held.ticketId);
                } else {
                    // có thể đã hết hạn trong lúc sleep
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

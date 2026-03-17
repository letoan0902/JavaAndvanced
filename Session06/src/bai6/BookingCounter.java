package bai6;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingCounter implements Runnable {
    final String counterName;
    final List<TicketPool> pools;
    final Random rnd = new Random();

    final Object pauseLock;
    final AtomicBoolean paused;

    BookingCounter(String counterName, List<TicketPool> pools, Object pauseLock, AtomicBoolean paused) {
        this.counterName = counterName;
        this.pools = pools;
        this.pauseLock = pauseLock;
        this.paused = paused;
    }

    @Override
    public void run() {
        try {
            System.out.println(counterName + " bắt đầu bán vé...");
            while (!Thread.currentThread().isInterrupted()) {
                if (paused.get()) {
                    synchronized (pauseLock) {
                        while (paused.get()) pauseLock.wait();
                    }
                }

                TicketPool pool = pools.get(rnd.nextInt(pools.size()));
                Ticket t = pool.sellTicket();
                if (t != null) {
                    System.out.println(counterName + " đã bán vé " + t);
                }

                Thread.sleep(150);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

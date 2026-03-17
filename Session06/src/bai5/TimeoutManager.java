package bai5;

import java.util.List;

public class TimeoutManager implements Runnable {
    final List<TicketPool> pools;
    final long checkIntervalMs;

    TimeoutManager(List<TicketPool> pools, long checkIntervalMs) {
        this.pools = pools;
        this.checkIntervalMs = checkIntervalMs;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (TicketPool p : pools) {
                    p.releaseExpiredTickets();
                }
                Thread.sleep(checkIntervalMs);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

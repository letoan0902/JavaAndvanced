package bai2;

public class BookingCounter implements Runnable {
    final String counterName;
    final TicketPool pool;
    int soldCount;

    BookingCounter(String counterName, TicketPool pool) {
        this.counterName = counterName;
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Ticket t = pool.sellTicket(counterName);
                soldCount++;
                System.out.println(counterName + " bán vé " + t);
                Thread.sleep(200);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

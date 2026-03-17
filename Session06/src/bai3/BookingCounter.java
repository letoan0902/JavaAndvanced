package bai3;

import java.util.Random;

public class BookingCounter implements Runnable {
    final String counterName;
    final TicketPool roomA;
    final TicketPool roomB;
    final Random rnd = new Random();
    int soldCount;

    BookingCounter(String counterName, TicketPool roomA, TicketPool roomB) {
        this.counterName = counterName;
        this.roomA = roomA;
        this.roomB = roomB;
    }

    @Override
    public void run() {
        while (true) {
            boolean aEmpty = roomA.remaining() == 0;
            boolean bEmpty = roomB.remaining() == 0;
            if (aEmpty && bEmpty) return;

            TicketPool chosen;
            if (aEmpty) chosen = roomB;
            else if (bEmpty) chosen = roomA;
            else chosen = rnd.nextBoolean() ? roomA : roomB;

            Ticket t = chosen.sellTicket();
            if (t != null) {
                soldCount++;
                System.out.println(counterName + " đã bán vé " + t);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

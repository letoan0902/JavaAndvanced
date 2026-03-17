package bai2;

import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    final String roomName;
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextNo = 1;

    TicketPool(String roomName, int initial) {
        this.roomName = roomName;
        addTickets(initial);
    }

    public synchronized Ticket sellTicket(String counterName) throws InterruptedException {
        while (true) {
            Ticket t = takeOneUnsoldNoSync();
            if (t != null) return t;

            System.out.println(counterName + ": Hết vé phòng " + roomName + ", đang chờ...");
            wait();
        }
    }

    public synchronized void addTickets(int count) {
        for (int i = 0; i < count; i++) {
            String id = String.format("%03d", nextNo++);
            tickets.add(new Ticket(id, roomName));
        }
        notifyAll();
    }

    private Ticket takeOneUnsoldNoSync() {
        for (Ticket t : tickets) {
            if (!t.isSold) {
                t.isSold = true;
                return t;
            }
        }
        return null;
    }

    public synchronized int remaining() {
        int c = 0;
        for (Ticket t : tickets) if (!t.isSold) c++;
        return c;
    }
}

package bai4;

import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    final String roomName;
    private final List<Ticket> tickets = new ArrayList<>();

    TicketPool(String roomName, int capacity) {
        this.roomName = roomName;
        for (int i = 1; i <= capacity; i++) {
            tickets.add(new Ticket(String.format("%03d", i), roomName));
        }
    }

    public synchronized Ticket sellTicket() {
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

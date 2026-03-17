package bai3;

import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    final String roomName;
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextNo = 1;

    TicketPool(String roomName, int initialCount) {
        this.roomName = roomName;
        addTickets(initialCount);
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

    public synchronized void addTickets(int count) {
        for (int i = 0; i < count; i++) {
            String id = String.format("%03d", nextNo++);
            tickets.add(new Ticket(id, roomName));
        }
    }

    public synchronized int remaining() {
        int c = 0;
        for (Ticket t : tickets) if (!t.isSold) c++;
        return c;
    }
}

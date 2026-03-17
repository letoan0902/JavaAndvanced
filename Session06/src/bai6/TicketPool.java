package bai6;

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

    synchronized void addTickets(int count) {
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(String.format("%03d", nextNo++), roomName));
        }
    }

    synchronized Ticket sellTicket() {
        for (Ticket t : tickets) {
            if (!t.isSold) {
                t.isSold = true;
                return t;
            }
        }
        return null;
    }

    synchronized int countSold() {
        int c = 0;
        for (Ticket t : tickets) if (t.isSold) c++;
        return c;
    }

    synchronized int totalTickets() {
        return tickets.size();
    }
}

package bai5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketPool {
    final String roomName;
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextNo = 1;

    TicketPool(String roomName, int capacity) {
        this.roomName = roomName;
        for (int i = 0; i < capacity; i++) {
            // demo: 20% VIP
            boolean vip = (i % 5 == 0);
            tickets.add(new Ticket(String.format("%03d", nextNo++), roomName, vip));
        }
    }

    public synchronized Ticket holdTicket(String counterName, boolean wantVIP) {
        Ticket candidate = null;
        for (Ticket t : tickets) {
            if (t.isSold) continue;
            if (t.isHeld) continue;
            if (wantVIP && !t.isVIP) continue;
            candidate = t;
            break;
        }
        if (candidate == null && !wantVIP) {
            // thường: cho phép lấy vé VIP nếu còn
            for (Ticket t : tickets) {
                if (!t.isSold && !t.isHeld) {
                    candidate = t;
                    break;
                }
            }
        }
        if (candidate == null) return null;

        candidate.isHeld = true;
        candidate.heldBy = counterName;
        candidate.holdExpiryTime = System.currentTimeMillis() + 5000;
        return candidate;
    }

    public synchronized boolean sellHeldTicket(String counterName, Ticket t) {
        if (t == null) return false;
        if (!tickets.contains(t)) return false;
        if (t.isSold) return false;
        if (!t.isHeld) return false;
        if (!Objects.equals(t.heldBy, counterName)) return false;
        if (System.currentTimeMillis() > t.holdExpiryTime) return false;

        t.isSold = true;
        t.isHeld = false;
        t.heldBy = null;
        return true;
    }

    public synchronized int releaseExpiredTickets() {
        long now = System.currentTimeMillis();
        int released = 0;
        for (Ticket t : tickets) {
            if (t.isHeld && !t.isSold && now > t.holdExpiryTime) {
                System.out.println("TimeoutManager: Vé " + t.roomName + "-" + t.ticketId + " hết hạn giữ, đã trả lại kho");
                t.isHeld = false;
                t.heldBy = null;
                released++;
            }
        }
        return released;
    }

    public synchronized int remainingAvailable() {
        int c = 0;
        for (Ticket t : tickets) if (!t.isSold && !t.isHeld) c++;
        return c;
    }

    public synchronized int countSold() {
        int c = 0;
        for (Ticket t : tickets) if (t.isSold) c++;
        return c;
    }
}

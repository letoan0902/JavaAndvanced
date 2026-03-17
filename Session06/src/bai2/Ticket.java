package bai2;

public class Ticket {
    final String ticketId;
    final String roomName;
    boolean isSold;

    Ticket(String ticketId, String roomName) {
        this.ticketId = ticketId;
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return roomName + "-" + ticketId;
    }
}

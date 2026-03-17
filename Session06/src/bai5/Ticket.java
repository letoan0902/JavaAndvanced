package bai5;

public class Ticket {
    final String ticketId;
    final String roomName;
    final boolean isVIP;

    boolean isSold;
    boolean isHeld;
    long holdExpiryTime;
    String heldBy;

    Ticket(String ticketId, String roomName, boolean isVIP) {
        this.ticketId = ticketId;
        this.roomName = roomName;
        this.isVIP = isVIP;
    }

    @Override
    public String toString() {
        return roomName + "-" + ticketId + (isVIP ? "(VIP)" : "");
    }
}

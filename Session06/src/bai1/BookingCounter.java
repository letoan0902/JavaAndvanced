package bai1;

public class BookingCounter {
    final String counterName;
    final TicketPool roomA;
    final TicketPool roomB;
    final LockOrder lockOrder;

    BookingCounter(String counterName, TicketPool roomA, TicketPool roomB, LockOrder lockOrder) {
        this.counterName = counterName;
        this.roomA = roomA;
        this.roomB = roomB;
        this.lockOrder = lockOrder;
    }

    void sellComboOnce() {
        sellComboWithPossibleDeadlock();
    }

    boolean sellCombo() {
        return sellComboFixedOrder();
    }

    /**
     * Bản cố tình tạo deadlock bằng cách cho 2 quầy khóa theo thứ tự ngược nhau.
     */
    private boolean sellComboWithPossibleDeadlock() {
        TicketPool first = lockOrder == LockOrder.B_THEN_A ? roomB : roomA;
        TicketPool second = lockOrder == LockOrder.B_THEN_A ? roomA : roomB;

        synchronized (first) {
            Ticket t1 = first.takeOneUnsoldNoSync();
            if (t1 == null) {
                System.out.println(counterName + ": Hết vé phòng " + first.roomName + ", bán combo thất bại");
                return false;
            }
            System.out.println(counterName + ": Đã lấy vé " + t1);

            // Cố tình sleep để tăng khả năng deadlock
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }

            System.out.println(counterName + ": Đang chờ vé " + second.roomName + "...");

            synchronized (second) {
                Ticket t2 = second.takeOneUnsoldNoSync();
                if (t2 == null) {
                    first.returnTicketNoSync(t1);
                    System.out.println(counterName + ": Hết vé phòng " + second.roomName + ", trả lại vé " + t1 + ", combo thất bại");
                    return false;
                }
                System.out.println(counterName + " bán combo thành công: " + t1 + " & " + t2);
                return true;
            }
        }
    }

    /**
     * Phòng chống deadlock: tất cả quầy đều khóa theo cùng 1 thứ tự (A < B).
     */
    private boolean sellComboFixedOrder() {
        TicketPool first;
        TicketPool second;

        if (roomA.roomName.compareTo(roomB.roomName) <= 0) {
            first = roomA;
            second = roomB;
        } else {
            first = roomB;
            second = roomA;
        }

        synchronized (first) {
            synchronized (second) {
                Ticket t1 = first.takeOneUnsoldNoSync();
                if (t1 == null) {
                    System.out.println(counterName + ": Hết vé phòng " + first.roomName + ", bán combo thất bại");
                    return false;
                }
                Ticket t2 = second.takeOneUnsoldNoSync();
                if (t2 == null) {
                    first.returnTicketNoSync(t1);
                    System.out.println(counterName + ": Hết vé phòng " + second.roomName + ", trả lại vé " + t1 + ", combo thất bại");
                    return false;
                }
                System.out.println(counterName + " bán combo thành công: " + t1 + " & " + t2);
                return true;
            }
        }
    }
}

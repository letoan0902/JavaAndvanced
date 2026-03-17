package bai3;

public class TicketSupplier implements Runnable {
    final TicketPool roomA;
    final TicketPool roomB;
    final int supplyCount;
    final long intervalMs;
    final int rounds;

    TicketSupplier(TicketPool roomA, TicketPool roomB, int supplyCount, long intervalMs, int rounds) {
        this.roomA = roomA;
        this.roomB = roomB;
        this.supplyCount = supplyCount;
        this.intervalMs = intervalMs;
        this.rounds = rounds;
    }

    @Override
    public void run() {
        for (int i = 0; i < rounds; i++) {
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                return;
            }
            roomA.addTickets(supplyCount);
            System.out.println("Nhà cung cấp: Đã thêm " + supplyCount + " vé vào phòng A");
            roomB.addTickets(supplyCount);
            System.out.println("Nhà cung cấp: Đã thêm " + supplyCount + " vé vào phòng B");
        }
    }
}

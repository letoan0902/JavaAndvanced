package bai1;

public class OrderCalculator {
    public long calculateTotal(Order order) {
        long total = 0;
        for (OrderItem item : order.getItems()) {
            total += item.getLineTotal();
        }
        order.setTotalAmount(total);
        return total;
    }
}

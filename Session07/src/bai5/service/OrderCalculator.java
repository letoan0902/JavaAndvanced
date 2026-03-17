package bai5.service;

import bai5.discount.DiscountStrategy;
import bai5.model.Order;
import bai5.model.OrderItem;

public class OrderCalculator {
    public long calculateTotal(Order order) {
        long total = 0;
        for (OrderItem item : order.getItems()) {
            total += item.getLineTotal();
        }
        order.setTotalAmount(total);
        return total;
    }

    public long applyDiscount(Order order, DiscountStrategy discountStrategy) {
        long total = order.getTotalAmount();
        long discount = discountStrategy.discount(total);
        long payable = Math.max(0, total - discount);
        order.setDiscountAmount(discount);
        order.setPayableAmount(payable);
        return payable;
    }
}

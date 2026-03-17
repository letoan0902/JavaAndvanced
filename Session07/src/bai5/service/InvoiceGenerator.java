package bai5.service;

import bai5.model.Order;
import bai5.model.OrderItem;

public class InvoiceGenerator {
    public void printInvoice(Order order) {
        System.out.println("=== HÓA ĐƠN ===");
        System.out.println("Khách: " + order.getCustomer().getName());
        for (OrderItem item : order.getItems()) {
            System.out.println(item.getProduct().getCode() + " - Số lượng: " + item.getQuantity() + " - Đơn giá: " + item.getProduct().getPrice() + " - Thành tiền: " + item.getLineTotal());
        }
        System.out.println("Tổng tiền: " + order.getTotalAmount());
        System.out.println("Giảm giá: " + order.getDiscountAmount());
        System.out.println("Cần thanh toán: " + order.getPayableAmount());
    }
}

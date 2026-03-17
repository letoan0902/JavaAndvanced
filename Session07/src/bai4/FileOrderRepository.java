package bai4;

import bai5.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
        System.out.println("Lưu đơn hàng vào file: " + order.getOrderId());
    }

    @Override
    public List<Order> findAll() {
        return Collections.unmodifiableList(orders);
    }
}

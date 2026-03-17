package bai4;

import bai5.model.Order;

public class OrderService {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
        notificationService.send("Đơn hàng " + order.getOrderId() + " đã được tạo", order.getCustomer().getEmail());
    }
}

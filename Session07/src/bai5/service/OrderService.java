package bai5.service;

import bai5.discount.DiscountStrategy;
import bai5.model.Customer;
import bai5.model.Order;
import bai5.model.Product;
import bai5.notify.NotificationService;
import bai5.payment.PaymentMethod;
import bai5.repo.OrderRepository;

import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final OrderCalculator orderCalculator;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderIdGenerator orderIdGenerator;

    public OrderService(OrderRepository orderRepository,
                        NotificationService notificationService,
                        OrderCalculator orderCalculator,
                        InvoiceGenerator invoiceGenerator,
                        OrderIdGenerator orderIdGenerator) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
        this.orderCalculator = orderCalculator;
        this.invoiceGenerator = invoiceGenerator;
        this.orderIdGenerator = orderIdGenerator;
    }

    public Order createOrder(Customer customer,
                             List<Product> products,
                             List<Integer> quantities,
                             DiscountStrategy discountStrategy,
                             PaymentMethod paymentMethod) {
        if (products.size() != quantities.size()) {
            throw new IllegalArgumentException("products and quantities size mismatch");
        }

        Order order = new Order(orderIdGenerator.nextId(), customer);
        for (int i = 0; i < products.size(); i++) {
            order.addItem(products.get(i), quantities.get(i));
        }

        orderCalculator.calculateTotal(order);
        orderCalculator.applyDiscount(order, discountStrategy);

        order.setPaymentMethodName(paymentMethod.getName());
        paymentMethod.pay(order.getPayableAmount());

        orderRepository.save(order);
        notificationService.send("Đơn hàng " + order.getOrderId() + " đã được tạo", customer.getEmail());

        return order;
    }

    public void printInvoice(Order order) {
        invoiceGenerator.printInvoice(order);
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    public long totalRevenue() {
        long sum = 0;
        for (Order o : orderRepository.findAll()) {
            sum += o.getPayableAmount();
        }
        return sum;
    }
}

package bai5.ui;

import bai5.discount.*;
import bai5.model.Customer;
import bai5.model.Order;
import bai5.model.Product;
import bai5.notify.EmailNotification;
import bai5.notify.NotificationService;
import bai5.payment.*;
import bai5.repo.FileOrderRepository;
import bai5.repo.OrderRepository;
import bai5.service.*;

import java.util.*;

public class ConsoleMenu {
    private final Scanner scanner;

    private final List<Product> products = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    private final Map<Integer, PaymentMethod> paymentMethods = new LinkedHashMap<>();
    private final Map<Integer, DiscountStrategy> discountStrategies = new LinkedHashMap<>();

    private final OrderService orderService;

    public ConsoleMenu(Scanner scanner) {
        this.scanner = scanner;

        OrderRepository orderRepository = new FileOrderRepository();
        NotificationService notificationService = new EmailNotification();

        this.orderService = new OrderService(
                orderRepository,
                notificationService,
                new OrderCalculator(),
                new InvoiceGenerator(),
                new OrderIdGenerator()
        );

        seedDefaults();
    }

    private void seedDefaults() {
        paymentMethods.put(1, new CODPayment());
        paymentMethods.put(2, new CreditCardPayment());
        paymentMethods.put(3, new MomoPayment());
        paymentMethods.put(4, new VNPayPayment());

        discountStrategies.put(1, new PercentageDiscount(10));
        discountStrategies.put(2, new FixedDiscount(50_000));
        discountStrategies.put(3, new HolidayDiscount());
    }

    public void run() {
        while (true) {
            System.out.println("\n=== MENU (Bài 5) ===");
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("3. Tạo đơn hàng mới");
            System.out.println("4. Xem danh sách đơn hàng");
            System.out.println("5. Tính tổng doanh thu");
            System.out.println("6. Thêm phương thức thanh toán mới (mô phỏng)");
            System.out.println("7. Thêm chiến lược giảm giá mới (mô phỏng)");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addProduct();
                case "2" -> addCustomer();
                case "3" -> createOrder();
                case "4" -> listOrders();
                case "5" -> revenue();
                case "6" -> addPaymentMethodMock();
                case "7" -> addDiscountStrategyMock();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private void addProduct() {
        System.out.print("Mã: ");
        String code = scanner.nextLine().trim();
        System.out.print("Tên: ");
        String name = scanner.nextLine().trim();
        System.out.print("Giá: ");
        long price = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Danh mục: ");
        String category = scanner.nextLine().trim();

        products.add(new Product(code, name, price, category));
        System.out.println("Đã thêm sản phẩm " + code);
    }

    private void addCustomer() {
        System.out.print("Tên: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("ĐT: ");
        String phone = scanner.nextLine().trim();

        customers.add(new Customer(name, email, phone));
        System.out.println("Đã thêm khách hàng");
    }

    private void createOrder() {
        if (customers.isEmpty()) {
            System.out.println("Chưa có khách hàng. Hãy thêm khách hàng trước.");
            return;
        }
        if (products.isEmpty()) {
            System.out.println("Chưa có sản phẩm. Hãy thêm sản phẩm trước.");
            return;
        }

        Customer customer = chooseCustomer();
        List<Product> chosenProducts = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        while (true) {
            Product p = chooseProduct();
            System.out.print("Số lượng: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            chosenProducts.add(p);
            quantities.add(qty);

            System.out.print("Thêm sản phẩm nữa? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                break;
            }
        }

        DiscountStrategy discount = chooseDiscount();
        PaymentMethod payment = choosePayment();

        Order order = orderService.createOrder(customer, chosenProducts, quantities, discount, payment);
        orderService.printInvoice(order);
    }

    private Customer chooseCustomer() {
        System.out.println("Chọn khách:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }
        System.out.print("Chọn: ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        return customers.get(Math.max(0, Math.min(idx, customers.size() - 1)));
    }

    private Product chooseProduct() {
        System.out.println("Chọn sản phẩm:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
        System.out.print("Chọn: ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        return products.get(Math.max(0, Math.min(idx, products.size() - 1)));
    }

    private DiscountStrategy chooseDiscount() {
        System.out.println("Chọn giảm giá:");
        for (Map.Entry<Integer, DiscountStrategy> e : discountStrategies.entrySet()) {
            System.out.println(e.getKey() + ". " + e.getValue().getName());
        }
        System.out.print("Chọn: ");
        int k = Integer.parseInt(scanner.nextLine().trim());
        return discountStrategies.getOrDefault(k, new NoDiscount());
    }

    private PaymentMethod choosePayment() {
        System.out.println("Chọn thanh toán:");
        for (Map.Entry<Integer, PaymentMethod> e : paymentMethods.entrySet()) {
            System.out.println(e.getKey() + ". " + e.getValue().getName());
        }
        System.out.print("Chọn: ");
        int k = Integer.parseInt(scanner.nextLine().trim());
        return paymentMethods.getOrDefault(k, new CODPayment());
    }

    private void listOrders() {
        List<Order> orders = orderService.listOrders();
        if (orders.isEmpty()) {
            System.out.println("Chưa có đơn hàng.");
            return;
        }
        System.out.println("Danh sách đơn hàng:");
        for (Order o : orders) {
            System.out.println(o.getOrderId() + " - " + o.getCustomer().getName() + " - " + o.getPayableAmount());
        }
    }

    private void revenue() {
        System.out.println("Tổng doanh thu: " + orderService.totalRevenue());
    }

    private void addPaymentMethodMock() {
        int nextKey = paymentMethods.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
        System.out.print("Tên phương thức mới: ");
        String name = scanner.nextLine().trim();
        paymentMethods.put(nextKey, new PaymentMethod() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void pay(long amount) {
                System.out.println("Xử lý thanh toán " + name + ": " + amount + " - Thành công");
            }
        });
        System.out.println("Đã thêm phương thức thanh toán " + name);
    }

    private void addDiscountStrategyMock() {
        int nextKey = discountStrategies.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
        System.out.print("Mô tả giảm giá mới: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Phần trăm giảm (0-100): ");
        int percent = Integer.parseInt(scanner.nextLine().trim());

        discountStrategies.put(nextKey, new PercentageDiscount(percent) {
            @Override
            public String getName() {
                return desc + " (" + percent + "%)";
            }
        });

        System.out.println("Đã thêm chiến lược giảm giá " + desc);
    }

    private static class NoDiscount implements DiscountStrategy {
        @Override
        public String getName() {
            return "Không giảm";
        }

        @Override
        public long discount(long totalAmount) {
            return 0;
        }
    }
}

import bai1.*;
import bai2.*;
import bai3.*;
import bai4.*;
import bai5.model.Customer;
import bai5.model.Order;
import bai5.model.Product;
import bai5.ui.ConsoleMenu;
import bai6.ui.ChannelMenu;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        runBai1();
        runBai2();
        runBai3();
        runBai4();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== CHỌN BÀI ===");
            System.out.println("5. Chạy menu Bài 5 (SOLID full)");
            System.out.println("6. Demo Bài 6 (SalesChannel + Abstract Factory)");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "5" -> new ConsoleMenu(scanner).run();
                case "6" -> new ChannelMenu(scanner).runDemo();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private static void runBai1() {
        System.out.println("\n--- Bài 1 ---");
        Product sp01 = new Product("SP01", "Laptop", 15000000);
        Product sp02 = new Product("SP02", "Chuột", 300000);
        System.out.println("Tạo sản phẩm: " + sp01 + ", " + sp02);
        System.out.println("Đã thêm sản phẩm SP01, SP02");

        Customer customer = new Customer("Nguyễn Văn A", "a@example.com", "Hà Nội");
        System.out.println("Tạo khách hàng: " + customer);
        System.out.println("Đã thêm khách hàng");

        Order order = new Order("ORD001", customer);
        order.addItem(sp01, 1);
        order.addItem(sp02, 2);
        System.out.println("Tạo đơn hàng: SP01 (1 cái), SP02 (2 cái)");
        System.out.println("Đơn hàng ORD001 được tạo");

        System.out.println("Tính tổng tiền");
        long total = new bai1.OrderCalculator().calculateTotal(order);
        System.out.println("Tổng tiền: " + total);

        System.out.println("Lưu đơn hàng");
        bai1.OrderRepository repo = new bai1.OrderRepository();
        repo.save(order);
        System.out.println("Đã lưu đơn hàng " + order.getOrderId());

        System.out.println("Gửi email xác nhận");
        new bai1.EmailService().sendConfirmationEmail(order);
    }

    private static void runBai2() {
        System.out.println("\n--- Bài 2 ---");
        double total = 1_000_000;

        bai2.OrderCalculator calc = new bai2.OrderCalculator(new PercentageDiscount(10));
        System.out.println("Đơn hàng: tổng tiền 1.000.000, áp dụng PercentageDiscount 10%");
        System.out.println("Số tiền sau giảm: " + (long) calc.applyDiscount(total));

        calc.setDiscountStrategy(new FixedDiscount(50_000));
        System.out.println("Đơn hàng: tổng tiền 1.000.000, áp dụng FixedDiscount 50.000");
        System.out.println("Số tiền sau giảm: " + (long) calc.applyDiscount(total));

        calc.setDiscountStrategy(new NoDiscount());
        System.out.println("Đơn hàng: tổng tiền 1.000.000, áp dụng NoDiscount");
        System.out.println("Số tiền sau giảm: " + (long) calc.applyDiscount(total));

        calc.setDiscountStrategy(new HolidayDiscount());
        System.out.println("Thêm HolidayDiscount 15% (không sửa code cũ)");
        System.out.println("Số tiền sau giảm: " + (long) calc.applyDiscount(total));
    }

    private static void runBai3() {
        System.out.println("\n--- Bài 3 ---");
        PaymentProcessor processor = new PaymentProcessor();

        System.out.println("COD\nSố tiền 500.000");
        processor.process(new CODPayment(), 500_000);

        System.out.println("Thẻ tín dụng\nSố tiền 1.000.000");
        processor.process(new CreditCardPayment(), 1_000_000);

        System.out.println("Ví MoMo\nSố tiền 750.000");
        processor.process(new MomoPayment(), 750_000);

        System.out.println("Kiểm tra LSP\nThay thế CreditCardPayment bằng MomoPayment trong cùng interface");
        PaymentMethod method = new bai3.CreditCardPayment();
        processor.process(method, 111_000);
        method = new bai3.MomoPayment();
        processor.process(method, 111_000);
        System.out.println("Chương trình vẫn chạy đúng");
    }

    private static void runBai4() {
        System.out.println("\n--- Bài 4 ---");

        // Dùng FileOrderRepository và EmailService
        bai4.OrderRepository fileRepo = new bai4.FileOrderRepository();
        bai4.NotificationService email = new bai4.EmailService();
        bai4.OrderService orderService1 = new bai4.OrderService(fileRepo, email);

        Product p = new Product("SP01", "Laptop", 15000000, "Điện tử");
        Customer c = new Customer("Nguyễn Văn A", "a@example.com", "0123456789");
        Order o1 = new Order("ORD001", c);
        o1.addItem(p, 1);
        System.out.println("Dùng FileOrderRepository và EmailService\nTạo đơn hàng ORD001");
        orderService1.createOrder(o1);

        // Đổi sang DatabaseOrderRepository và SMSNotification
        bai4.OrderRepository dbRepo = new bai4.DatabaseOrderRepository();
        bai4.NotificationService sms = new bai4.SMSNotification();
        bai4.OrderService orderService2 = new bai4.OrderService(dbRepo, sms);

        Order o2 = new Order("ORD002", c);
        o2.addItem(p, 1);
        System.out.println("\nĐổi sang DatabaseOrderRepository và SMSNotification\nTạo đơn hàng ORD002");
        orderService2.createOrder(o2);

        System.out.println("Không sửa code OrderService\nOrderService hoạt động với implementation mới");
    }
}
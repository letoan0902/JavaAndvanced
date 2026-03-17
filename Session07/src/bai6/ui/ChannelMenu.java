package bai6.ui;

import bai5.model.Customer;
import bai5.model.Order;
import bai5.model.Product;
import bai5.repo.FileOrderRepository;
import bai5.repo.OrderRepository;
import bai5.service.*;
import bai6.factory.*;

import java.util.List;
import java.util.Scanner;

public class ChannelMenu {
    private final Scanner scanner;

    public ChannelMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void runDemo() {
        System.out.println("Chọn kênh bán");
        System.out.println("1. Website, 2. Mobile App, 3. POS");
        System.out.print("Bạn chọn: ");
        String choice = scanner.nextLine().trim();

        ChannelFactory factory;
        switch (choice) {
            case "1" -> {
                System.out.println("Bạn đã chọn kênh Website");
                System.out.print("Nhập mã giảm giá (vd WEB10): ");
                String coupon = scanner.nextLine().trim();
                factory = new WebsiteFactory(coupon);
            }
            case "2" -> {
                System.out.println("Bạn đã chọn kênh Mobile App");
                System.out.print("Có phải lần đầu mua? (y/n): ");
                boolean firstTime = scanner.nextLine().trim().equalsIgnoreCase("y");
                factory = new MobileAppFactory(firstTime);
            }
            default -> {
                System.out.println("Bạn đã chọn kênh POS");
                System.out.print("Khách hàng có phải member? (y/n): ");
                boolean isMember = scanner.nextLine().trim().equalsIgnoreCase("y");
                factory = new StorePosFactory(isMember);
            }
        }

        // Demo data
        Product laptop = new Product("SP01", "Laptop", 15_000_000, "Điện tử");
        Customer customer = new Customer("Nguyễn Văn A", "a@example.com", "0123456789");

        OrderRepository repo = new FileOrderRepository();
        OrderService svc = new OrderService(
                repo,
                factory.createNotificationService(),
                new OrderCalculator(),
                new InvoiceGenerator(),
                new OrderIdGenerator()
        );

        System.out.println("\nTạo đơn hàng (" + factory.getChannelName() + ")");
        Order order = svc.createOrder(
                customer,
                List.of(laptop),
                List.of(1),
                factory.createDiscountStrategy(),
                factory.createPaymentMethod()
        );

        svc.printInvoice(order);

        System.out.println("\nThêm kênh mới: Kiosk tự động");
        System.out.println("Chỉ cần tạo factory mới, không sửa code cũ");
    }
}

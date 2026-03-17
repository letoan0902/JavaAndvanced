package bai1;

public class EmailService {
    public void sendConfirmationEmail(Order order) {
        System.out.println("Đã gửi email đến " + order.getCustomer().getEmail() + ": Đơn hàng " + order.getOrderId() + " đã được tạo");
    }
}

package bai5.payment;

public class VNPayPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "VNPay";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán VNPay: " + amount + " - Thành công");
    }
}

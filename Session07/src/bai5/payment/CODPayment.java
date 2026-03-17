package bai5.payment;

public class CODPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "COD";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán COD: " + amount + " - Thành công");
    }
}

package bai5.payment;

public class MomoPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "MoMo";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán MoMo: " + amount + " - Thành công");
    }
}

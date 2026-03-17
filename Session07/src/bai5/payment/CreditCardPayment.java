package bai5.payment;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "Thẻ tín dụng";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán thẻ tín dụng: " + amount + " - Thành công");
    }
}

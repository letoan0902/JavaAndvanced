package bai3;

public class CreditCardPayment implements CardPayable {
    @Override
    public void processCreditCard(long amount) {
        System.out.println("Xử lý thanh toán thẻ tín dụng: " + amount + " - Thành công");
    }

    @Override
    public String getName() {
        return "Thẻ tín dụng";
    }
}

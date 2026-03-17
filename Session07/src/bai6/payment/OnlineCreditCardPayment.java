package bai6.payment;

import bai5.payment.PaymentMethod;

public class OnlineCreditCardPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "Thẻ tín dụng";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán thẻ tín dụng qua cổng thanh toán online");
    }
}

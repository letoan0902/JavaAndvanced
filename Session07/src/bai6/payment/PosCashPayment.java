package bai6.payment;

import bai5.payment.PaymentMethod;

public class PosCashPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "Tiền mặt";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán tiền mặt tại quầy");
    }
}

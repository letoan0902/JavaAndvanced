package bai6.payment;

import bai5.payment.PaymentMethod;

public class IntegratedMomoPayment implements PaymentMethod {
    @Override
    public String getName() {
        return "MoMo";
    }

    @Override
    public void pay(long amount) {
        System.out.println("Xử lý thanh toán MoMo tích hợp");
    }
}

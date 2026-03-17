package bai3;

public class PaymentProcessor {
    public void process(PaymentMethod method, long amount) {
        method.pay(amount);
    }
}

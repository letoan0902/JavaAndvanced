package bai5.payment;

public interface PaymentMethod {
    String getName();
    void pay(long amount);
}

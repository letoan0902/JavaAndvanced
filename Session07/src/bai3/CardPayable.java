package bai3;

public interface CardPayable extends PaymentMethod {
    void processCreditCard(long amount);

    @Override
    default void pay(long amount) {
        processCreditCard(amount);
    }

    @Override
    default String getName() {
        return "Thẻ tín dụng";
    }
}

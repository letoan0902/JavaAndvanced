package bai3;

public interface CODPayable extends PaymentMethod {
    void processCOD(long amount);

    @Override
    default void pay(long amount) {
        processCOD(amount);
    }

    @Override
    default String getName() {
        return "COD";
    }
}

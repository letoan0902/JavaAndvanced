package bai3;

public interface EWalletPayable extends PaymentMethod {
    void processEWallet(long amount);

    @Override
    default void pay(long amount) {
        processEWallet(amount);
    }

    @Override
    default String getName() {
        return "Ví điện tử";
    }
}

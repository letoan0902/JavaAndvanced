package bai5.discount;

public interface DiscountStrategy {
    String getName();
    long discount(long totalAmount);

    default long apply(long totalAmount) {
        long d = Math.max(0, discount(totalAmount));
        return Math.max(0, totalAmount - d);
    }
}

package bai5.discount;

public class FixedDiscount implements DiscountStrategy {
    private final long amount;

    public FixedDiscount(long amount) {
        this.amount = amount;
    }

    @Override
    public String getName() {
        return "-" + amount;
    }

    @Override
    public long discount(long totalAmount) {
        return Math.min(totalAmount, amount);
    }
}

package bai5.discount;

public class PercentageDiscount implements DiscountStrategy {
    private final int percent;

    public PercentageDiscount(int percent) {
        this.percent = percent;
    }

    @Override
    public String getName() {
        return percent + "%";
    }

    @Override
    public long discount(long totalAmount) {
        return totalAmount * percent / 100L;
    }
}

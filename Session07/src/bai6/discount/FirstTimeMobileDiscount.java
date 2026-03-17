package bai6.discount;

import bai5.discount.DiscountStrategy;

public class FirstTimeMobileDiscount implements DiscountStrategy {
    private final boolean firstTime;

    public FirstTimeMobileDiscount(boolean firstTime) {
        this.firstTime = firstTime;
    }

    @Override
    public String getName() {
        return "Mobile first-time 15%";
    }

    @Override
    public long discount(long totalAmount) {
        if (!firstTime) return 0;
        return totalAmount * 15 / 100L;
    }
}

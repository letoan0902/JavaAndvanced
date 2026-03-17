package bai6.discount;

import bai5.discount.DiscountStrategy;

public class MemberPosDiscount implements DiscountStrategy {
    private final boolean isMember;

    public MemberPosDiscount(boolean isMember) {
        this.isMember = isMember;
    }

    @Override
    public String getName() {
        return "POS member 5%";
    }

    @Override
    public long discount(long totalAmount) {
        if (!isMember) return 0;
        return totalAmount * 5 / 100L;
    }
}

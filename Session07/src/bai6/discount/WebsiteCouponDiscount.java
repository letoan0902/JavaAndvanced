package bai6.discount;

import bai5.discount.DiscountStrategy;

public class WebsiteCouponDiscount implements DiscountStrategy {
    private final String coupon;

    public WebsiteCouponDiscount(String coupon) {
        this.coupon = coupon;
    }

    @Override
    public String getName() {
        return "Website coupon " + coupon;
    }

    @Override
    public long discount(long totalAmount) {
        if ("WEB10".equalsIgnoreCase(coupon)) {
            return totalAmount * 10 / 100L;
        }
        return 0;
    }
}

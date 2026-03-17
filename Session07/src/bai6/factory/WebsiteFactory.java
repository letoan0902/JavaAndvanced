package bai6.factory;

import bai5.discount.DiscountStrategy;
import bai5.notify.EmailNotification;
import bai5.notify.NotificationService;
import bai5.payment.PaymentMethod;
import bai6.discount.WebsiteCouponDiscount;
import bai6.payment.OnlineCreditCardPayment;

public class WebsiteFactory implements ChannelFactory {
    private final String coupon;

    public WebsiteFactory(String coupon) {
        this.coupon = coupon;
    }

    @Override
    public String getChannelName() {
        return "Website";
    }

    @Override
    public DiscountStrategy createDiscountStrategy() {
        return new WebsiteCouponDiscount(coupon);
    }

    @Override
    public PaymentMethod createPaymentMethod() {
        return new OnlineCreditCardPayment();
    }

    @Override
    public NotificationService createNotificationService() {
        return new EmailNotification();
    }
}

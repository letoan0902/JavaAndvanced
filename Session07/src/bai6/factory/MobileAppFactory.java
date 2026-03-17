package bai6.factory;

import bai5.discount.DiscountStrategy;
import bai5.notify.NotificationService;
import bai5.payment.PaymentMethod;
import bai6.discount.FirstTimeMobileDiscount;
import bai6.notify.PushNotification;
import bai6.payment.IntegratedMomoPayment;

public class MobileAppFactory implements ChannelFactory {
    private final boolean firstTime;

    public MobileAppFactory(boolean firstTime) {
        this.firstTime = firstTime;
    }

    @Override
    public String getChannelName() {
        return "Mobile App";
    }

    @Override
    public DiscountStrategy createDiscountStrategy() {
        return new FirstTimeMobileDiscount(firstTime);
    }

    @Override
    public PaymentMethod createPaymentMethod() {
        return new IntegratedMomoPayment();
    }

    @Override
    public NotificationService createNotificationService() {
        return new PushNotification();
    }
}

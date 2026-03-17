package bai6.factory;

import bai5.discount.DiscountStrategy;
import bai5.notify.NotificationService;
import bai5.payment.PaymentMethod;

public interface ChannelFactory {
    String getChannelName();
    DiscountStrategy createDiscountStrategy();
    PaymentMethod createPaymentMethod();
    NotificationService createNotificationService();
}

package bai6.factory;

import bai5.discount.DiscountStrategy;
import bai5.notify.NotificationService;
import bai5.payment.PaymentMethod;
import bai6.discount.MemberPosDiscount;
import bai6.notify.PaperReceiptPrinter;
import bai6.payment.PosCashPayment;

public class StorePosFactory implements ChannelFactory {
    private final boolean isMember;

    public StorePosFactory(boolean isMember) {
        this.isMember = isMember;
    }

    @Override
    public String getChannelName() {
        return "POS";
    }

    @Override
    public DiscountStrategy createDiscountStrategy() {
        return new MemberPosDiscount(isMember);
    }

    @Override
    public PaymentMethod createPaymentMethod() {
        return new PosCashPayment();
    }

    @Override
    public NotificationService createNotificationService() {
        return new PaperReceiptPrinter();
    }
}

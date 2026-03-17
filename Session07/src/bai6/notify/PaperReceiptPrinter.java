package bai6.notify;

import bai5.notify.NotificationService;

public class PaperReceiptPrinter implements NotificationService {
    @Override
    public String getName() {
        return "In hóa đơn";
    }

    @Override
    public void send(String message, String recipient) {
        System.out.println("In hóa đơn giấy: " + message);
    }
}

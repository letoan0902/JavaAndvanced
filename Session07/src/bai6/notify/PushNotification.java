package bai6.notify;

import bai5.notify.NotificationService;

public class PushNotification implements NotificationService {
    @Override
    public String getName() {
        return "Push";
    }

    @Override
    public void send(String message, String recipient) {
        System.out.println("Gửi push notification: " + message);
    }
}

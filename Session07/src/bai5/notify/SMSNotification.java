package bai5.notify;

public class SMSNotification implements NotificationService {
    @Override
    public String getName() {
        return "SMS";
    }

    @Override
    public void send(String message, String recipient) {
        System.out.println("Gửi SMS: " + message);
    }
}

package bai5.notify;

public class EmailNotification implements NotificationService {
    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public void send(String message, String recipient) {
        System.out.println("Đã gửi email đến " + recipient + ": " + message);
    }
}

package bai5.notify;

public interface NotificationService {
    String getName();
    void send(String message, String recipient);
}

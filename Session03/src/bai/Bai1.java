package bai;

import java.util.List;

public class Bai1 {
    enum Status {
        ACTIVE, INACTIVE
    }

    record User(String username, String email, Status status) {}

    public static void run() {
        System.out.println("=== Bai 1 ===");
        List<User> users = List.of(
                new User("alice", "alice@gmail.com", Status.ACTIVE),
                new User("bob", "bob@yahoo.com", Status.INACTIVE),
                new User("charlie", "charlie@gmail.com", Status.ACTIVE)
        );

        users.forEach(System.out::println);
        System.out.println();
    }
}

package bai;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bai4 {
    record User(String username, String email) {}

    public static void run() {
        System.out.println("=== Bai 4 ===");
        List<User> users = List.of(
                new User("alice", "alice@gmail.com"),
                new User("bob", "bob@yahoo.com"),
                new User("alice", "alice2@gmail.com"),
                new User("charlie", "charlie@gmail.com"),
                new User("bob", "bob2@yahoo.com")
        );

        Map<String, User> uniqueByUsername = new LinkedHashMap<>();
        for (User user : users) {
            uniqueByUsername.putIfAbsent(user.username(), user);
        }

        List<User> deduplicatedUsers = new ArrayList<>(uniqueByUsername.values());
        deduplicatedUsers.forEach(System.out::println);
        System.out.println();
    }
}

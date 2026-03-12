package bai;

import java.util.List;

public class Bai2 {
    record User(String username, String email) {}

    public static void run() {
        System.out.println("=== Bai 2 ===");
        List<User> users = List.of(
                new User("alice", "alice@gmail.com"),
                new User("bob", "bob@yahoo.com"),
                new User("charlie", "charlie@gmail.com")
        );

        users.stream()
                .filter(user -> user.email().endsWith("@gmail.com"))
                .map(User::username)
                .forEach(System.out::println);

        System.out.println();
    }
}

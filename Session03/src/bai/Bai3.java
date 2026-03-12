package bai;

import java.util.List;
import java.util.Optional;

public class Bai3 {
    record User(String username, String email) {}

    static class UserRepository {
        private final List<User> users;

        UserRepository(List<User> users) {
            this.users = users;
        }

        Optional<User> findUserByUsername(String username) {
            return users.stream()
                    .filter(user -> user.username().equalsIgnoreCase(username))
                    .findFirst();
        }
    }

    public static void run() {
        System.out.println("=== Bai 3 ===");
        List<User> users = List.of(
                new User("alice", "alice@gmail.com"),
                new User("bob", "bob@yahoo.com"),
                new User("charlie", "charlie@gmail.com")
        );

        UserRepository repository = new UserRepository(users);

        String message = repository.findUserByUsername("alice")
                .map(user -> "Welcome " + user.username())
                .orElse("Guest login");

        System.out.println(message);
        System.out.println();
    }
}

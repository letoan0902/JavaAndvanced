package bai;

import java.util.List;

public class Bai5 {
    record User(String username, String email) {}

    public static void run() {
        System.out.println("=== Bai 5 ===");
        List<User> users = List.of(
                new User("alexander", "alexander@gmail.com"),
                new User("charlotte", "charlotte@gmail.com"),
                new User("Benjamin", "benjamin@gmail.com"),
                new User("bob", "bob@yahoo.com"),
                new User("amy", "amy@gmail.com")
        );

        users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.username().length(), u1.username().length()))
                .limit(3)
                .map(User::username)
                .forEach(System.out::println);

        System.out.println();
    }
}

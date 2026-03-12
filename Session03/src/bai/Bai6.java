package bai;

import java.util.List;

public class Bai6 {
    record Post(String title, List<String> tags) {}

    public static void run() {
        System.out.println("=== Bai 6 ===");
        List<Post> posts = List.of(
                new Post("Java Post", List.of("java", "backend")),
                new Post("Python Post", List.of("python", "data"))
        );

        List<String> flattenedTags = posts.stream()
                .flatMap(post -> post.tags().stream())
                .toList();

        System.out.println(flattenedTags);
        System.out.println();
    }
}

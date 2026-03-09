import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class UserBai4 {
    private final String username;

    public UserBai4() {
        this.username = "new_user";
    }

    public UserBai4(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

public class Bai4 {
    public static void main(String[] args) {
        List<UserBai4> users = Arrays.asList(new UserBai4("an"), new UserBai4("binh"), new UserBai4("chi"));

        Function<UserBai4, String> getUsername = UserBai4::getUsername;
        Consumer<String> print = System.out::println;
        Supplier<UserBai4> creator = UserBai4::new;

        users.stream().map(getUsername).forEach(print);
        print.accept(creator.get().getUsername());
    }
}

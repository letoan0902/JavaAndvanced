class UserBai6 {
    private final String username;

    public UserBai6(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

@FunctionalInterface
interface UserProcessor {
    String process(UserBai6 u);
}

class UserUtils {
    public static String convertToUpperCase(UserBai6 u) {
        return u.getUsername().toUpperCase();
    }
}

public class Bai6 {
    public static void main(String[] args) {
        UserProcessor processor = UserUtils::convertToUpperCase;
        UserBai6 user = new UserBai6("nguyenvana");
        System.out.println(processor.process(user));
    }
}

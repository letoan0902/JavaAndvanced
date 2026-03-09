import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class UserBai1 {
    private final String username;
    private final String role;

    public UserBai1(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}

public class Bai1 {
    public static void main(String[] args) {
        Predicate<UserBai1> isAdmin = user -> "ADMIN".equalsIgnoreCase(user.getRole());
        Function<UserBai1, String> toUsername = UserBai1::getUsername;
        Consumer<UserBai1> printUser = System.out::println;
        Supplier<UserBai1> defaultUserSupplier = () -> new UserBai1("default_user", "USER");

        UserBai1 admin = new UserBai1("an", "ADMIN");
        UserBai1 defaultUser = defaultUserSupplier.get();

        System.out.println("1. Kiểm tra User có phải Admin hay không -> Predicate: " + isAdmin.test(admin));
        System.out.println("2. Chuyển User thành username -> Function: " + toUsername.apply(admin));
        System.out.print("3. In thông tin User -> Consumer: ");
        printUser.accept(admin);
        System.out.print("4. Khởi tạo User mặc định -> Supplier: ");
        printUser.accept(defaultUser);
    }
}

@FunctionalInterface
interface Authenticatable {
    String getPassword();

    default boolean isAuthenticated() {
        return getPassword() != null && !getPassword().isEmpty();
    }

    static String encrypt(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().append("_encrypted").toString();
    }
}

class Account implements Authenticatable {
    private final String password;

    public Account(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }
}

public class Bai3 {
    public static void main(String[] args) {
        Account account = new Account("123456");
        System.out.println("Password: " + account.getPassword());
        System.out.println("Authenticated: " + account.isAuthenticated());
        System.out.println("Encrypted: " + Authenticatable.encrypt(account.getPassword()));
    }
}

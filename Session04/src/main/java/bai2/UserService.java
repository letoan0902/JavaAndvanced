package bai2;

public class UserService {
    public boolean checkRegistrationAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Tuổi không được âm");
        }
        return age >= 18;
    }
}

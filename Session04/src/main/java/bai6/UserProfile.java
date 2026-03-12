package bai6;

import java.time.LocalDate;

public class UserProfile {
    private String email;
    private LocalDate dob;

    public UserProfile(String email, LocalDate dob) {
        this.email = email;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDob() {
        return dob;
    }
}

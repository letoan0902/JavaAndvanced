package bai6;

import java.time.LocalDate;

public class User {
    private String id;
    private String email;
    private LocalDate dob;

    public User(String id, String email, LocalDate dob) {
        this.id = id;
        this.email = email;
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}

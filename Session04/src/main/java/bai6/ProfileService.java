package bai6;

import java.time.LocalDate;
import java.util.List;

public class ProfileService {
    public User updateProfile(User existingUser, UserProfile newProfile, List<User> allUsers) {
        if (existingUser == null || newProfile == null) {
            return null;
        }

        if (newProfile.getDob().isAfter(LocalDate.now())) {
            return null;
        }

        String newEmail = newProfile.getEmail();

        if (allUsers != null) {
            for (User u : allUsers) {
                if (u.getEmail().equals(newEmail)) {
                    if (!u.getId().equals(existingUser.getId())) {
                        return null;
                    }
                }
            }
        }

        existingUser.setEmail(newEmail);
        existingUser.setDob(newProfile.getDob());
        return existingUser;
    }
}

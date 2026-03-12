package bai6;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileServiceTest {

    private final ProfileService service = new ProfileService();

    @Test
    void testUpdateProfile_Success_ValidInputs() {
        User user = new User("1", "old@example.com", LocalDate.of(1990, 1, 1));
        UserProfile newProfile = new UserProfile("new@example.com", LocalDate.of(1990, 1, 1));
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        allUsers.add(new User("2", "other@example.com", LocalDate.of(1995, 1, 1)));

        User result = service.updateProfile(user, newProfile, allUsers);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testUpdateProfile_Fail_FutureDate() {
        User user = new User("1", "val@ex.com", LocalDate.now());
        UserProfile newProfile = new UserProfile("val@ex.com", LocalDate.now().plusDays(1));
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user);

        User result = service.updateProfile(user, newProfile, allUsers);

        assertNull(result);
    }

    @Test
    void testUpdateProfile_Fail_DuplicateEmail() {
        User user1 = new User("1", "me@ex.com", LocalDate.now());
        User user2 = new User("2", "taken@ex.com", LocalDate.now());

        UserProfile newProfile = new UserProfile("taken@ex.com", LocalDate.now());

        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);

        User result = service.updateProfile(user1, newProfile, allUsers);

        assertNull(result);
    }

    @Test
    void testUpdateProfile_Success_SameEmail() {
        User user1 = new User("1", "me@ex.com", LocalDate.now());
        UserProfile newProfile = new UserProfile("me@ex.com", LocalDate.now().minusDays(1));

        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(new User("2", "other@ex.com", LocalDate.now()));

        User result = service.updateProfile(user1, newProfile, allUsers);

        assertNotNull(result);
        assertEquals("me@ex.com", result.getEmail());
    }

    @Test
    void testUpdateProfile_Success_EmptyList() {
        User user = new User("1", "me@ex.com", LocalDate.now());
        UserProfile newProfile = new UserProfile("new@ex.com", LocalDate.now());
        List<User> allUsers = new ArrayList<>();

        User result = service.updateProfile(user, newProfile, allUsers);

        assertNotNull(result);
    }

    @Test
    void testUpdateProfile_Fail_MultipleViolations() {
        User user1 = new User("1", "me@ex.com", LocalDate.now());
        User user2 = new User("2", "taken@ex.com", LocalDate.now());

        UserProfile newProfile = new UserProfile("taken@ex.com", LocalDate.now().plusDays(1));

        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);

        User result = service.updateProfile(user1, newProfile, allUsers);

        assertNull(result);
    }
}

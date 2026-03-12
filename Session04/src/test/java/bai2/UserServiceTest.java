package bai2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    void testCheckRegistrationAge_Valid() {
        int age = 18;
        boolean result = userService.checkRegistrationAge(age);
        assertEquals(true, result, "Age 18 should be valid");
    }

    @Test
    void testCheckRegistrationAge_TooYoung() {
        int age = 17;
        boolean result = userService.checkRegistrationAge(age);
        assertEquals(false, result, "Age 17 should be invalid");
    }

    @Test
    void testCheckRegistrationAge_Negative() {
        int age = -1;
        assertThrows(IllegalArgumentException.class, () -> userService.checkRegistrationAge(age), "Negative age should throw IllegalArgumentException");
    }
}

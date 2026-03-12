package bai1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    void testIsValidUsername_Valid() {
        String validUsername = "user123";
        boolean result = validator.isValidUsername(validUsername);
        assertTrue(result, "Username should be valid");
    }

    @Test
    void testIsValidUsername_TooShort() {
        String shortUsername = "abc";
        boolean result = validator.isValidUsername(shortUsername);
        assertFalse(result, "Username is too short");
    }

    @Test
    void testIsValidUsername_ContainsSpace() {
        String usernameWithSpace = "user name";
        boolean result = validator.isValidUsername(usernameWithSpace);
        assertFalse(result, "Username contains space");
    }
}

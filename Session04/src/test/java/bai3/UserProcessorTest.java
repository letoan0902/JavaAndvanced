package bai3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserProcessorTest {

    private UserProcessor userProcessor;

    @BeforeEach
    void setUp() {
        userProcessor = new UserProcessor();
    }

    @Test
    void testProcessEmail_Valid() {
        String input = "user@gmail.com";
        String result = userProcessor.processEmail(input);
        assertEquals("user@gmail.com", result);
    }

    @Test
    void testProcessEmail_MissingAtSymbol() {
        String input = "usergmail.com";
        assertThrows(IllegalArgumentException.class, () -> userProcessor.processEmail(input));
    }

    @Test
    void testProcessEmail_MissingDomain() {
        String input = "user@";
        assertThrows(IllegalArgumentException.class, () -> userProcessor.processEmail(input));
    }

    @Test
    void testProcessEmail_Normalization() {
        String input = "Example@Gmail.com";
        String result = userProcessor.processEmail(input);
        assertEquals("example@gmail.com", result);
    }
}

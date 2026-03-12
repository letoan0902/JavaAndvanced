package bai4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void testEvaluatePasswordStrength_Strong() {
        assertEquals("Mạnh", validator.evaluatePasswordStrength("Abc123!@"));
    }

    @Test
    void testEvaluatePasswordStrength_MissingUpper() {
        assertEquals("Trung bình", validator.evaluatePasswordStrength("abc123!@"));
    }

    @Test
    void testEvaluatePasswordStrength_MissingLower() {
        assertEquals("Trung bình", validator.evaluatePasswordStrength("ABC123!@"));
    }

    @Test
    void testEvaluatePasswordStrength_MissingDigit() {
        assertEquals("Trung bình", validator.evaluatePasswordStrength("Abcdef!@"));
    }

    @Test
    void testEvaluatePasswordStrength_MissingSpecial() {
        assertEquals("Trung bình", validator.evaluatePasswordStrength("Abc12345"));
    }

    @Test
    void testEvaluatePasswordStrength_TooShort() {
        assertEquals("Yếu", validator.evaluatePasswordStrength("Ab1!"));
    }

    @Test
    void testEvaluatePasswordStrength_OnlyLower() {
        assertEquals("Yếu", validator.evaluatePasswordStrength("password"));
    }

    @Test
    void testEvaluatePasswordStrength_OnlyUpperAndDigit() {
        assertEquals("Yếu", validator.evaluatePasswordStrength("ABC12345"));
    }
}

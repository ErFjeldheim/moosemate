package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    // Constructor test - skipped as constructor is private

    // Email validation tests
    @Test
    void testIsValidEmail_ValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("test123@test-domain.org"));
    }

    @Test
    void testIsValidEmail_InvalidEmail() {
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail("invalid"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("test@"));
        assertFalse(ValidationUtils.isValidEmail("test@.com"));
    }

    @Test
    void testValidateEmail_ValidEmail() {
        assertDoesNotThrow(() -> ValidationUtils.validateEmail("test@example.com"));
    }

    @Test
    void testValidateEmail_NullEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateEmail(null));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateEmail_EmptyEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateEmail(""));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateEmail_InvalidFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateEmail("invalid-email"));
        assertEquals("Invalid email format: example@email.com", exception.getMessage());
    }

    // Password validation tests
    @Test
    void testValidatePassword_ValidPassword() {
        assertDoesNotThrow(() -> ValidationUtils.validatePassword("password123"));
        assertDoesNotThrow(() -> ValidationUtils.validatePassword("Test1234"));
        assertDoesNotThrow(() -> ValidationUtils.validatePassword("Abcdefgh1"));
    }

    @Test
    void testValidatePassword_NullPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validatePassword(null));
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void testValidatePassword_EmptyPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validatePassword(""));
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void testValidatePassword_TooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validatePassword("pass1"));
        assertEquals("Password must be at least 8 characters long", exception.getMessage());
    }

    @Test
    void testValidatePassword_NoLetter() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validatePassword("12345678"));
        assertEquals("Password must contain at least one letter", exception.getMessage());
    }

    @Test
    void testValidatePassword_NoNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validatePassword("password"));
        assertEquals("Password must contain at least one number", exception.getMessage());
    }

    // Username validation tests
    @Test
    void testValidateUsername_ValidUsername() {
        assertDoesNotThrow(() -> ValidationUtils.validateUsername("john"));
        assertDoesNotThrow(() -> ValidationUtils.validateUsername("user123"));
        assertDoesNotThrow(() -> ValidationUtils.validateUsername("JohnDoe"));
    }

    @Test
    void testValidateUsername_NullUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateUsername(null));
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateUsername_EmptyUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateUsername(""));
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateUsername_TooLong() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateUsername("thisusernameiswaytoolongtobevalid"));
        assertEquals("Username must be less than 20 characters", exception.getMessage());
    }

    @Test
    void testValidateUsername_ContainsSpaces() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.validateUsername("john doe"));
        assertEquals("Username cannot contain spaces", exception.getMessage());
    }

    // Null/Empty validation tests
    @Test
    void testIsNullOrEmpty_Null() {
        assertTrue(ValidationUtils.isNullOrEmpty(null));
    }

    @Test
    void testIsNullOrEmpty_Empty() {
        assertTrue(ValidationUtils.isNullOrEmpty(""));
        assertTrue(ValidationUtils.isNullOrEmpty("   "));
    }

    @Test
    void testIsNullOrEmpty_NotEmpty() {
        assertFalse(ValidationUtils.isNullOrEmpty("text"));
        assertFalse(ValidationUtils.isNullOrEmpty("  text  "));
    }

    @Test
    void testAnyNullOrEmpty_NullArray() {
        assertTrue(ValidationUtils.anyNullOrEmpty((String[]) null));
    }

    @Test
    void testAnyNullOrEmpty_AllValid() {
        assertFalse(ValidationUtils.anyNullOrEmpty("a", "b", "c"));
    }

    @Test
    void testAnyNullOrEmpty_OneNull() {
        assertTrue(ValidationUtils.anyNullOrEmpty("a", null, "c"));
    }

    @Test
    void testAllNullOrEmpty_NullArray() {
        assertTrue(ValidationUtils.allNullOrEmpty((String[]) null));
    }

    @Test
    void testAllNullOrEmpty_EmptyArray() {
        assertTrue(ValidationUtils.allNullOrEmpty());
    }

    @Test
    void testAllNullOrEmpty_AllNull() {
        assertTrue(ValidationUtils.allNullOrEmpty(null, null, null));
    }

    @Test
    void testAllNullOrEmpty_OneValid() {
        assertFalse(ValidationUtils.allNullOrEmpty(null, "a", ""));
    }

    @Test
    void testAllNullOrEmpty_AllValid() {
        assertFalse(ValidationUtils.allNullOrEmpty("a", "b", "c"));
    }

    @Test
    void testRequireNonEmpty_Valid() {
        assertDoesNotThrow(() -> ValidationUtils.requireNonEmpty("value", "Field"));
    }

    @Test
    void testRequireNonEmpty_Null() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.requireNonEmpty(null, "Field"));
        assertEquals("Field cannot be empty", exception.getMessage());
    }

    @Test
    void testRequireAllNonEmpty_NullArray() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.requireAllNonEmpty((String[]) null));
        assertEquals("Values cannot be null", exception.getMessage());
    }

    @Test
    void testRequireAllNonEmpty_AllValid() {
        assertDoesNotThrow(() -> ValidationUtils.requireAllNonEmpty("a", "b", "c"));
    }

    @Test
    void testRequireAllNonEmpty_OneNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.requireAllNonEmpty("a", null, "c"));
        assertEquals("Value at index 1 cannot be empty", exception.getMessage());
    }

    @Test
    void testRequireAllNonEmpty_OneEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.requireAllNonEmpty("a", "", "c"));
        assertEquals("Value at index 1 cannot be empty", exception.getMessage());
    }
}

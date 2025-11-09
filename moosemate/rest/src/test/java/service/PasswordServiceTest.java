package service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Unit tests for PasswordService.
public class PasswordServiceTest {
    
    private PasswordService passwordService;
    
    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }
    
    @Test
    void testHashPassword() {
        String password = "testPassword123";
        String hashedPassword = passwordService.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }
    
    @Test
    void testHashPasswordWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword(null);
        });
    }
    
    @Test
    void testHashPasswordWithEmptyStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword("");
        });
    }
    
    @Test
    void testHashPasswordWithWhitespaceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword("   ");
        });
    }
    
    @Test
    void testVerifyPasswordCorrect() {
        String password = "testPassword123";
        String hashedPassword = passwordService.hashPassword(password);
        
        assertTrue(passwordService.verifyPassword(password, hashedPassword));
    }
    
    @Test
    void testVerifyPasswordIncorrect() {
        String password = "testPassword123";
        String wrongPassword = "wrongPassword";
        String hashedPassword = passwordService.hashPassword(password);
        
        assertFalse(passwordService.verifyPassword(wrongPassword, hashedPassword));
    }
    
    @Test
    void testVerifyPasswordWithNullValues() {
        assertThrows(IllegalArgumentException.class, () -> passwordService.verifyPassword(null, "hashedPassword"));
        assertThrows(NullPointerException.class, () -> passwordService.verifyPassword("password", null));
        assertThrows(IllegalArgumentException.class, () -> passwordService.verifyPassword(null, null));
    }
    
    @Test
    void testVerifyPasswordWithInvalidHash() {
        assertFalse(passwordService.verifyPassword("password", "invalidHash"));
    }
    
    @Test
    void testHashPasswordProducesUniqueHashes() {
        String password = "testPassword123";
        String hash1 = passwordService.hashPassword(password);
        String hash2 = passwordService.hashPassword(password);
        
        assertNotEquals(hash1, hash2);
        assertTrue(passwordService.verifyPassword(password, hash1));
        assertTrue(passwordService.verifyPassword(password, hash2));
    }
}

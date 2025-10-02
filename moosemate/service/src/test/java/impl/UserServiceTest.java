package impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;
import java.util.Optional;

/**
 * Unit tests for UserService.
 */
public class UserServiceTest {
    
    private UserService userService;
    private static final String TEST_DATA_FILE = "test-data.json";
    
    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        File testFile = new File(TEST_DATA_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        // Initialize test service
        userService = new TestUserService();
    }
    
    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File testFile = new File(TEST_DATA_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    /**
     * Test-specific UserService that uses a separate test data file
     */
    private static class TestUserService extends UserService {
        @Override
        protected String getDataFilePath() {
            return TEST_DATA_FILE;
        }
    }
    
    @Test
    void testCreateUser() {
        boolean result = userService.createUser("newuser", "new@example.com", "Password123");
        assertTrue(result, "Should be able to create a new user");
    }
    
    @Test
    void testCreateUserDuplicateUsername() {
        // Create first user
        userService.createUser("testuser", "test@example.com", "Password123");
        // Try to create duplicate username
        boolean result = userService.createUser("testuser", "other@example.com", "Password456");
        assertFalse(result, "Should not allow duplicate usernames");
    }
    
    @Test
    void testCreateUserDuplicateEmail() {
        // Create first user
        userService.createUser("testuser", "test@example.com", "Password123");
        // Try to create duplicate email
        boolean result = userService.createUser("testuser2", "test@example.com", "Password456");
        assertFalse(result, "Should not allow duplicate emails");
    }
    
    @Test
    void testFindByUsername() {
        // Create test user
        userService.createUser("testuser", "test@example.com", "Password123");
        Optional<Map<String, String>> userOpt = userService.findByUsernameOrEmail("testuser");
        assertTrue(userOpt.isPresent(), "Should find user by username");
        assertEquals("testuser", userOpt.get().get("username"));
    }
    
    @Test
    void testFindByEmail() {
        // Create test user
        userService.createUser("testuser", "test@example.com", "Password123");
        Optional<Map<String, String>> userOpt = userService.findByUsernameOrEmail("test@example.com");
        assertTrue(userOpt.isPresent(), "Should find user by email");
        assertEquals("test@example.com", userOpt.get().get("email"));
    }
    
    @Test
    void testUserExists() {
        // Create test user
        userService.createUser("testuser", "test@example.com", "Password123");
        assertTrue(userService.userExists("testuser"));
        assertFalse(userService.userExists("nonexistent"));
    }
    
    @Test
    void testEmailExists() {
        // Create test user
        userService.createUser("testuser", "test@example.com", "Password123");
        assertTrue(userService.emailExists("test@example.com"));
        assertFalse(userService.emailExists("nonexistent@example.com"));
    }
}
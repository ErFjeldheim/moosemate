package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import repository.UserRepository;
import java.io.File;
import java.util.Map;

/**
 * Test class for LoginService
 * Tests the LoginService that delegates to UserService and PasswordService
 */
public class LoginServiceTest {

    private LoginService loginService;
    private UserService userService;
    private PasswordService passwordService;
    private TestUserRepository userRepository;
    private final String testDataFile = "./target/test-login-data.json";

    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        cleanupTestFile();
        // Initialize services with test isolation
        userRepository = new TestUserRepository();
        userService = new UserService(userRepository);
        passwordService = new PasswordService();
        loginService = new LoginService(userService, passwordService);
    }

    @AfterEach
    public void tearDown() {
        cleanupTestFile();
    }

    /**
     * Test UserRepository that uses separate test data file
     */
    private class TestUserRepository extends UserRepository {
        @Override
        protected String getDataFilePath() {
            return testDataFile;
        }
    }

    private void cleanupTestFile() {
        File file = new File(testDataFile);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testLoginUserWithValidUsernameAndPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        boolean result = loginService.loginUser("testuser", "password123");
        assertTrue(result, "Should login successfully with valid username and password");
    }

    @Test
    public void testLoginUserWithValidEmailAndPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        boolean result = loginService.loginUser("test@example.com", "password123");
        assertTrue(result, "Should login successfully with valid email and password");
    }

    @Test
    public void testLoginUserWithInvalidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        boolean result = loginService.loginUser("testuser", "wrongpassword");
        assertFalse(result, "Should fail login with invalid password");
    }

    @Test
    public void testLoginUserWithNonExistentUser() {
        // Don't create any users - file is empty after setUp
        boolean result = loginService.loginUser("nonexistent", "password123");
        assertFalse(result, "Should fail login with non-existent user");
    }

    @Test
    public void testLoginUserWhenDataFileDoesNotExist() {
        // Don't create any users - file remains empty after setUp cleanup
        boolean result = loginService.loginUser("testuser", "password123");
        assertFalse(result, "Should fail login when no data file exists");
    }

    @Test
    public void testValidatePasswordWithValidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("mySecretPassword");
        userService.createUser("testuser2", "test2@example.com", hashedPassword);
        var userOpt = userService.findByUsernameOrEmail("testuser2");
        assertTrue(userOpt.isPresent(), "User should exist");
        
        Map<String, String> user = userOpt.get();
        boolean result = loginService.validatePassword(user, "mySecretPassword");
        assertTrue(result, "Should validate correct password");
    }

    @Test
    public void testValidatePasswordWithInvalidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("mySecretPassword");
        userService.createUser("testuser2", "test2@example.com", hashedPassword);
        var userOpt = userService.findByUsernameOrEmail("testuser2");
        assertTrue(userOpt.isPresent(), "User should exist");
        
        Map<String, String> user = userOpt.get();
        boolean result = loginService.validatePassword(user, "wrongPassword");
        assertFalse(result, "Should not validate incorrect password");
    }

    @Test
    public void testLoginWithEmptyPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        boolean result = loginService.loginUser("testuser", "");
        assertFalse(result, "Should fail login with empty password");
    }

    @Test
    public void testLoginWithEmptyUsername() {
        // No need to create users for this test - empty username should fail regardless
        boolean result = loginService.loginUser("", "password123");
        assertFalse(result, "Should fail login with empty username");
    }

    @Test
    public void testMultipleUsersLogin() {
        // Create test users for this test with hashed passwords
        String hashedPassword1 = passwordService.hashPassword("password1");
        String hashedPassword2 = passwordService.hashPassword("password2");
        userService.createUser("user1", "user1@example.com", hashedPassword1);
        userService.createUser("user2", "user2@example.com", hashedPassword2);
        
        // Test login for both users
        assertTrue(loginService.loginUser("user1", "password1"), "User1 should login successfully");
        assertTrue(loginService.loginUser("user2@example.com", "password2"), "User2 should login successfully with email");
        
        // Test cross-login failures
        assertFalse(loginService.loginUser("user1", "password2"), "User1 should not login with user2's password");
        assertFalse(loginService.loginUser("user2", "password1"), "User2 should not login with user1's password");
    }

    @Test
    public void testPasswordHashing() {
        String plainPassword = "password123";
        
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword(plainPassword);
        userService.createUser("testuser", "test@example.com", hashedPassword);
        assertTrue(loginService.loginUser("testuser", plainPassword), "Should login with original plain password");
        
        // Verify that the stored password is actually hashed (not plain text)
        var userOpt = userService.findByUsernameOrEmail("testuser");
        assertTrue(userOpt.isPresent(), "User should exist");
        
        String storedPassword = userOpt.get().get("password");
        assertNotEquals(plainPassword, storedPassword, "Stored password should be hashed, not plain text");
        assertTrue(storedPassword.startsWith("$2a$"), "Should be BCrypt hash format");
    }
}
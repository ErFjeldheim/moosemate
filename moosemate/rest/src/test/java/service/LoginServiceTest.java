package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import util.JsonFileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Test class for LoginService.
 * Tests the LoginService that delegates to UserService and PasswordService.
 */
public class LoginServiceTest {

    private LoginService loginService;
    private UserService userService;
    private PasswordService passwordService;
    private UserRepository userRepository;
    private final String testDataFile = "rest/target/test-data/test-login-data.json";

    /**
     * Test implementation of JsonFileHandler that uses a custom file path.
     */
    private static class TestJsonFileHandler extends JsonFileHandler {
        private final String testFilePath;

        TestJsonFileHandler(String testFilePath) {
            super();
            this.testFilePath = testFilePath;
        }

        @Override
        public String getDataFilePath(String relativePath) {
            return testFilePath;
        }
    }

    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        cleanupTestFile();
        // Initialize services with test isolation
        userRepository = new UserRepository(new TestJsonFileHandler(testDataFile));
        userService = new UserService(userRepository);
        passwordService = new PasswordService();
        loginService = new LoginService(userService, passwordService);
    }

    @AfterEach
    public void tearDown() {
        cleanupTestFile();
    }

    private void cleanupTestFile() {
        File file = new File(testDataFile);
        if (file.exists()) {
            file.delete();
        }
    }
    
    private void createTestDataFile() throws IOException {
        String testData = "{\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"username\": \"testuser\",\n"
                + "      \"email\": \"test@example.com\",\n"
                + "      \"password\": \"password123\",\n"
                + "      \"userID\": \"test-uuid\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        Files.write(Paths.get(testDataFile), testData.getBytes());
    }

    @Test
    public void testLoginUserWithValidUsernameAndPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        
        User user = loginService.loginUser("testuser", "password123");
        assertNotNull(user, "Should login successfully with valid username and password");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testLoginUserWithValidEmailAndPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        User user = loginService.loginUser("test@example.com", "password123");
        assertNotNull(user, "Should login successfully with valid email and password");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testLoginUserWithInvalidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        User user = loginService.loginUser("testuser", "wrongpassword");
        assertNull(user, "Should fail login with invalid password");
    }

    @Test
    public void testLoginUserWithNonExistentUser() {
        // Don't create any users - file is empty after setUp
        User user = loginService.loginUser("nonexistent", "password123");
        assertNull(user, "Should fail login with non-existent user");
    }

    @Test
    public void testLoginUserWhenDataFileDoesNotExist() {
        // Don't create any users - file remains empty after setUp cleanup
        User user = loginService.loginUser("testuser", "password123");
        assertNull(user, "Should fail login when no data file exists");
    }

    @Test
    public void testValidatePasswordWithValidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("mySecretPassword");
        userService.createUser("testuser2", "test2@example.com", hashedPassword);
        
        // Test password validation via login
        User user = loginService.loginUser("testuser2", "mySecretPassword");
        assertNotNull(user, "Should login successfully with correct password");
        assertEquals("testuser2", user.getUsername());
    }

    @Test
    public void testValidatePasswordWithInvalidPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("mySecretPassword");
        userService.createUser("testuser2", "test2@example.com", hashedPassword);
        
        // Test password validation via login - should fail with wrong password
        User user = loginService.loginUser("testuser2", "wrongPassword");
        assertNull(user, "Should not login with incorrect password");
    }

    @Test
    public void testLoginWithEmptyPassword() {
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("testuser", "test@example.com", hashedPassword);
        
        // LoginService now throws IllegalArgumentException for empty password
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("testuser", "");
        }, "Should throw exception for empty password");
    }

    @Test
    public void testLoginWithEmptyUsername() {
        // No need to create users for this test - empty username should fail regardless
        // LoginService now throws IllegalArgumentException for empty username
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("", "password123");
        }, "Should throw exception for empty username");
    }

    @Test
    public void testMultipleUsersLogin() {
        // Create test users for this test with hashed passwords
        String hashedPassword1 = passwordService.hashPassword("password1");
        String hashedPassword2 = passwordService.hashPassword("password2");
        userService.createUser("user1", "user1@example.com", hashedPassword1);
        userService.createUser("user2", "user2@example.com", hashedPassword2);
        
        // Test login for both users
        assertNotNull(loginService.loginUser("user1", "password1"),
                "User1 should login successfully");
        assertNotNull(loginService.loginUser("user2@example.com", "password2"),
                "User2 should login successfully with email");
        
        // Test cross-login failures
        assertNull(loginService.loginUser("user1", "password2"),
                "User1 should not login with user2's password");
        assertNull(loginService.loginUser("user2", "password1"),
                "User2 should not login with user1's password");
    }

    @Test
    public void testPasswordHashing() {
        String plainPassword = "password123";
        
        // Create test user for this test with hashed password
        String hashedPassword = passwordService.hashPassword(plainPassword);
        userService.createUser("testuser", "test@example.com", hashedPassword);
        
        User user = loginService.loginUser("testuser", plainPassword);
        assertNotNull(user, "Should login with original plain password");
        
        // Verify that the stored password is actually hashed (not plain text)
        User storedUser = userService.findByUsernameOrEmail("testuser");
        assertNotNull(storedUser, "User should exist");
        
        String storedPassword = storedUser.getPassword();
        assertNotEquals(plainPassword, storedPassword, "Stored password should be hashed, not plain text");
        assertTrue(storedPassword.startsWith("$2a$"), "Should be BCrypt hash format");
    }

    @Test
    public void testLoginUserWithEmptyCredentials() throws IOException {
        createTestDataFile();
        
        // LoginService now throws IllegalArgumentException for empty credentials
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("", "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("testuser", "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("", "");
        });
    }

    @Test
    public void testLoginUserWithNullCredentials() throws IOException {
        createTestDataFile();
        
        // LoginService now throws IllegalArgumentException for null credentials
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser(null, "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("testuser", null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser(null, null);
        });
    }

    @Test
    public void testLoginUserWithWhitespaceCredentials() throws IOException {
        createTestDataFile();
        
        // LoginService now throws IllegalArgumentException for whitespace credentials
        // (whitespace is considered empty after trim)
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("   ", "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("testuser", "   ");
        });
    }

    @Test
    public void testLoginUserCaseSensitivity() throws IOException {
        createTestDataFile();
        
        // Test case sensitivity for username - should fail
        User user1 = loginService.loginUser("TESTUSER", "password123");
        assertNull(user1, "Username should be case-sensitive");
        
        // Test case sensitivity for email - should fail
        User user2 = loginService.loginUser("TEST@EXAMPLE.COM", "password123");
        assertNull(user2, "Email should be case-sensitive");
        
        // Test case sensitivity for password - should fail
        User user3 = loginService.loginUser("testuser", "PASSWORD123");
        assertNull(user3, "Password should be case-sensitive");
    }

    @Test
    public void testUserExistsWithEmptyAndNullUsername() throws IOException {
        createTestDataFile();
        
        // Test with empty username
        boolean result1 = userService.userExists("");
        assertFalse(result1);
        
        // Test with null username
        boolean result2 = userService.userExists(null);
        assertFalse(result2);
    }



    @Test
    public void testLoginServiceCanBeInstantiatedMultipleTimes() {
        LoginService service1 = new LoginService(userService, passwordService);
        LoginService service2 = new LoginService(userService, passwordService);
        
        assertNotNull(service1);
        assertNotNull(service2);
        assertNotSame(service1, service2);
    }

    @Test
    public void testLoginUserWithSpecialCharacters() throws IOException {
        // Create user with special characters - hash the password first
        String hashedPassword = passwordService.hashPassword("pass@123!");
        userService.createUser("user@#$%", "special@test.com", hashedPassword);
        
        User user1 = loginService.loginUser("user@#$%", "pass@123!");
        assertNotNull(user1, "Should login with special characters in username");
        
        User user2 = loginService.loginUser("special@test.com", "pass@123!");
        assertNotNull(user2, "Should login with special characters in email");
    }

    @Test
    public void testUserExistsWithSpecialCharacters() throws IOException {
        String testDataWithSpecialChars = "{\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"username\": \"user@#$%\",\n"
                + "      \"email\": \"special@test.com\",\n"
                + "      \"password\": \"pass@123!\",\n"
                + "      \"userID\": \"special-uuid\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        Files.write(Paths.get(testDataFile), testDataWithSpecialChars.getBytes());
        
        boolean result = userService.userExists("user@#$%");
        assertTrue(result);
    }

    @Test
    public void testLoginUserWithIncompleteUserData() throws IOException {
        String incompleteData = "{\n"
                + "  \"users\": [\n"
                + "    {\n"
                + "      \"username\": \"incompleteuser\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"email\": \"incomplete@test.com\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"password\": \"password123\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        Files.write(Paths.get(testDataFile), incompleteData.getBytes());
        
        User user1 = loginService.loginUser("incompleteuser", "password123");
        assertNull(user1, "Should fail login with incomplete user data");
        
        User user2 = loginService.loginUser("incomplete@test.com", "password123");
        assertNull(user2, "Should fail login with incomplete user data");
    }

    @Test
    public void testLoginUserFindsCorrectUserInMultipleUsersDataset() throws IOException {
        // Create a small dataset with 5 users to test search functionality efficiently
        // Hash passwords before creating users
        for (int i = 0; i < 5; i++) {
            String hashedPassword = passwordService.hashPassword("password" + i);
            userService.createUser("user" + i, "user" + i + "@test.com", hashedPassword);
        }
        
        // Test finding user at the beginning
        User user1 = loginService.loginUser("user0", "password0");
        assertNotNull(user1, "Should find user at beginning");
        
        // Test finding user in the middle
        User user2 = loginService.loginUser("user2", "password2");
        assertNotNull(user2, "Should find user in middle");
        
        // Test finding user at the end
        User user3 = loginService.loginUser("user4", "password4");
        assertNotNull(user3, "Should find user at end");
        
        // Test non-existent user
        User user4 = loginService.loginUser("user5", "password5");
        assertNull(user4, "Should not find non-existent user");
    }







    @Test
    public void testSuccessfulLoginPath() {
        // Create test user with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("successuser", "success@example.com", hashedPassword);
        
        // Test successful login with username
        User user1 = loginService.loginUser("successuser", "password123");
        assertNotNull(user1, "Should successfully login with correct username and password");
        
        // Test successful login with email
        User user2 = loginService.loginUser("success@example.com", "password123");
        assertNotNull(user2, "Should successfully login with correct email and password");
    }

    @Test
    public void testUserNotFoundScenario() {
        // Test with completely non-existent user
        User user = loginService.loginUser("nonexistentuser123", "anypassword");
        assertNull(user, "Should return null for non-existent user");
    }

    @Test
    public void testLoginServiceExceptionHandling() {
        // Test error handling with malformed inputs
        // Empty strings now throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("", "password123");
        }, "Should throw exception for empty username");
        
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.loginUser("testuser", "");
        }, "Should throw exception for empty password");
    }

    @Test
    public void testRealLoginServiceWithNullInputs() {
        // Test the actual LoginService class directly
        LoginService realLoginService = new LoginService(userService, passwordService);
        
        // LoginService now throws IllegalArgumentException for null inputs
        assertThrows(IllegalArgumentException.class, () -> {
            realLoginService.loginUser(null, "password123");
        }, "Should throw exception for null username");
        
        assertThrows(IllegalArgumentException.class, () -> {
            realLoginService.loginUser("testuser", null);
        }, "Should throw exception for null password");
        
        assertThrows(IllegalArgumentException.class, () -> {
            realLoginService.loginUser(null, null);
        }, "Should throw exception for both null");
    }

    @Test
    public void testRealLoginServiceUserNotFound() {
        // Test the actual LoginService class directly
        LoginService realLoginService = new LoginService(userService, passwordService);
        
        // Test with non-existent user (should trigger user not found path)
        User user = realLoginService.loginUser("nonexistentuser123456", "password123");
        assertNull(user, "Should return null for non-existent user");
    }

    @Test
    public void testRealLoginServiceSuccessfulLogin() {
        // Create a user using the test repository with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("realuser", "real@example.com", hashedPassword);
        
        // Test login using the same test repository setup
        // Use our test loginService that uses the test repository
        User user = loginService.loginUser("realuser", "password123");
        assertNotNull(user, "Should return user for successful login");
        assertEquals("realuser", user.getUsername());
    }


}

package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private final String testDataFile = "rest/target/test-data/test-login-data.json";

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
    
    private void createTestDataFile() throws IOException {
        String testData = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"username\": \"testuser\",\n" +
                "      \"email\": \"test@example.com\",\n" +
                "      \"password\": \"password123\",\n" +
                "      \"userID\": \"test-uuid\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Files.write(Paths.get(testDataFile), testData.getBytes());
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

    @Test
    public void testLoginUserWithEmptyCredentials() throws IOException {
        createTestDataFile();
        
        // Test with empty username
        boolean result1 = loginService.loginUser("", "password123");
        assertFalse(result1);
        
        // Test with empty password
        boolean result2 = loginService.loginUser("testuser", "");
        assertFalse(result2);
        
        // Test with both empty
        boolean result3 = loginService.loginUser("", "");
        assertFalse(result3);
    }

    @Test
    public void testLoginUserWithNullCredentials() throws IOException {
        createTestDataFile();
        
        // Test with null username
        boolean result1 = loginService.loginUser(null, "password123");
        assertFalse(result1);
        
        // Test with null password  
        boolean result2 = loginService.loginUser("testuser", null);
        assertFalse(result2);
        
        // Test with both null
        boolean result3 = loginService.loginUser(null, null);
        assertFalse(result3);
    }

    @Test
    public void testLoginUserWithWhitespaceCredentials() throws IOException {
        createTestDataFile();
        
        // Test with whitespace username
        boolean result1 = loginService.loginUser("   ", "password123");
        assertFalse(result1);
        
        // Test with whitespace password
        boolean result2 = loginService.loginUser("testuser", "   ");
        assertFalse(result2);
    }

    @Test
    public void testLoginUserCaseSensitivity() throws IOException {
        createTestDataFile();
        
        // Test case sensitivity for username
        boolean result1 = loginService.loginUser("TESTUSER", "password123");
        assertFalse(result1);
        
        // Test case sensitivity for email
        boolean result2 = loginService.loginUser("TEST@EXAMPLE.COM", "password123");
        assertFalse(result2);
        
        // Test case sensitivity for password
        boolean result3 = loginService.loginUser("testuser", "PASSWORD123");
        assertFalse(result3);
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
        LoginService service1 = new LoginService();
        LoginService service2 = new LoginService();
        
        assertNotNull(service1);
        assertNotNull(service2);
        assertNotSame(service1, service2);
    }

    @Test
    public void testLoginUserWithSpecialCharacters() throws IOException {
        // Create user with special characters - hash the password first
        String hashedPassword = passwordService.hashPassword("pass@123!");
        userService.createUser("user@#$%", "special@test.com", hashedPassword);
        
        boolean result1 = loginService.loginUser("user@#$%", "pass@123!");
        assertTrue(result1);
        
        boolean result2 = loginService.loginUser("special@test.com", "pass@123!");
        assertTrue(result2);
    }

    @Test
    public void testUserExistsWithSpecialCharacters() throws IOException {
        String testDataWithSpecialChars = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"username\": \"user@#$%\",\n" +
                "      \"email\": \"special@test.com\",\n" +
                "      \"password\": \"pass@123!\",\n" +
                "      \"userID\": \"special-uuid\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Files.write(Paths.get(testDataFile), testDataWithSpecialChars.getBytes());
        
        boolean result = userService.userExists("user@#$%");
        assertTrue(result);
    }

    @Test
    public void testLoginUserWithIncompleteUserData() throws IOException {
        String incompleteData = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"username\": \"incompleteuser\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"email\": \"incomplete@test.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"password\": \"password123\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Files.write(Paths.get(testDataFile), incompleteData.getBytes());
        
        boolean result1 = loginService.loginUser("incompleteuser", "password123");
        assertFalse(result1);
        
        boolean result2 = loginService.loginUser("incomplete@test.com", "password123");
        assertFalse(result2);
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
        boolean result1 = loginService.loginUser("user0", "password0");
        assertTrue(result1);
        
        // Test finding user in the middle
        boolean result2 = loginService.loginUser("user2", "password2");
        assertTrue(result2);
        
        // Test finding user at the end
        boolean result3 = loginService.loginUser("user4", "password4");
        assertTrue(result3);
        
        // Test non-existent user
        boolean result4 = loginService.loginUser("user5", "password5");
        assertFalse(result4);
    }







    @Test
    public void testSuccessfulLoginPath() {
        // Create test user with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("successuser", "success@example.com", hashedPassword);
        
        // Test successful login with username
        boolean result1 = loginService.loginUser("successuser", "password123");
        assertTrue(result1, "Should successfully login with correct username and password");
        
        // Test successful login with email
        boolean result2 = loginService.loginUser("success@example.com", "password123");
        assertTrue(result2, "Should successfully login with correct email and password");
    }

    @Test
    public void testUserNotFoundScenario() {
        // Test with completely non-existent user
        boolean result = loginService.loginUser("nonexistentuser123", "anypassword");
        assertFalse(result, "Should return false for non-existent user");
    }

    @Test
    public void testLoginServiceExceptionHandling() {
        // Test error handling with malformed inputs
        // Empty strings should be handled gracefully
        boolean result1 = loginService.loginUser("", "password123");
        assertFalse(result1, "Should handle empty username gracefully");
        
        boolean result2 = loginService.loginUser("testuser", "");
        assertFalse(result2, "Should handle empty password gracefully");
    }

    @Test
    public void testRealLoginServiceWithNullInputs() {
        // Test the actual LoginService class directly
        LoginService realLoginService = new LoginService();
        
        // Test null username
        boolean result1 = realLoginService.loginUser(null, "password123");
        assertFalse(result1, "Should return false for null username");
        
        // Test null password
        boolean result2 = realLoginService.loginUser("testuser", null);
        assertFalse(result2, "Should return false for null password");
        
        // Test both null
        boolean result3 = realLoginService.loginUser(null, null);
        assertFalse(result3, "Should return false for both null");
    }

    @Test
    public void testRealLoginServiceUserNotFound() {
        // Test the actual LoginService class directly
        LoginService realLoginService = new LoginService();
        
        // Test with non-existent user (should trigger user not found path)
        boolean result = realLoginService.loginUser("nonexistentuser123456", "password123");
        assertFalse(result, "Should return false for non-existent user");
    }

    @Test
    public void testRealLoginServiceSuccessfulLogin() {
        // Create a user using the test repository with hashed password
        String hashedPassword = passwordService.hashPassword("password123");
        userService.createUser("realuser", "real@example.com", hashedPassword);
        
        // Test login using the same test repository setup
        // Use our test loginService that uses the test repository
        boolean result = loginService.loginUser("realuser", "password123");
        assertTrue(result, "Should return true for successful login");
    }


}
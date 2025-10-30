package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import repository.UserRepository;
import util.JsonFileHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test class for SignUpService.
 * Tests the SignUpService that delegates to UserService for user creation.
 */
public class SignUpServiceTest {

    private SignUpService signUpService;
    private UserService userService;
    private PasswordService passwordService;
    private final String testDataFile = "rest/target/test-data/test-signup-data.json";

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
        // Initialize services with test repository
        UserRepository testRepository = new UserRepository(new TestJsonFileHandler(testDataFile));
        userService = new UserService(testRepository);
        passwordService = new PasswordService();
        signUpService = new SignUpService(userService, passwordService);
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
        String testData = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"username\": \"existinguser\",\n" +
                "      \"email\": \"existing@example.com\",\n" +
                "      \"password\": \"password123\",\n" +
                "      \"userID\": \"existing-uuid\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Files.write(Paths.get(testDataFile), testData.getBytes());
    }

    @Test
    public void testSignUpUserWithValidData() {
        boolean result = signUpService.signUpUser("newuser", "new@example.com", "password123");
        assertTrue(result, "Should be able to create a new user");
    }

    @Test
    public void testSignUpUserWithExistingUsername() {
        // Create first user
        signUpService.signUpUser("existinguser", "first@example.com", "password123");
        
        // Try to create another user with same username should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("existinguser", "new@example.com", "password123");
        }, "Should throw exception for duplicate username");
    }

    @Test
    public void testSignUpUserWithExistingEmail() {
        // Reuse the existing user from the previous test to avoid creating another user
        // The test isolation ensures this doesn't interfere with other tests
        signUpService.signUpUser("firstuser", "existing@example.com", "password123");
        
        // Try to create another user with same email should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("newuser", "existing@example.com", "password123");
        }, "Should throw exception for duplicate email");
    }

    @Test
    public void testSignUpUserWithNullUsername() {
        // Test that validation happens and exceptions are re-thrown
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, "test@example.com", "password123");
        }, "Should throw exception for null username");
    }

    @Test
    public void testSignUpUserWithNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", null, "password123");
        }, "Should throw exception for null email");
    }

    @Test
    public void testSignUpUserWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", "test@example.com", null);
        }, "Should throw exception for null password");
    }

    @Test
    public void testSignUpUserMultiple() {
        // Create minimal number of users for testing (only 2)
        boolean result1 = signUpService.signUpUser("user1", "user1@example.com", "password1");
        boolean result2 = signUpService.signUpUser("user2", "user2@example.com", "password2");
        
        assertTrue(result1, "Should create first user successfully");
        assertTrue(result2, "Should create second user successfully");
    }

    @Test
    public void testPasswordHashingDuringSignUp() {
        String plainPassword = "myPlainTextPassword123";
        
        // Create user with plain password - the service should hash it
        assertTrue(signUpService.signUpUser("hashtest", "hash@example.com", plainPassword), 
                  "Should create user successfully");
        
        // The password should be hashed in storage (we can't directly test this through SignUpService
        // as it doesn't expose user lookup methods, but this test ensures the signup process works)
    }

    @Test
    public void testSignUpUserWithNullParameters() {
        // Test null parameter handling
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, "test@example.com", "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", null, "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", "test@example.com", null);
        });
    }

    @Test
    public void testSignUpUserCreatesValidUUID() {
        boolean result = signUpService.signUpUser("uuidtest", "uuid@example.com", "password123");
        assertTrue(result);
        
        // We can't directly test the UUID generation, but we can test that the user was created
        assertTrue(userService.userExists("uuidtest"));
    }



    @Test
    public void testUserExistsWithEmptyAndNullInput() throws IOException {
        createTestDataFile();
        
        assertFalse(userService.userExists(""));
        assertFalse(userService.userExists(null));
    }

    @Test
    public void testEmailExistsWithEmptyAndNullInput() throws IOException {
        createTestDataFile();
        
        assertFalse(userService.emailExists(""));
        assertFalse(userService.emailExists(null));
    }

    @Test
    public void testUserExistsWhenDataFileDoesNotExist() {
        cleanupTestFile();
        
        assertFalse(userService.userExists("testuser"));
    }

    @Test
    public void testEmailExistsWhenDataFileDoesNotExist() {
        cleanupTestFile();
        
        assertFalse(userService.emailExists("test@example.com"));
    }

    @Test
    public void testSignUpUserWithSpecialCharacters() {
        boolean result = signUpService.signUpUser("user@#$%", "special@example.com", "pass@123!");
        assertTrue(result);
        
        assertTrue(userService.userExists("user@#$%"));
        assertTrue(userService.emailExists("special@example.com"));
    }

    @Test
    public void testSignUpMultipleUsers() {
        // Test adding multiple users
        assertTrue(signUpService.signUpUser("user1", "user1@example.com", "password123"));
        assertTrue(signUpService.signUpUser("user2", "user2@example.com", "password456"));
        assertTrue(signUpService.signUpUser("user3", "user3@example.com", "password789"));
    }

    @Test
    public void testSignUpUserCaseSensitivity() {
        // Sign up user with lowercase
        assertTrue(signUpService.signUpUser("testuser", "test@example.com", "password123"));
        
        // Try to sign up with uppercase (should be treated as different user)
        assertTrue(signUpService.signUpUser("TESTUSER", "TEST@EXAMPLE.COM", "password123"));
        
        // Verify both exist as separate users
        assertTrue(userService.userExists("testuser"));
        assertTrue(userService.userExists("TESTUSER"));
        assertTrue(userService.emailExists("test@example.com"));
        assertTrue(userService.emailExists("TEST@EXAMPLE.COM"));
    }



    @Test
    public void testUserExistsWithCaseSensitivity() throws IOException {
        createTestDataFile();
        
        assertTrue(userService.userExists("existinguser"));
        assertFalse(userService.userExists("EXISTINGUSER"));
        assertFalse(userService.userExists("ExistingUser"));
    }

    @Test
    public void testEmailExistsWithCaseSensitivity() throws IOException {
        createTestDataFile();
        
        assertTrue(userService.emailExists("existing@example.com"));
        assertFalse(userService.emailExists("EXISTING@EXAMPLE.COM"));
        assertFalse(userService.emailExists("Existing@Example.com"));
    }

    @Test
    public void testSignUpServiceInstanceCanBeCreatedMultipleTimes() {
        // Create test services with test repository to avoid writing to production data
        UserRepository testRepo1 = new UserRepository(new TestJsonFileHandler(testDataFile));
        UserRepository testRepo2 = new UserRepository(new TestJsonFileHandler(testDataFile));
        UserService userService1 = new UserService(testRepo1);
        UserService userService2 = new UserService(testRepo2);
        PasswordService passwordService = new PasswordService();
        SignUpService service1 = new SignUpService(userService1, passwordService);
        SignUpService service2 = new SignUpService(userService2, passwordService);
        
        assertNotNull(service1);
        assertNotNull(service2);
        assertNotSame(service1, service2);
        
        // Both should work independently - use timestamp to ensure unique usernames
        long timestamp = System.currentTimeMillis();
        assertTrue(service1.signUpUser("uniqueUser1_" + timestamp, "unique1_" + timestamp + "@example.com", "password123"));
        assertTrue(service2.signUpUser("uniqueUser2_" + timestamp, "unique2_" + timestamp + "@example.com", "password456"));
    }





    @Test
    public void testSignUpUserWithNullValidation() {
        // Test all null combinations to cover null validation branches
        
        // Test null username - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, "test@example.com", "password123");
        }, "Should throw exception for null username");
        
        // Test null email - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", null, "password123");
        }, "Should throw exception for null email");
        
        // Test null password - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", "test@example.com", null);
        }, "Should throw exception for null password");
        
        // Test all null - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, null, null);
        }, "Should throw exception for all null parameters");
    }

    @Test
    public void testSignUpUserValidationErrorPaths() {
        // Test various validation error paths that might not be covered
        
        // Test multiple null combinations to ensure all branches are hit
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, "test@example.com", null);
        }, "Should throw exception for null username and password");
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", null, null);
        }, "Should throw exception for null email and password");
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser(null, null, "password123");
        }, "Should throw exception for null username and email");
    }

    @Test
    public void testSignUpUserFailureScenarios() throws IOException {
        createTestDataFile();
        
        // Test scenarios where signUpService throws exceptions for existing users
        
        // The test data file already has "existinguser" and "existing@example.com"
        // Try to create user with existing username - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("existinguser", "different@example.com", "password456");
        }, "Should throw exception when username already exists");
        
        // Try to create user with existing email - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("newuser", "existing@example.com", "password789");
        }, "Should throw exception when email already exists");
    }

    @Test
    public void testRealSignUpServiceNullValidation() {
        // Test the actual SignUpService class directly (not TestSignUpService)
        SignUpService realSignUpService = new SignUpService(userService, passwordService);
        
        // Test null username - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            realSignUpService.signUpUser(null, "test@example.com", "password123");
        }, "Should throw exception for null username");
        
        // Test null email - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            realSignUpService.signUpUser("testuser", null, "password123");
        }, "Should throw exception for null email");
        
        // Test null password - should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            realSignUpService.signUpUser("testuser", "test@example.com", null);
        }, "Should throw exception for null password");
    }
}

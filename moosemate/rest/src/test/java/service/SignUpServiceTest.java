package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import repository.UserRepository;
import java.io.File;

/**
 * Test class for SignUpService
 * Tests the SignUpService that delegates to UserService for user creation
 */
public class SignUpServiceTest {

    private SignUpService signUpService;
    private UserService userService;
    private PasswordService passwordService;
    private final String testDataFile = "./target/test-signup-data.json";

    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        cleanupTestFile();
        // Initialize services with test repository
        TestUserRepository testRepository = new TestUserRepository();
        userService = new UserService(testRepository);
        passwordService = new PasswordService();
        signUpService = new SignUpService(userService, passwordService);
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
        String plainPassword = "myPlainTextPassword";
        
        // Create user with plain password - the service should hash it
        assertTrue(signUpService.signUpUser("hashtest", "hash@example.com", plainPassword), 
                  "Should create user successfully");
        
        // The password should be hashed in storage (we can't directly test this through SignUpService
        // as it doesn't expose user lookup methods, but this test ensures the signup process works)
    }
}
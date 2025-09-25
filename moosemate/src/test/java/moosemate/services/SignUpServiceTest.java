package moosemate.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import moosemate.core.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Test class for SignUpService
 * Credits: Claude Sonnet 4
 */
public class SignUpServiceTest {

    private SignUpService signUpService;
    private final String testDataFile = "data.json";
    private String testUserID;

    @BeforeEach
    public void setUp() {
        testUserID = UUID.randomUUID().toString();
        // Clean up any existing test data
        cleanupTestFile();
        // Initialize fresh SignUpService which will create the data file
        signUpService = new SignUpService();
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
        String testData = """
            {
              "users": [
                {
                  "username": "existinguser",
                  "email": "existing@example.com",
                  "password": "password123",
                  "userID": "existing-uuid-123"
                }
              ]
            }
            """;
        Files.write(Paths.get(testDataFile), testData.getBytes());
    }

    @Test
    public void testSignUpUserWithValidData() {
        boolean result = signUpService.signUpUser("newuser", "new@example.com", "password123");
        assertTrue(result);
        
        // Verify user was added
        assertTrue(signUpService.userExists("newuser"));
        assertTrue(signUpService.emailExists("new@example.com"));
    }

    @Test
    public void testSignUpUserWithUserObject() {
        User newUser = new User("objectuser", "object@example.com", "password123", testUserID);
        
        boolean result = signUpService.signUpUser(newUser);
        assertTrue(result);
        
        // Verify user was added
        assertTrue(signUpService.userExists("objectuser"));
    }

    @Test
    public void testSignUpUserWithExistingUsername() throws IOException {
        createTestDataFile();
        
        boolean result = signUpService.signUpUser("existinguser", "new@example.com", "password123");
        assertFalse(result);
    }

    @Test
    public void testSignUpUserWithExistingEmail() throws IOException {
        createTestDataFile();
        
        boolean result = signUpService.signUpUser("newuser", "existing@example.com", "password123");
        assertFalse(result);
    }

    @Test
    public void testGetAllUsernames() throws IOException {
        createTestDataFile();
        
        List<String> usernames = signUpService.getAllUsernames();
        assertEquals(1, usernames.size());
        assertTrue(usernames.contains("existinguser"));
    }

    @Test
    public void testGetAllUsernamesWhenEmpty() {
        List<String> usernames = signUpService.getAllUsernames();
        assertTrue(usernames.isEmpty());
    }

    @Test
    public void testGetAllUsernamesWithMalformedJson() throws IOException {
        String malformedJson = "{ invalid json }";
        Files.write(Paths.get(testDataFile), malformedJson.getBytes());
        
        List<String> usernames = signUpService.getAllUsernames();
        assertTrue(usernames.isEmpty());
    }

    @Test
    public void testUserExistsTrue() throws IOException {
        createTestDataFile();
        
        assertTrue(signUpService.userExists("existinguser"));
    }

    @Test
    public void testUserExistsFalse() throws IOException {
        createTestDataFile();
        
        assertFalse(signUpService.userExists("nonexistent"));
    }

    @Test
    public void testUserExistsWithMalformedJson() throws IOException {
        String malformedJson = "{ invalid json }";
        Files.write(Paths.get(testDataFile), malformedJson.getBytes());
        
        assertFalse(signUpService.userExists("testuser"));
    }

    @Test
    public void testEmailExistsTrue() throws IOException {
        createTestDataFile();
        
        assertTrue(signUpService.emailExists("existing@example.com"));
    }

    @Test
    public void testEmailExistsFalse() throws IOException {
        createTestDataFile();
        
        assertFalse(signUpService.emailExists("nonexistent@example.com"));
    }

    @Test
    public void testEmailExistsWithMalformedJson() throws IOException {
        String malformedJson = "{ invalid json }";
        Files.write(Paths.get(testDataFile), malformedJson.getBytes());
        
        assertFalse(signUpService.emailExists("test@example.com"));
    }

    @Test
    public void testInitializeDataFileCreatesFile() {
        // Constructor should create file if it doesn't exist
        cleanupTestFile();
        
        // Create new instance to trigger initialization
        new SignUpService();
        
        File file = new File(testDataFile);
        assertTrue(file.exists());
    }

    @Test
    public void testInitializeDataFileHandlesExistingFile() throws IOException {
        // Test that initialization works when file already exists with content
        createTestDataFile();
        
        // Create new instance - should not overwrite existing file
        SignUpService newService = new SignUpService();
        
        // Verify existing data is preserved
        assertTrue(newService.userExists("existinguser"));
    }

    @Test
    public void testInitializeDataFileHandlesEmptyExistingFile() throws IOException {
        // Create an empty file
        File file = new File(testDataFile);
        file.createNewFile();
        
        // Create new instance - should initialize empty file with proper structure
        SignUpService newService = new SignUpService();
        
        // Verify file exists and has proper structure
        assertTrue(file.exists());
        List<String> usernames = newService.getAllUsernames();
        assertTrue(usernames.isEmpty());
    }

    @Test
    public void testSignUpUserHandlesIOExceptionDuringFileWrite() throws IOException {
        // Create a directory with the same name as our data file to cause IOException
        File directory = new File(testDataFile);
        if (directory.exists()) {
            directory.delete();
        }
        directory.mkdir(); // Create a directory instead of a file
        
        try {
            // This should handle the IOException gracefully
            SignUpService problematicService = new SignUpService();
            boolean result = problematicService.signUpUser("testuser", "test@example.com", "password123");
            assertFalse(result); // Should return false due to IOException
        } finally {
            // Clean up the directory
            directory.delete();
        }
    }

    @Test
    public void testSignUpUserObjectWithInvalidUserReturnsExceptionHandling() {
        User validUser = new User("testuser", "test@example.com", "password123", testUserID);
        
        boolean result = signUpService.signUpUser(validUser);
        assertTrue(result);
    }

    @Test
    public void testSignUpUserObjectHandlesExceptionFromInvalidUser() {
        // Create a user that will cause an exception in the signUpUser(String, String, String) method
        // by using invalid data that passes User constructor but fails later
        User userWithInvalidData = new User();
        userWithInvalidData.setUsername("validuser");
        userWithInvalidData.setEmail("valid@example.com");
        userWithInvalidData.setPassword("password123");
        
        // Mock a scenario where the underlying signUpUser method might throw an exception
        // by first creating a user with the same username
        signUpService.signUpUser("validuser", "first@example.com", "password123");
        
        // Now try to sign up with User object that has same username
        boolean result = signUpService.signUpUser(userWithInvalidData);
        assertFalse(result); // Should return false due to existing username
    }

    @Test
    public void testSignUpUserObjectWithNullValues() {
        // Test User object with null values (should trigger exceptions)
        User userWithNulls = new User();
        
        boolean result = signUpService.signUpUser(userWithNulls);
        assertFalse(result); // Should return false due to exception handling
    }

    // Test validation through SignUpService - these should throw exceptions
    @Test
    public void testSignUpValidationThrowsExceptionForInvalidData() {
        // Test that validation happens and exceptions are re-thrown
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("", "test@example.com", "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", "", "password123");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            signUpService.signUpUser("testuser", "test@example.com", "");
        });
    }
}
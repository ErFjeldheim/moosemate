package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.JsonFileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

//Test class for UserRepository.
// Tests persistence layer operations for user data.
class UserRepositoryTest {

    private UserRepository repository;
    private Path testDataFile;

    /**
     * Test implementation of JsonFileHandler that uses a custom file path.
     */
    private static class TestJsonFileHandler extends JsonFileHandler {
        private final String customPath;

        TestJsonFileHandler(String customPath) {
            super();
            this.customPath = customPath;
        }

        @Override
        public String getDataFilePath(String relativePath) {
            return customPath;
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file for test data
        testDataFile = Files.createTempFile("test-data-", ".json");
        repository = new UserRepository(new TestJsonFileHandler(testDataFile.toString()));
    }

    @AfterEach
    void deleteTestData() throws IOException {
        // Clean up test file after each test
        if (testDataFile != null && Files.exists(testDataFile)) {
            Files.delete(testDataFile);
        }
    }

    @Test
    void testCreateUser_Success() {
        boolean result = repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        assertTrue(result, "User creation should succeed");
        assertTrue(repository.userExists("testuser"), "User should exist after creation");
        assertTrue(repository.emailExists("test@example.com"), "Email should exist after creation");
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        boolean result = repository.createUser("testuser", "different@example.com", "hashedPassword456");
        
        assertFalse(result, "Creating user with duplicate username should fail");
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        boolean result = repository.createUser("differentuser", "test@example.com", "hashedPassword456");
        
        assertFalse(result, "Creating user with duplicate email should fail");
    }

    @Test
    void testFindByUsernameOrEmail_ByUsername() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        Optional<Map<String, String>> result = repository.findByUsernameOrEmail("testuser");
        
        assertTrue(result.isPresent(), "User should be found by username");
        assertEquals("testuser", result.get().get("username"));
        assertEquals("test@example.com", result.get().get("email"));
    }

    @Test
    void testFindByUsernameOrEmail_ByEmail() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        Optional<Map<String, String>> result = repository.findByUsernameOrEmail("test@example.com");
        
        assertTrue(result.isPresent(), "User should be found by email");
        assertEquals("testuser", result.get().get("username"));
        assertEquals("test@example.com", result.get().get("email"));
    }

    @Test
    void testFindByUsernameOrEmail_NotFound() {
        Optional<Map<String, String>> result = repository.findByUsernameOrEmail("nonexistent");
        
        assertFalse(result.isPresent(), "Non-existent user should not be found");
    }

    @Test
    void testUserExists_ExistingUser() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        assertTrue(repository.userExists("testuser"), "Existing user should return true");
    }

    @Test
    void testUserExists_NonExistingUser() {
        assertFalse(repository.userExists("nonexistent"), "Non-existing user should return false");
    }

    @Test
    void testEmailExists_ExistingEmail() {
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        assertTrue(repository.emailExists("test@example.com"), "Existing email should return true");
    }

    @Test
    void testEmailExists_NonExistingEmail() {
        assertFalse(repository.emailExists("nonexistent@example.com"), "Non-existing email should return false");
    }

    @Test
    void testMultipleUsers_AllOperations() {
        // Create multiple users
        repository.createUser("user1", "user1@example.com", "password1");
        repository.createUser("user2", "user2@example.com", "password2");
        repository.createUser("user3", "user3@example.com", "password3");
        
        // Verify all exist
        assertTrue(repository.userExists("user1"));
        assertTrue(repository.userExists("user2"));
        assertTrue(repository.userExists("user3"));
        
        assertTrue(repository.emailExists("user1@example.com"));
        assertTrue(repository.emailExists("user2@example.com"));
        assertTrue(repository.emailExists("user3@example.com"));
        
        // Verify correct retrieval
        Optional<Map<String, String>> user1 = repository.findByUsernameOrEmail("user1");
        assertTrue(user1.isPresent());
        assertEquals("user1@example.com", user1.get().get("email"));
        
        Optional<Map<String, String>> user2 = repository.findByUsernameOrEmail("user2@example.com");
        assertTrue(user2.isPresent());
        assertEquals("user2", user2.get().get("username"));
    }

    @Test
    void testCreateUser_GeneratesUniqueUserIDs() {
        repository.createUser("user1", "user1@example.com", "password1");
        repository.createUser("user2", "user2@example.com", "password2");
        
        Optional<Map<String, String>> user1 = repository.findByUsernameOrEmail("user1");
        Optional<Map<String, String>> user2 = repository.findByUsernameOrEmail("user2");
        
        assertTrue(user1.isPresent());
        assertTrue(user2.isPresent());
        
        String userId1 = user1.get().get("userID");
        String userId2 = user2.get().get("userID");
        
        assertNotNull(userId1, "User ID should not be null");
        assertNotNull(userId2, "User ID should not be null");
        assertNotEquals(userId1, userId2, "User IDs should be unique");
    }

    @Test
    void testDataFilePersistence() {
        // Create user
        repository.createUser("persistentuser", "persistent@example.com", "hashedPassword123");
        
        // Create new repository instance with same file
        UserRepository newRepository = new UserRepository(new TestJsonFileHandler(testDataFile.toString()));
        
        // Verify user persists across repository instances
        assertTrue(newRepository.userExists("persistentuser"), 
                "User should persist across repository instances");
        
        Optional<Map<String, String>> user = newRepository.findByUsernameOrEmail("persistentuser");
        assertTrue(user.isPresent());
        assertEquals("persistent@example.com", user.get().get("email"));
    }

    @Test
    void testGetUserById_Success() {
        // Create a user
        repository.createUser("testuser", "test@example.com", "hashedPassword123");
        
        // Get the user's ID
        Optional<Map<String, String>> userData = repository.findByUsernameOrEmail("testuser");
        assertTrue(userData.isPresent());
        String userId = userData.get().get("userID");
        
        // Test getUserById
        var result = repository.getUserById(userId);
        
        assertTrue(result.isPresent(), "User should be found by ID");
        assertEquals("testuser", result.get().getUsername());
        assertEquals("test@example.com", result.get().getEmail());
        assertEquals("hashedPassword123", result.get().getPassword());
        assertEquals(userId, result.get().getUserID());
    }

    @Test
    void testGetUserById_NotFound() {
        var result = repository.getUserById("non-existent-id");
        
        assertFalse(result.isPresent(), "Non-existent user ID should return empty");
    }

    @Test
    void testGetUserById_NullId() {
        var result = repository.getUserById("");
        
        assertFalse(result.isPresent(), "Empty user ID should return empty");
    }

    @Test
    void testFindByUsernameOrEmail_NullInput() {
        Optional<Map<String, String>> result = repository.findByUsernameOrEmail(null);
        
        assertFalse(result.isPresent(), "Null input should return empty");
    }

    @Test
    void testUserExists_NullUsername() {
        assertFalse(repository.userExists(null), "Null username should return false");
    }

    @Test
    void testEmailExists_NullEmail() {
        assertFalse(repository.emailExists(null), "Null email should return false");
    }

        @Test
    void testCreateUser_NullUsername() {
        try {
            boolean result = repository.createUser(null, "test@example.com", "password123");
            assertFalse(result, "Should fail to create user with null username");
        } catch (IllegalArgumentException e) {
            // Also acceptable - validation throws exception
            assertTrue(e.getMessage().contains("null") || e.getMessage().contains("empty"), 
                    "Error should mention null or empty");
        }
    }

    @Test
    void testCreateUser_NullEmail() {
        try {
            repository.createUser("testuser", null, "password123");
            assertFalse(true, "Should throw exception for null email");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Email") || e.getMessage().contains("email") 
                    || e.getMessage().contains("null") || e.getMessage().contains("empty"), 
                    "Error should mention email or null/empty");
        }
    }

    @Test
    void testCreateUser_NullPassword() {
        try {
            repository.createUser("testuser", "test@example.com", null);
            assertFalse(true, "Should throw exception for null password");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Password") || e.getMessage().contains("password") 
                    || e.getMessage().contains("null") || e.getMessage().contains("empty"), 
                    "Error should mention password or null/empty");
        }
    }
}

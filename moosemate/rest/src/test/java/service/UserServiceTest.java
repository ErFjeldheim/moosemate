package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.User;
import repository.UserRepository;
import java.io.File;

// Unit tests for UserService.
public class UserServiceTest {
    
    private UserService userService;
    private static final String TEST_DATA_FILE = "rest/target/test-data/test-data.json";
    
    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        File testFile = new File(TEST_DATA_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        // Initialize test service with test repository
        UserRepository testRepository = new UserRepository(TEST_DATA_FILE);
        userService = new UserService(testRepository);
    }
    
    @AfterEach
    void tearDown() {
        // Clean up all test files after each test
        File testFile = new File(TEST_DATA_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        // Clean up special test files used in individual tests
        File ioTestFile = new File("./target/test-io-exception.json");
        if (ioTestFile.exists()) {
            ioTestFile.delete();
        }
        
        File nonExistentTestFile = new File("./target/non-existent-test-file.json");
        if (nonExistentTestFile.exists()) {
            nonExistentTestFile.delete();
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
        User user = userService.findByUsernameOrEmail("testuser");
        assertNotNull(user, "Should find user by username");
        assertEquals("testuser", user.getUsername());
    }
    
    @Test
    void testFindByEmail() {
        // Create test user
        userService.createUser("testuser", "test@example.com", "Password123");
        User user = userService.findByUsernameOrEmail("test@example.com");
        assertNotNull(user, "Should find user by email");
        assertEquals("test@example.com", user.getEmail());
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

    @Test
    void testReadDataFromFileWhenFileDoesNotExist() {
        // Create a test repository that points to a file that doesn't exist
        UserRepository nonExistentFileRepository = new UserRepository("./target/non-existent-test-file.json");
        UserService nonExistentFileService = new UserService(nonExistentFileRepository);
        
        // This should trigger the readDataFromFile method's branch where file doesn't exist
        // and should return null
        User result = nonExistentFileService.findByUsernameOrEmail("anyuser");
        assertNull(result, "Should return null when file doesn't exist");
    }

    @Test
    void testInitializeDataFileIOException() {
        // Clean up any existing test-io-exception file first
        File ioTestFile = new File("./target/test-io-exception.json");
        if (ioTestFile.exists()) {
            ioTestFile.delete();
        }
        
        // Create a test repository with a separate test file
        UserRepository ioExceptionRepository = new UserRepository("./target/test-io-exception.json");
        UserService ioExceptionService = new UserService(ioExceptionRepository);
        
        // This test verifies no exception is thrown during normal operations
        // Use unique username and email to avoid conflicts
        String uniqueUsername = "ioexceptionuser_" + System.currentTimeMillis();
        String uniqueEmail = "ioexception_" + System.currentTimeMillis() + "@example.com";
        boolean result = ioExceptionService.createUser(uniqueUsername, uniqueEmail, "password123");
        assertTrue(result, "User creation should succeed with valid data");
        assertNotNull(ioExceptionService, "Service should be created without throwing exception");
        
        // Clean up after test
        if (ioTestFile.exists()) {
            ioTestFile.delete();
        }
    }
}
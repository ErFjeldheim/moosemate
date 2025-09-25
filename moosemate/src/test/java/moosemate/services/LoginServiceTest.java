package moosemate.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test class for LoginService
 * Credits: Claude Sonnet 4
 */
public class LoginServiceTest {

    private LoginService loginService;
    private final String testDataFile = "data.json";

    @BeforeEach
    public void setUp() {
        loginService = new LoginService();
        // Clean up any existing test data
        cleanupTestFile();
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
                  "username": "testuser",
                  "email": "test@example.com",
                  "password": "password123",
                  "userID": "test-uuid-123"
                },
                {
                  "username": "user2",
                  "email": "user2@example.com", 
                  "password": "mypassword456",
                  "userID": "test-uuid-456"
                }
              ]
            }
            """;
        Files.write(Paths.get(testDataFile), testData.getBytes());
    }

    @Test
    public void testLoginUserWithValidUsernameAndPassword() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.loginUser("testuser", "password123");
        assertTrue(result);
    }

    @Test
    public void testLoginUserWithValidEmailAndPassword() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.loginUser("test@example.com", "password123");
        assertTrue(result);
    }

    @Test
    public void testLoginUserWithInvalidPassword() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.loginUser("testuser", "wrongpassword");
        assertFalse(result);
    }

    @Test
    public void testLoginUserWithNonExistentUser() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.loginUser("nonexistent", "password123");
        assertFalse(result);
    }

    @Test
    public void testLoginUserWhenDataFileDoesNotExist() {
        // Ensure no data file exists
        cleanupTestFile();
        
        boolean result = loginService.loginUser("testuser", "password123");
        assertFalse(result);
    }

    @Test
    public void testLoginUserWithEmptyUsersArray() throws IOException {
        String emptyData = """
            {
              "users": []
            }
            """;
        Files.write(Paths.get(testDataFile), emptyData.getBytes());
        
        boolean result = loginService.loginUser("testuser", "password123");
        assertFalse(result);
    }

    @Test
    public void testLoginUserWithNullUsersArray() throws IOException {
        String nullUsersData = """
            {
              "users": null
            }
            """;
        Files.write(Paths.get(testDataFile), nullUsersData.getBytes());
        
        boolean result = loginService.loginUser("testuser", "password123");
        assertFalse(result);
    }

    @Test
    public void testUserExistsWithValidUser() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.userExists("testuser");
        assertTrue(result);
    }

    @Test
    public void testUserExistsWithNonExistentUser() throws IOException {
        createTestDataFile();
        
        boolean result = loginService.userExists("nonexistent");
        assertFalse(result);
    }

    @Test
    public void testUserExistsWhenDataFileDoesNotExist() {
        cleanupTestFile();
        
        boolean result = loginService.userExists("testuser");
        assertFalse(result);
    }

    @Test
    public void testLoginWithMalformedJsonFile() throws IOException {
        String malformedJson = "{ invalid json }";
        Files.write(Paths.get(testDataFile), malformedJson.getBytes());
        
        boolean result = loginService.loginUser("testuser", "password123");
        assertFalse(result);
    }

    @Test
    public void testUserExistsWithMalformedJsonFile() throws IOException {
        String malformedJson = "{ invalid json }";
        Files.write(Paths.get(testDataFile), malformedJson.getBytes());
        
        boolean result = loginService.userExists("testuser");
        assertFalse(result);
    }
}
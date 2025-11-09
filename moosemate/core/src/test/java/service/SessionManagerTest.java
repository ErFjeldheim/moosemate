package service;

import dto.LoginResponse;
import dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
        // Clean state before each test
        sessionManager.logout();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        sessionManager.logout();
    }

    @Test
    void testGetInstance_returnsSameInstance() {
        // Test singleton pattern
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();
        
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void testLogin_setsSessionData() {
        // Arrange
        UserDto user = new UserDto("testuser", "test@example.com");
        LoginResponse loginResponse = new LoginResponse(user, "test-token-123", "user-id-456");

        // Act
        sessionManager.login(loginResponse);

        // Assert
        assertEquals("test-token-123", sessionManager.getSessionToken());
        assertEquals("testuser", sessionManager.getUsername());
        assertEquals("test@example.com", sessionManager.getEmail());
        assertEquals("user-id-456", sessionManager.getUserId());
        assertTrue(sessionManager.isLoggedIn());
    }

    @Test
    void testLogout_clearsSessionData() {
        // Arrange - first login
        UserDto user = new UserDto("testuser", "test@example.com");
        LoginResponse loginResponse = new LoginResponse(user, "test-token-123", "user-id-456");
        sessionManager.login(loginResponse);

        // Act
        sessionManager.logout();

        // Assert
        assertNull(sessionManager.getSessionToken());
        assertNull(sessionManager.getUsername());
        assertNull(sessionManager.getEmail());
        assertNull(sessionManager.getUserId());
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    void testIsLoggedIn_whenNotLoggedIn_returnsFalse() {
        // Arrange - ensure clean state
        sessionManager.logout();

        // Assert
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    void testIsLoggedIn_whenLoggedIn_returnsTrue() {
        // Arrange
        UserDto user = new UserDto("testuser", "test@example.com");
        LoginResponse loginResponse = new LoginResponse(user, "test-token-123", "user-id-456");
        sessionManager.login(loginResponse);

        // Assert
        assertTrue(sessionManager.isLoggedIn());
    }

    @Test
    void testIsLoggedIn_withEmptyToken_returnsFalse() {
        // Arrange - create a login response with empty token
        UserDto user = new UserDto("testuser", "test@example.com");
        LoginResponse loginResponse = new LoginResponse(user, "", "user-id-456");
        sessionManager.login(loginResponse);

        // Assert
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    void testGetSessionToken_whenNotLoggedIn_returnsNull() {
        // Arrange
        sessionManager.logout();

        // Assert
        assertNull(sessionManager.getSessionToken());
    }

    @Test
    void testGetUsername_whenNotLoggedIn_returnsNull() {
        // Arrange
        sessionManager.logout();

        // Assert
        assertNull(sessionManager.getUsername());
    }

    @Test
    void testGetEmail_whenNotLoggedIn_returnsNull() {
        // Arrange
        sessionManager.logout();

        // Assert
        assertNull(sessionManager.getEmail());
    }

    @Test
    void testGetUserId_whenNotLoggedIn_returnsNull() {
        // Arrange
        sessionManager.logout();

        // Assert
        assertNull(sessionManager.getUserId());
    }

    @Test
    void testLogin_overwritesPreviousSession() {
        // Arrange - first login
        UserDto user1 = new UserDto("user1", "user1@example.com");
        LoginResponse loginResponse1 = new LoginResponse(user1, "token-1", "id-1");
        sessionManager.login(loginResponse1);

        // Act - second login overwrites
        UserDto user2 = new UserDto("user2", "user2@example.com");
        LoginResponse loginResponse2 = new LoginResponse(user2, "token-2", "id-2");
        sessionManager.login(loginResponse2);

        // Assert - should have second user's data
        assertEquals("token-2", sessionManager.getSessionToken());
        assertEquals("user2", sessionManager.getUsername());
        assertEquals("user2@example.com", sessionManager.getEmail());
        assertEquals("id-2", sessionManager.getUserId());
    }

    @Test
    void testLogin_withNullUsername_handlesGracefully() {
        // Arrange
        UserDto user = new UserDto(null, "test@example.com");
        LoginResponse loginResponse = new LoginResponse(user, "token-123", "user-id");

        // Act
        sessionManager.login(loginResponse);

        // Assert
        assertNull(sessionManager.getUsername());
        assertNotNull(sessionManager.getSessionToken());
    }

    @Test
    void testLogin_withNullEmail_handlesGracefully() {
        // Arrange
        UserDto user = new UserDto("testuser", null);
        LoginResponse loginResponse = new LoginResponse(user, "token-123", "user-id");

        // Act
        sessionManager.login(loginResponse);

        // Assert
        assertNull(sessionManager.getEmail());
        assertNotNull(sessionManager.getSessionToken());
    }
}

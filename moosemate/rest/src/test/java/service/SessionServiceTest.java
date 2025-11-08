package service;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for SessionService with focus on session management and error handling.
 */
public class SessionServiceTest {

    private SessionService sessionService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        sessionService = new SessionService();
        testUser = new User("testuser", "test@example.com", "password123", "user123");
    }

    // ============== CREATE SESSION TESTS ==============

    @Test
    public void testCreateSessionSuccess() {
        String sessionToken = sessionService.createSession(testUser);

        assertNotNull(sessionToken);
        assertFalse(sessionToken.isEmpty());
        assertTrue(sessionService.isValidSession(sessionToken));
    }

    @Test
    public void testCreateSessionGeneratesUniqueTokens() {
        String token1 = sessionService.createSession(testUser);
        String token2 = sessionService.createSession(testUser);

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
    }

    @Test
    public void testCreateMultipleSessions() {
        User user2 = new User("user2", "user2@example.com", "password", "user456");
        User user3 = new User("user3", "user3@example.com", "password", "user789");

        String token1 = sessionService.createSession(testUser);
        String token2 = sessionService.createSession(user2);
        String token3 = sessionService.createSession(user3);

        assertTrue(sessionService.isValidSession(token1));
        assertTrue(sessionService.isValidSession(token2));
        assertTrue(sessionService.isValidSession(token3));
        
        assertEquals(testUser, sessionService.getUser(token1));
        assertEquals(user2, sessionService.getUser(token2));
        assertEquals(user3, sessionService.getUser(token3));
    }

    // ============== GET USER TESTS ==============

    @Test
    public void testGetUserSuccess() {
        String sessionToken = sessionService.createSession(testUser);
        User retrievedUser = sessionService.getUser(sessionToken);

        assertNotNull(retrievedUser);
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser.getUserID(), retrievedUser.getUserID());
    }

    @Test
    public void testGetUserWithInvalidToken() {
        User retrievedUser = sessionService.getUser("invalid-token");

        assertNull(retrievedUser);
    }

    @Test
    public void testGetUserWithNullToken() {
        // ConcurrentHashMap doesn't allow null keys, so this will throw NPE
        // We should handle this gracefully in real implementation
        assertThrows(NullPointerException.class, () -> {
            sessionService.getUser(null);
        });
    }

    @Test
    public void testGetUserWithEmptyToken() {
        User retrievedUser = sessionService.getUser("");

        assertNull(retrievedUser);
    }

    // ============== GET USER ID BY TOKEN TESTS ==============

    @Test
    public void testGetUserIdByTokenSuccess() {
        String sessionToken = sessionService.createSession(testUser);
        String userId = sessionService.getUserIdByToken(sessionToken);

        assertNotNull(userId);
        assertEquals("user123", userId);
    }

    @Test
    public void testGetUserIdByTokenInvalid() {
        String userId = sessionService.getUserIdByToken("invalid-token");

        assertNull(userId);
    }

    @Test
    public void testGetUserIdByTokenNull() {
        // ConcurrentHashMap doesn't allow null keys
        assertThrows(NullPointerException.class, () -> {
            sessionService.getUserIdByToken(null);
        });
    }

    @Test
    public void testGetUserIdByTokenEmpty() {
        String userId = sessionService.getUserIdByToken("");

        assertNull(userId);
    }

    // ============== IS VALID SESSION TESTS ==============

    @Test
    public void testIsValidSessionTrue() {
        String sessionToken = sessionService.createSession(testUser);

        assertTrue(sessionService.isValidSession(sessionToken));
    }

    @Test
    public void testIsValidSessionFalse() {
        assertFalse(sessionService.isValidSession("invalid-token"));
    }

    @Test
    public void testIsValidSessionNull() {
        assertFalse(sessionService.isValidSession(null));
    }

    @Test
    public void testIsValidSessionEmpty() {
        assertFalse(sessionService.isValidSession(""));
    }

    @Test
    public void testIsValidSessionAfterTermination() {
        String sessionToken = sessionService.createSession(testUser);
        assertTrue(sessionService.isValidSession(sessionToken));

        sessionService.terminateSession(sessionToken);
        assertFalse(sessionService.isValidSession(sessionToken));
    }

    // ============== TERMINATE SESSION TESTS ==============

    @Test
    public void testTerminateSessionSuccess() {
        String sessionToken = sessionService.createSession(testUser);
        assertTrue(sessionService.isValidSession(sessionToken));

        sessionService.terminateSession(sessionToken);

        assertFalse(sessionService.isValidSession(sessionToken));
        assertNull(sessionService.getUser(sessionToken));
        assertNull(sessionService.getUserIdByToken(sessionToken));
    }

    @Test
    public void testTerminateInvalidSession() {
        // Should not throw exception when terminating invalid session
        assertDoesNotThrow(() -> sessionService.terminateSession("invalid-token"));
    }

    @Test
    public void testTerminateNullSession() {
        // ConcurrentHashMap doesn't allow null keys, so this will throw NPE
        assertThrows(NullPointerException.class, () -> {
            sessionService.terminateSession(null);
        });
    }

    @Test
    public void testTerminateSessionTwice() {
        String sessionToken = sessionService.createSession(testUser);
        
        sessionService.terminateSession(sessionToken);
        assertFalse(sessionService.isValidSession(sessionToken));

        // Terminating again should not cause issues
        assertDoesNotThrow(() -> sessionService.terminateSession(sessionToken));
    }

    // ============== CONCURRENT SESSION TESTS ==============

    @Test
    public void testMultipleActiveSessionsForSameUser() {
        // User can have multiple active sessions
        String token1 = sessionService.createSession(testUser);
        String token2 = sessionService.createSession(testUser);

        assertTrue(sessionService.isValidSession(token1));
        assertTrue(sessionService.isValidSession(token2));

        // Terminating one session should not affect the other
        sessionService.terminateSession(token1);
        assertFalse(sessionService.isValidSession(token1));
        assertTrue(sessionService.isValidSession(token2));
    }

    @Test
    public void testSessionPersistenceAcrossUsers() {
        User user2 = new User("user2", "user2@example.com", "password", "user456");
        
        String token1 = sessionService.createSession(testUser);
        String token2 = sessionService.createSession(user2);

        // Both sessions should be active
        assertTrue(sessionService.isValidSession(token1));
        assertTrue(sessionService.isValidSession(token2));

        // Terminate user2's session
        sessionService.terminateSession(token2);

        // user1's session should still be active
        assertTrue(sessionService.isValidSession(token1));
        assertFalse(sessionService.isValidSession(token2));
    }

    // ============== EDGE CASE TESTS ==============

    @Test
    public void testCreateSessionWithDifferentUsers() {
        User user1 = new User("alice", "alice@example.com", "pass1", "id1");
        User user2 = new User("bob", "bob@example.com", "pass2", "id2");
        User user3 = new User("charlie", "charlie@example.com", "pass3", "id3");

        String token1 = sessionService.createSession(user1);
        String token2 = sessionService.createSession(user2);
        String token3 = sessionService.createSession(user3);

        assertEquals("id1", sessionService.getUserIdByToken(token1));
        assertEquals("id2", sessionService.getUserIdByToken(token2));
        assertEquals("id3", sessionService.getUserIdByToken(token3));
    }

    @Test
    public void testGetUserReturnsCorrectUser() {
        User user1 = new User("alice", "alice@example.com", "pass1", "id1");
        User user2 = new User("bob", "bob@example.com", "pass2", "id2");

        String token1 = sessionService.createSession(user1);
        String token2 = sessionService.createSession(user2);

        User retrieved1 = sessionService.getUser(token1);
        User retrieved2 = sessionService.getUser(token2);

        assertEquals("alice", retrieved1.getUsername());
        assertEquals("bob", retrieved2.getUsername());
        assertNotEquals(retrieved1.getUserID(), retrieved2.getUserID());
    }

    @Test
    public void testSessionTokenFormat() {
        String sessionToken = sessionService.createSession(testUser);

        // UUID format check (36 characters with dashes)
        assertEquals(36, sessionToken.length());
        assertTrue(sessionToken.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    public void testLargeNumberOfSessions() {
        // Test handling many concurrent sessions
        int sessionCount = 100;
        String[] tokens = new String[sessionCount];

        for (int i = 0; i < sessionCount; i++) {
            User user = new User("user" + i, "user" + i + "@test.com", "pass", "id" + i);
            tokens[i] = sessionService.createSession(user);
        }

        // All sessions should be valid
        for (int i = 0; i < sessionCount; i++) {
            assertTrue(sessionService.isValidSession(tokens[i]));
            assertEquals("id" + i, sessionService.getUserIdByToken(tokens[i]));
        }

        // Terminate half of them
        for (int i = 0; i < sessionCount / 2; i++) {
            sessionService.terminateSession(tokens[i]);
        }

        // Check validity
        for (int i = 0; i < sessionCount / 2; i++) {
            assertFalse(sessionService.isValidSession(tokens[i]));
        }
        for (int i = sessionCount / 2; i < sessionCount; i++) {
            assertTrue(sessionService.isValidSession(tokens[i]));
        }
    }
}

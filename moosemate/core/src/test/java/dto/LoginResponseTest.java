package dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LoginResponseTest {

    @Test
    void testDefaultConstructor() {
        LoginResponse response = new LoginResponse();
        assertNotNull(response);
        assertNull(response.getUser());
        assertNull(response.getSessionToken());
        assertNull(response.getUserId());
    }

    @Test
    void testParameterizedConstructor() {
        UserDto user = new UserDto("john", "john@example.com");
        LoginResponse response = new LoginResponse(user, "token123", "user-id-123");
        
        assertEquals(user, response.getUser());
        assertEquals("token123", response.getSessionToken());
        assertEquals("user-id-123", response.getUserId());
    }

    @Test
    void testSetUser() {
        LoginResponse response = new LoginResponse();
        UserDto user = new UserDto("jane", "jane@test.com");
        response.setUser(user);
        assertEquals(user, response.getUser());
    }

    @Test
    void testSetSessionToken() {
        LoginResponse response = new LoginResponse();
        response.setSessionToken("abc-token-xyz");
        assertEquals("abc-token-xyz", response.getSessionToken());
    }

    @Test
    void testSetUserId() {
        LoginResponse response = new LoginResponse();
        response.setUserId("user-456");
        assertEquals("user-456", response.getUserId());
    }

    @Test
    void testSetUserOverride() {
        UserDto user1 = new UserDto("john", "john@example.com");
        UserDto user2 = new UserDto("jane", "jane@test.com");
        LoginResponse response = new LoginResponse(user1, "token", "id");
        
        response.setUser(user2);
        assertEquals(user2, response.getUser());
    }

    @Test
    void testSetSessionTokenOverride() {
        LoginResponse response = new LoginResponse(
                new UserDto("john", "john@example.com"),
                "old-token",
                "user-id"
        );
        
        response.setSessionToken("new-token");
        assertEquals("new-token", response.getSessionToken());
    }

    @Test
    void testSetUserIdOverride() {
        LoginResponse response = new LoginResponse(
                new UserDto("john", "john@example.com"),
                "token",
                "old-id"
        );
        
        response.setUserId("new-id");
        assertEquals("new-id", response.getUserId());
    }

    @Test
    void testAllFieldsIndependent() {
        UserDto user = new UserDto("user", "user@test.com");
        LoginResponse response = new LoginResponse();
        
        response.setUser(user);
        response.setSessionToken("token");
        response.setUserId("id");
        
        assertEquals(user, response.getUser());
        assertEquals("token", response.getSessionToken());
        assertEquals("id", response.getUserId());
    }
}

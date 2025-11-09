package dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void testDefaultConstructor() {
        LoginRequest request = new LoginRequest();
        assertNotNull(request);
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void testParameterizedConstructor() {
        LoginRequest request = new LoginRequest("john", "pass123");
        assertEquals("john", request.getUsername());
        assertEquals("pass123", request.getPassword());
    }

    @Test
    void testSetUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("jane");
        assertEquals("jane", request.getUsername());
    }

    @Test
    void testSetPassword() {
        LoginRequest request = new LoginRequest();
        request.setPassword("secret456");
        assertEquals("secret456", request.getPassword());
    }

    @Test
    void testSetUsernameOverride() {
        LoginRequest request = new LoginRequest("john", "pass123");
        request.setUsername("newuser");
        assertEquals("newuser", request.getUsername());
    }

    @Test
    void testSetPasswordOverride() {
        LoginRequest request = new LoginRequest("john", "pass123");
        request.setPassword("newpass");
        assertEquals("newpass", request.getPassword());
    }
}

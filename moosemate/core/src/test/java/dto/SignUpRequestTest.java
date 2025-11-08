package dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestTest {

    @Test
    void testDefaultConstructor() {
        SignUpRequest request = new SignUpRequest();
        assertNotNull(request);
        assertNull(request.getUsername());
        assertNull(request.getPassword());
        assertNull(request.getEmail());
    }

    @Test
    void testParameterizedConstructor() {
        SignUpRequest request = new SignUpRequest("john", "pass123", "john@example.com");
        assertEquals("john", request.getUsername());
        assertEquals("pass123", request.getPassword());
        assertEquals("john@example.com", request.getEmail());
    }

    @Test
    void testSetUsername() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("jane");
        assertEquals("jane", request.getUsername());
    }

    @Test
    void testSetPassword() {
        SignUpRequest request = new SignUpRequest();
        request.setPassword("secret456");
        assertEquals("secret456", request.getPassword());
    }

    @Test
    void testSetEmail() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        assertEquals("test@example.com", request.getEmail());
    }

    @Test
    void testSetUsernameOverride() {
        SignUpRequest request = new SignUpRequest("john", "pass123", "john@example.com");
        request.setUsername("newuser");
        assertEquals("newuser", request.getUsername());
    }

    @Test
    void testSetPasswordOverride() {
        SignUpRequest request = new SignUpRequest("john", "pass123", "john@example.com");
        request.setPassword("newpass");
        assertEquals("newpass", request.getPassword());
    }

    @Test
    void testSetEmailOverride() {
        SignUpRequest request = new SignUpRequest("john", "pass123", "john@example.com");
        request.setEmail("newemail@test.com");
        assertEquals("newemail@test.com", request.getEmail());
    }
}

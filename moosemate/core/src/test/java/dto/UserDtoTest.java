package dto;

import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for UserDto.
 */
public class UserDtoTest {

    @Test
    public void testDefaultConstructor() {
        UserDto dto = new UserDto();
        
        assertNotNull(dto);
        assertNull(dto.getUsername());
        assertNull(dto.getEmail());
    }

    @Test
    public void testConstructorWithParameters() {
        UserDto dto = new UserDto("testuser", "test@example.com");
        
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    public void testSetUsername() {
        UserDto dto = new UserDto();
        dto.setUsername("newuser");
        
        assertEquals("newuser", dto.getUsername());
    }

    @Test
    public void testSetEmail() {
        UserDto dto = new UserDto();
        dto.setEmail("new@example.com");
        
        assertEquals("new@example.com", dto.getEmail());
    }

    @Test
    public void testFromUserFactoryMethod() {
        User user = new User("testuser", "test@example.com", "password123", "user123");
        UserDto dto = UserDto.fromUser(user);
        
        assertNotNull(dto);
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    public void testFromUserDoesNotExposePassword() {
        User user = new User("testuser", "test@example.com", "secretpassword", "user123");
        UserDto dto = UserDto.fromUser(user);
        
        // Verify DTO only contains username and email, not password
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        // Password should not be accessible through DTO
    }

    @Test
    public void testNullUsername() {
        UserDto dto = new UserDto(null, "test@example.com");
        
        assertNull(dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    public void testNullEmail() {
        UserDto dto = new UserDto("testuser", null);
        
        assertEquals("testuser", dto.getUsername());
        assertNull(dto.getEmail());
    }

    @Test
    public void testEmptyStrings() {
        UserDto dto = new UserDto("", "");
        
        assertEquals("", dto.getUsername());
        assertEquals("", dto.getEmail());
    }

    @Test
    public void testLongUsername() {
        String longUsername = "a".repeat(100);
        UserDto dto = new UserDto(longUsername, "test@example.com");
        
        assertEquals(longUsername, dto.getUsername());
        assertEquals(100, dto.getUsername().length());
    }

    @Test
    public void testSpecialCharactersInUsername() {
        UserDto dto = new UserDto("user_123-test", "test@example.com");
        
        assertEquals("user_123-test", dto.getUsername());
    }

    @Test
    public void testComplexEmail() {
        String complexEmail = "user.name+tag@sub.domain.example.com";
        UserDto dto = new UserDto("testuser", complexEmail);
        
        assertEquals(complexEmail, dto.getEmail());
    }

    @Test
    public void testModifyingDto() {
        UserDto dto = new UserDto("original", "original@example.com");
        
        dto.setUsername("modified");
        dto.setEmail("modified@example.com");
        
        assertEquals("modified", dto.getUsername());
        assertEquals("modified@example.com", dto.getEmail());
    }

    @Test
    public void testFromUserWithComplexUser() {
        User user = new User("complex_user-123", "complex.email+test@domain.co.uk", "P@ssw0rd!", "user456");
        UserDto dto = UserDto.fromUser(user);
        
        assertEquals("complex_user-123", dto.getUsername());
        assertEquals("complex.email+test@domain.co.uk", dto.getEmail());
    }
}

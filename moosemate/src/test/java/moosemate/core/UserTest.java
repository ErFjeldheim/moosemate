package moosemate.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
//Constructor test
    @Test
    public void constructorTest(){
        User user = new User("testuser", "test@example.com", "password123");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testUserUsername() {
        User user = new User("testuser", "test@example.com", "password123");

        //Testing getter
        assertEquals("testuser", user.getUsername());
        
        //Testing setter
        user.setUsername("testusername");
        assertEquals("testusername", user.getUsername());
    }

    @Test
    public void testUserEmail() {
        User user = new User("testuser", "test@example.com", "password123");

        //Testing getter
        assertEquals("test@example.com", user.getEmail());
        
        //Testing setter
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testUserPassword() {
        User user = new User("testuser", "test@example.com", "password123");

        //Testing getter
        assertEquals("password123", user.getPassword());
        
        //Testing setter
        user.setPassword("newpassword123");
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    public void testInvalidUsername() {
        User user = new User("testuser", "test@example.com", "password123");
        
        //Testing empty username
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(""));
        
        //Testing username with space
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("user name"));
        
        //Testing username too long
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("verylongusernamethatexceeds20characters"));
    }

    @Test
    public void testInvalidEmail() {
        User user = new User("testuser", "test@example.com", "password123");
        
        //Testing empty email
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
        
        //Testing invalid email format
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid-email"));
    }

    @Test
    public void testInvalidPassword() {
        User user = new User("testuser", "test@example.com", "password123");
        
        //Testing empty password
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
        
        //Testing password too short
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("short"));
        
        //Testing password without letters
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("12345678"));
        
        //Testing password without numbers
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("password"));
    }
}
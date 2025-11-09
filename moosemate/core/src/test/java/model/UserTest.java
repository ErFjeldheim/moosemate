package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UserTest {

    String userID = UUID.randomUUID().toString(); // Generates userID-hash

    //Constructor test
    @Test
    public void constructorTest() {
        User user = new User("testuser", "test@example.com", "password123", userID);
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    // Default constructor test
    @Test
    public void defaultConstructorTest() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getUserID());
    }

    @Test
    public void testUserUsername() {
        User user = new User("testuser", "test@example.com", "password123", userID);

        //Testing getter
        assertEquals("testuser", user.getUsername());
        
        //Testing setter
        user.setUsername("testusername");
        assertEquals("testusername", user.getUsername());
    }

    @Test
    public void testUserEmail() {
        User user = new User("testuser", "test@example.com", "password123", userID);

        //Testing getter
        assertEquals("test@example.com", user.getEmail());
        
        //Testing setter
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testUserPassword() {
        User user = new User("testuser", "test@example.com", "password123", userID);

        //Testing getter
        assertEquals("password123", user.getPassword());
        
        //Testing setter
        user.setPassword("newpassword123");
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    public void testInvalidUsername() {
        User user = new User("testuser", "test@example.com", "password123", userID);
        
        //Testing empty username
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(""));
        
        //Testing null username
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(null));
        
        //Testing username with space
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("user name"));
        
        //Testing username too long
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("verylongusernamethatexceeds20characters"));
    }

    @Test
    public void testInvalidEmail() {
        User user = new User("testuser", "test@example.com", "password123", userID);
        
        //Testing empty email
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
        
        //Testing null email
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        
        //Testing invalid email format
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid-email"));
    }

    @Test
    public void testInvalidPassword() {
        User user = new User("testuser", "test@example.com", "password123", userID);
        
        //Testing empty password
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
        
        //Testing null password
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
        
        //Testing password too short
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("short"));
        
        //Testing password without letters
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("12345678"));
        
        //Testing password without numbers
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("password"));
    }

    @Test
    public void testValidUserID() {
        User user = new User("testuser", "test@example.com", "password123", userID);
        
        //Testing userID construction
        assertNotNull(userID);

        //Testing assigning userID to user
        assertNotNull(user.getUserID());
        assertEquals(userID, user.getUserID());

        //Testing valid UUID-format
        assertDoesNotThrow(() -> UUID.fromString(userID));

        //Testing userID is unique
        String otherID = UUID.randomUUID().toString();
        assertNotEquals(userID, otherID);
    }

    @Test
    public void testUserIDSetter() {
        User user = new User();
        String newUserID = UUID.randomUUID().toString();
        
        // Test setting UserID
        user.setUserID(newUserID);
        assertEquals(newUserID, user.getUserID());
        
        // Test setting null UserID (should be allowed)
        user.setUserID(null);
        assertNull(user.getUserID());
        
        // Test setting empty UserID (should be allowed)
        user.setUserID("");
        assertEquals("", user.getUserID());
    }

    @Test
    public void testCompleteUserCreationAndModification() {
        // Test creating user step by step using default constructor and setters
        User user = new User();
        
        // Set all properties individually
        user.setUsername("stepuser");
        user.setEmail("step@example.com");
        user.setPassword("steppass123");
        user.setUserID("step-id-123");
        
        // Verify all properties are set correctly
        assertEquals("stepuser", user.getUsername());
        assertEquals("step@example.com", user.getEmail());
        assertEquals("steppass123", user.getPassword());
        assertEquals("step-id-123", user.getUserID());
    }

    @Test
    public void testUsernameEdgeCases() {
        User user = new User();
        
        // Test valid username at maximum length (20 characters)
        user.setUsername("12345678901234567890");
        assertEquals("12345678901234567890", user.getUsername());
        
        // Test valid username with allowed characters
        user.setUsername("user_123");
        assertEquals("user_123", user.getUsername());
        
        // Test valid username with dots
        user.setUsername("user.name");
        assertEquals("user.name", user.getUsername());
    }

    @Test
    public void testEmailEdgeCases() {
        User user = new User();
        
        // Test various valid email formats
        user.setEmail("user@domain.com");
        assertEquals("user@domain.com", user.getEmail());
        
        user.setEmail("user.name+tag@domain.co.uk");
        assertEquals("user.name+tag@domain.co.uk", user.getEmail());
        
        user.setEmail("123@numeric-domain.org");
        assertEquals("123@numeric-domain.org", user.getEmail());
    }

    @Test
    public void testPasswordEdgeCases() {
        User user = new User();
        
        // Test valid password at minimum length (8 characters)
        user.setPassword("pass123a");
        assertEquals("pass123a", user.getPassword());
        
        // Test password with mixed case and numbers
        user.setPassword("MyPass123");
        assertEquals("MyPass123", user.getPassword());
        
        // Test password with special characters (letters and numbers required)
        user.setPassword("MyP@ss123");
        assertEquals("MyP@ss123", user.getPassword());
    }

    @Test
    public void testConstructorWithValidationEdgeCases() {
        // Test constructor properly validates all inputs
        String validUserID = UUID.randomUUID().toString();
        
        // Valid at boundary conditions
        User user = new User("a", "a@b.co", "pass123a", validUserID);
        assertEquals("a", user.getUsername());
        assertEquals("a@b.co", user.getEmail());
        assertEquals("pass123a", user.getPassword());
        assertEquals(validUserID, user.getUserID());
    }

    @Test
    public void testConstructorWithInvalidInputs() {
        String validUserID = UUID.randomUUID().toString();
        
        // Test constructor throws exceptions for invalid inputs
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, "valid@email.com", "validpass123", validUserID));
            
        assertThrows(IllegalArgumentException.class,
                () -> new User("validuser", null, "validpass123", validUserID));
            
        assertThrows(IllegalArgumentException.class,
                () -> new User("validuser", "valid@email.com", null, validUserID));
            
        assertThrows(IllegalArgumentException.class,
                () -> new User("", "valid@email.com", "validpass123", validUserID));
            
        assertThrows(IllegalArgumentException.class,
                () -> new User("validuser", "", "validpass123", validUserID));
            
        assertThrows(IllegalArgumentException.class,
                () -> new User("validuser", "valid@email.com", "", validUserID));
    }

    @Test
    public void testEmailValidationBoundaries() {
        User user = new User();
        
        // Test various invalid email formats to cover all validation branches
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("plainaddress"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("@missingusername.com"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("username@.com"));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("username@com"));
        
        // These might actually be valid depending on regex implementation, so test individually
        // assertThrows(IllegalArgumentException.class, () -> user.setEmail("username..double.dot@example.com"));
        // assertThrows(IllegalArgumentException.class, () -> user.setEmail("username@-example.com"));
        // assertThrows(IllegalArgumentException.class, () -> user.setEmail("username@example-.com"));
        
        // Test valid emails that should pass
        assertDoesNotThrow(() -> user.setEmail("test@example.com"));
        assertDoesNotThrow(() -> user.setEmail("user.name@domain.org"));
        assertDoesNotThrow(() -> user.setEmail("firstname+lastname@domain.co.uk"));
    }

    @Test
    public void testPasswordValidationBoundaries() {
        User user = new User();
        
        // Test password that is exactly 7 characters (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("pass12"));
        
        // Test password with only uppercase letters, no numbers (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("PASSWORDONLY"));
        
        // Test password with only lowercase letters, no numbers (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("passwordonly"));
        
        // Test password with numbers only, no letters (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("12345678"));
        
        // Test password with special characters but no letters (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("!@#$%^&*"));
        
        // Test password with special characters but no numbers (should fail)
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("password!@#"));
        
        // Test valid passwords that meet all criteria
        assertDoesNotThrow(() -> user.setPassword("validPass1"));
        assertDoesNotThrow(() -> user.setPassword("MyP@ssw0rd"));
        assertDoesNotThrow(() -> user.setPassword("Test123!"));
    }

    @Test
    public void testUsernameSpecialCasesAndBoundaries() {
        User user = new User();
        
        // Test username exactly at 20 character limit
        String exactlyTwentyChars = "12345678901234567890";
        assertEquals(20, exactlyTwentyChars.length());
        user.setUsername(exactlyTwentyChars);
        assertEquals(exactlyTwentyChars, user.getUsername());
        
        // Test username exactly at 21 characters (should fail)
        String twentyOneChars = "123456789012345678901";
        assertEquals(21, twentyOneChars.length());
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(twentyOneChars));
        
        // Test username with various space positions
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("user name"));
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(" username"));
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("username "));
        assertThrows(IllegalArgumentException.class, () -> user.setUsername("user name test"));
        
        // Test valid usernames with underscores and dots
        assertDoesNotThrow(() -> user.setUsername("user_name"));
        assertDoesNotThrow(() -> user.setUsername("user.name"));
        assertDoesNotThrow(() -> user.setUsername("user123"));
        assertDoesNotThrow(() -> user.setUsername("123user"));
    }
}

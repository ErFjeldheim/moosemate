package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for Moosage class with focus on error handling.
 */
public class MoosageTest {
    
    private Moosage moosage;
    private User testUser;
    private LocalDateTime testTime;
    
    @BeforeEach
    public void setUp() {
        testUser = new User("testuser", "test@example.com", "hashedpassword", "user123");
        testTime = LocalDateTime.now();
        moosage = new Moosage(1L, "Test content", testUser, testTime);
    }
    
    @Test
    public void testDefaultConstructor() {
        Moosage defaultMoosage = new Moosage();
        assertNotNull(defaultMoosage);
        assertNotNull(defaultMoosage.getLikedByUserIds());
        assertTrue(defaultMoosage.getLikedByUserIds().isEmpty());
    }
    
    @Test
    public void testParameterizedConstructor() {
        assertEquals(1L, moosage.getId());
        assertEquals("Test content", moosage.getContent());
        assertEquals(testUser, moosage.getAuthor());
        assertEquals(testTime, moosage.getTime());
        assertNotNull(moosage.getLikedByUserIds());
        assertTrue(moosage.getLikedByUserIds().isEmpty());
    }
    
    @Test
    public void testSettersAndGetters() {
        Moosage newMoosage = new Moosage();
        User newUser = new User("newuser", "new@example.com", "password", "user456");
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        
        newMoosage.setId(2L);
        newMoosage.setContent("New content");
        newMoosage.setAuthor(newUser);
        newMoosage.setTime(newTime);
        
        assertEquals(2L, newMoosage.getId());
        assertEquals("New content", newMoosage.getContent());
        assertEquals(newUser, newMoosage.getAuthor());
        assertEquals(newTime, newMoosage.getTime());
    }
    
    @Test
    public void testAddLike() {
        String userId = "user789";
        moosage.addLike(userId);
        
        assertTrue(moosage.getLikedByUserIds().contains(userId));
        assertEquals(1, moosage.getLikedByUserIds().size());
    }
    
    @Test
    public void testAddMultipleLikes() {
        moosage.addLike("user1");
        moosage.addLike("user2");
        moosage.addLike("user3");
        
        assertEquals(3, moosage.getLikedByUserIds().size());
        assertTrue(moosage.getLikedByUserIds().contains("user1"));
        assertTrue(moosage.getLikedByUserIds().contains("user2"));
        assertTrue(moosage.getLikedByUserIds().contains("user3"));
    }
    
    @Test
    public void testAddDuplicateLike() {
        String userId = "user123";
        moosage.addLike(userId);
        moosage.addLike(userId); // Adding same user again
        
        // Set should only contain unique values
        assertEquals(1, moosage.getLikedByUserIds().size());
    }
    
    @Test
    public void testRemoveLike() {
        String userId = "user456";
        moosage.addLike(userId);
        assertTrue(moosage.getLikedByUserIds().contains(userId));
        
        moosage.removeLike(userId);
        assertFalse(moosage.getLikedByUserIds().contains(userId));
        assertEquals(0, moosage.getLikedByUserIds().size());
    }
    
    @Test
    public void testRemoveNonExistentLike() {
        String userId = "nonexistent";
        
        // Removing like that doesn't exist should not throw exception
        assertDoesNotThrow(() -> moosage.removeLike(userId));
        assertEquals(0, moosage.getLikedByUserIds().size());
    }
    
    @Test
    public void testLikedByUserIdsNotNull() {
        Moosage newMoosage = new Moosage(5L, "Content", testUser, testTime);
        assertNotNull(newMoosage.getLikedByUserIds());
    }
    
    @Test
    public void testSetContentWithNull() {
        moosage.setContent(null);
        assertNull(moosage.getContent());
    }
    
    @Test
    public void testSetContentWithEmptyString() {
        moosage.setContent("");
        assertEquals("", moosage.getContent());
    }
    
    @Test
    public void testSetContentWithLongText() {
        String longContent = "a".repeat(500);
        moosage.setContent(longContent);
        assertEquals(500, moosage.getContent().length());
    }
    
    @Test
    public void testSetAuthorWithNull() {
        moosage.setAuthor(null);
        assertNull(moosage.getAuthor());
    }
    
    @Test
    public void testSetIdWithNull() {
        moosage.setId(null);
        assertNull(moosage.getId());
    }
    
    @Test
    public void testSetTimeWithNull() {
        moosage.setTime(null);
        assertNull(moosage.getTime());
    }
    
    @Test
    public void testMultipleLikeOperations() {
        moosage.addLike("user1");
        moosage.addLike("user2");
        moosage.addLike("user3");
        
        assertEquals(3, moosage.getLikedByUserIds().size());
        
        moosage.removeLike("user2");
        assertEquals(2, moosage.getLikedByUserIds().size());
        assertFalse(moosage.getLikedByUserIds().contains("user2"));
        
        moosage.addLike("user4");
        assertEquals(3, moosage.getLikedByUserIds().size());
    }
    
    @Test
    public void testGetLikedByUserIdsReturnsSet() {
        Set<String> likes = moosage.getLikedByUserIds();
        assertNotNull(likes);
        assertTrue(likes instanceof Set);
    }
}

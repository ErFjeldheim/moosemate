package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void testGenerateUUID_NotNull() {
        String uuid = IdGenerator.generateUUID();
        assertNotNull(uuid);
    }

    @Test
    void testGenerateUUID_NotEmpty() {
        String uuid = IdGenerator.generateUUID();
        assertFalse(uuid.isEmpty());
    }

    @Test
    void testGenerateUUID_Format() {
        String uuid = IdGenerator.generateUUID();
        // UUID format: 8-4-4-4-12 characters separated by hyphens
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    void testGenerateUUID_Unique() {
        String uuid1 = IdGenerator.generateUUID();
        String uuid2 = IdGenerator.generateUUID();
        assertNotEquals(uuid1, uuid2, "Generated UUIDs should be unique");
    }

    @Test
    void testGenerateUserId_NotNull() {
        String userId = IdGenerator.generateUserId();
        assertNotNull(userId);
    }

    @Test
    void testGenerateUserId_NotEmpty() {
        String userId = IdGenerator.generateUserId();
        assertFalse(userId.isEmpty());
    }

    @Test
    void testGenerateUserId_HasPrefix() {
        String userId = IdGenerator.generateUserId();
        assertTrue(userId.startsWith("USER-"), "User ID should start with 'USER-' prefix");
    }

    @Test
    void testGenerateUserId_Format() {
        String userId = IdGenerator.generateUserId();
        // Format: USER- followed by UUID
        assertTrue(userId.matches("USER-[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    void testGenerateUserId_Unique() {
        String userId1 = IdGenerator.generateUserId();
        String userId2 = IdGenerator.generateUserId();
        assertNotEquals(userId1, userId2, "Generated user IDs should be unique");
    }

    @Test
    void testGenerateUserId_ContainsUUID() {
        String userId = IdGenerator.generateUserId();
        String uuidPart = userId.substring(5); // Remove "USER-" prefix
        assertTrue(uuidPart.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }
}

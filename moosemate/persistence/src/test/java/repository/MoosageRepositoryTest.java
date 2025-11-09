package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import model.Moosage;
import model.User;

//Comprehensive tests for MoosageRepository.
// Tests JSON-based persistence of moosages.
public class MoosageRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private MoosageRepository repository;
    private Path testDataFile;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Create a temporary test data file
        testDataFile = Files.createTempFile("test-moosages-", ".json");
        
        // Create repository with test file handler
        TestJsonFileHandler testFileHandler = new TestJsonFileHandler(testDataFile.toAbsolutePath().toString());
        repository = new MoosageRepository(userRepository, testFileHandler);
        
        // Mock user repository to return test users
        User testUser1 = new User("testuser", "test@example.com", "password", "user1");
        User testUser2 = new User("user2name", "user2@example.com", "password", "user2");
        User testUser3 = new User("user3name", "user3@example.com", "password", "user3");
        when(userRepository.getUserById("user1")).thenReturn(Optional.of(testUser1));
        when(userRepository.getUserById("user2")).thenReturn(Optional.of(testUser2));
        when(userRepository.getUserById("user3")).thenReturn(Optional.of(testUser3));
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up test file
        if (testDataFile != null && Files.exists(testDataFile)) {
            Files.delete(testDataFile);
        }
    }

    @Test
    public void testCreateMoosage() {
        Moosage moosage = repository.createMoosage("Test content", "user1", "testuser");
        
        assertNotNull(moosage);
        assertEquals("Test content", moosage.getContent());
        assertEquals("user1", moosage.getAuthor().getUserID());
        assertEquals("testuser", moosage.getAuthor().getUsername());
        assertNotNull(moosage.getId());
        assertNotNull(moosage.getTime());
        assertNotNull(moosage.getLikedByUserIds());
        assertTrue(moosage.getLikedByUserIds().isEmpty());
        assertFalse(moosage.isEdited());
    }

    @Test
    public void testGetAllMoosagesEmpty() {
        List<Moosage> moosages = repository.getAllMoosages();
        
        assertNotNull(moosages);
        assertTrue(moosages.isEmpty());
    }

    @Test
    public void testGetAllMoosagesAfterCreating() {
        repository.createMoosage("First moosage", "user1", "user1");
        repository.createMoosage("Second moosage", "user2", "user2");
        
        List<Moosage> moosages = repository.getAllMoosages();
        
        assertEquals(2, moosages.size());
    }

    @Test
    public void testGetAllMoosagesSortedByTimeNewestFirst() throws InterruptedException {
        Moosage first = repository.createMoosage("First", "user1", "user1");
        Thread.sleep(10); // Small delay to ensure different timestamps
        Moosage second = repository.createMoosage("Second", "user1", "user1");
        
        List<Moosage> moosages = repository.getAllMoosages();
        
        assertEquals(2, moosages.size());
        // Newest should be first
        assertEquals(second.getId(), moosages.get(0).getId());
        assertEquals(first.getId(), moosages.get(1).getId());
    }

    @Test
    public void testGetMoosageById() {
        Moosage created = repository.createMoosage("Test", "user1", "user1");
        
        Optional<Moosage> found = repository.getMoosageById(created.getId());
        
        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
        assertEquals("Test", found.get().getContent());
    }

    @Test
    public void testGetMoosageByIdNotFound() {
        Optional<Moosage> found = repository.getMoosageById(999L);
        
        assertFalse(found.isPresent());
    }

    @Test
    public void testToggleLikeAddsLike() {
        Moosage moosage = repository.createMoosage("Test", "user1", "user1");
        
        Optional<Moosage> updated = repository.toggleLike(moosage.getId(), "user2");
        
        assertTrue(updated.isPresent());
        assertTrue(updated.get().getLikedByUserIds().contains("user2"));
        assertEquals(1, updated.get().getLikedByUserIds().size());
    }

    @Test
    public void testToggleLikeRemovesLike() {
        Moosage moosage = repository.createMoosage("Test", "user1", "user1");
        repository.toggleLike(moosage.getId(), "user2");
        
        // Toggle again to remove
        Optional<Moosage> updated = repository.toggleLike(moosage.getId(), "user2");
        
        assertTrue(updated.isPresent());
        assertFalse(updated.get().getLikedByUserIds().contains("user2"));
        assertTrue(updated.get().getLikedByUserIds().isEmpty());
    }

    @Test
    public void testToggleLikeMultipleUsers() {
        Moosage moosage = repository.createMoosage("Test", "user1", "user1");
        
        repository.toggleLike(moosage.getId(), "user2");
        repository.toggleLike(moosage.getId(), "user3");
        repository.toggleLike(moosage.getId(), "user4");
        
        Optional<Moosage> updated = repository.getMoosageById(moosage.getId());
        
        assertTrue(updated.isPresent());
        assertEquals(3, updated.get().getLikedByUserIds().size());
        assertTrue(updated.get().getLikedByUserIds().contains("user2"));
        assertTrue(updated.get().getLikedByUserIds().contains("user3"));
        assertTrue(updated.get().getLikedByUserIds().contains("user4"));
    }

    @Test
    public void testToggleLikeNonexistentMoosage() {
        Optional<Moosage> result = repository.toggleLike(999L, "user1");
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateMoosage() {
        Moosage moosage = repository.createMoosage("Original content", "user1", "user1");
        
        Optional<Moosage> updated = repository.updateMoosage(moosage.getId(), "Updated content");
        
        assertTrue(updated.isPresent());
        assertEquals("Updated content", updated.get().getContent());
        assertTrue(updated.get().isEdited());
        assertEquals(moosage.getId(), updated.get().getId());
    }

    @Test
    public void testUpdateNonexistentMoosage() {
        Optional<Moosage> result = repository.updateMoosage(999L, "New content");
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateMoosageEmptyContent() {
        Moosage moosage = repository.createMoosage("Original", "user1", "user1");
        
        Optional<Moosage> updated = repository.updateMoosage(moosage.getId(), "");
        
        assertTrue(updated.isPresent());
        assertEquals("", updated.get().getContent());
        assertTrue(updated.get().isEdited());
    }

    @Test
    public void testDeleteMoosage() {
        Moosage moosage = repository.createMoosage("To delete", "user1", "user1");
        
        boolean deleted = repository.deleteMoosage(moosage.getId());
        
        assertTrue(deleted);
        
        Optional<Moosage> found = repository.getMoosageById(moosage.getId());
        assertFalse(found.isPresent());
    }

    @Test
    public void testDeleteNonexistentMoosage() {
        boolean deleted = repository.deleteMoosage(999L);
        
        assertFalse(deleted);
    }

    @Test
    public void testDeleteMoosageFromMultiple() {
        Moosage first = repository.createMoosage("First", "user1", "user1");
        Moosage second = repository.createMoosage("Second", "user2", "user2");
        Moosage third = repository.createMoosage("Third", "user3", "user3");
        
        repository.deleteMoosage(second.getId());
        
        List<Moosage> remaining = repository.getAllMoosages();
        assertEquals(2, remaining.size());
        assertTrue(remaining.stream().anyMatch(m -> m.getId().equals(first.getId())));
        assertTrue(remaining.stream().anyMatch(m -> m.getId().equals(third.getId())));
        assertFalse(remaining.stream().anyMatch(m -> m.getId().equals(second.getId())));
    }

    @Test
    public void testCreateMoosageIncrementsId() {
        Moosage first = repository.createMoosage("First", "user1", "user1");
        Moosage second = repository.createMoosage("Second", "user1", "user1");
        
        assertNotEquals(first.getId(), second.getId());
        assertTrue(second.getId() > first.getId());
    }

    @Test
    public void testCreateMoosageWithLongContent() {
        String longContent = "A".repeat(10000);
        Moosage moosage = repository.createMoosage(longContent, "user1", "user1");
        
        assertEquals(longContent, moosage.getContent());
        assertEquals(10000, moosage.getContent().length());
    }

    @Test
    public void testCreateMoosageWithSpecialCharacters() {
        String specialContent = "Test @#$% & *() 特殊文字 emoji 🦌\nNewline\tTab";
        Moosage moosage = repository.createMoosage(specialContent, "user1", "user1");
        
        assertEquals(specialContent, moosage.getContent());
    }

    @Test
    public void testPersistenceAcrossOperations() {
        // Create
        Moosage created = repository.createMoosage("Test", "user1", "user1");
        Long id = created.getId();
        
        // Like
        repository.toggleLike(id, "user2");
        
        // Update
        repository.updateMoosage(id, "Updated");
        
        // Retrieve and verify all changes persisted
        Optional<Moosage> retrieved = repository.getMoosageById(id);
        assertTrue(retrieved.isPresent());
        assertEquals("Updated", retrieved.get().getContent());
        assertTrue(retrieved.get().isEdited());
        assertTrue(retrieved.get().getLikedByUserIds().contains("user2"));
    }

    //Test JsonFileHandler that uses a custom test file path.
   
    private static class TestJsonFileHandler extends util.JsonFileHandler {
        private final String testFilePath;
        
        public TestJsonFileHandler(String testFilePath) {
            this.testFilePath = testFilePath;
        }
        
        @Override
        public String getDataFilePath(String defaultPath) {
            return testFilePath;
        }
    }
}

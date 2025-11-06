package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for JsonFileHandler.
 * Tests JSON file handling operations.
 */
class JsonFileHandlerTest {
    
    private JsonFileHandler fileHandler;
    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        fileHandler = new JsonFileHandler();
        testFile = Files.createTempFile("test-json-", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (testFile != null && Files.exists(testFile)) {
            Files.delete(testFile);
        }
    }

    @Test
    void testInitializeDataFile_CreatesFileWithData() throws IOException {
        File file = testFile.toFile();
        Files.delete(testFile); // Delete to test creation
        
        Map<String, Object> initialData = new HashMap<>();
        initialData.put("test", "value");
        
        fileHandler.initializeDataFile(file, initialData);
        
        assertTrue(file.exists(), "File should be created");
        assertTrue(file.length() > 0, "File should contain data");
    }

    @Test
    void testInitializeDataFile_DoesNotOverwriteExistingFile() throws IOException {
        File file = testFile.toFile();
        
        // Write initial data
        Map<String, Object> initialData = new HashMap<>();
        initialData.put("original", "data");
        fileHandler.writeJsonToFile(file, initialData);
        
        long originalLength = file.length();
        
        // Try to initialize with different data
        Map<String, Object> newData = new HashMap<>();
        newData.put("new", "data");
        fileHandler.initializeDataFile(file, newData);
        
        // File should remain unchanged
        assertEquals(originalLength, file.length(), "File should not be overwritten");
    }

    @Test
    void testWriteAndReadJsonFile() throws IOException {
        File file = testFile.toFile();
        
        Map<String, Object> data = new HashMap<>();
        data.put("username", "testuser");
        data.put("email", "test@example.com");
        
        fileHandler.writeJsonToFile(file, data);
        
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() { };
        Map<String, Object> readData = fileHandler.readJsonFromFile(file, typeRef);
        
        assertNotNull(readData, "Read data should not be null");
        assertEquals("testuser", readData.get("username"));
        assertEquals("test@example.com", readData.get("email"));
    }

    @Test
    void testGetDataFilePath_FindsProjectRoot() {
        String path = fileHandler.getDataFilePath("persistence/src/main/resources/data/data.json");
        
        assertNotNull(path, "Path should not be null");
        assertTrue(path.contains("persistence"), "Path should contain 'persistence'");
        assertTrue(path.contains("data.json"), "Path should contain 'data.json'");
    }

    @Test
    void testGetObjectMapper_NotNull() {
        assertNotNull(fileHandler.getObjectMapper(), "ObjectMapper should not be null");
    }
}

package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;


// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

/**
 * Utility class for handling JSON file operations.
 * Provides methods for file path resolution, initialization, and JSON read/write operations.
 */
public class JsonFileHandler {
    
    private final ObjectMapper objectMapper;

    public JsonFileHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Gets the absolute path to a data file relative to the project root.
     * Walks up the directory tree to find the project root, then resolves the relative path.
     * 
     * @param relativePath the relative path from project root (e.g., "persistence/src/main/resources/data/data.json")
     * @return the absolute path to the data file
     */
    public String getDataFilePath(String relativePath) {
        // Find project root by walking up until we find the persistence directory
        File dir = new File(System.getProperty("user.dir"));
        while (dir != null && !new File(dir, "persistence").exists()) {
            dir = dir.getParentFile();
        }
        return new File(dir != null ? dir : new File("."), relativePath).getAbsolutePath();
    }

    /**
     * Initializes a data file if it doesn't exist or is empty.
     * Creates parent directories if needed and writes initial data to the file.
     * 
     * @param dataFile the file to initialize
     * @param initialData the initial data structure to write to the file
     * @throws IOException if file initialization fails
     */
    public void initializeDataFile(File dataFile, Object initialData) throws IOException {
        if (!dataFile.exists() || dataFile.length() == 0) {
            dataFile.getParentFile().mkdirs();
            objectMapper.writeValue(dataFile, initialData);
        }
    }

    /**
     * Reads JSON data from a file.
     * 
     * @param dataFile the file to read from
     * @param typeReference the type reference for deserialization
     * @param <T> the type to deserialize to
     * @return the deserialized data
     * @throws IOException if file reading fails
     */
    public <T> T readJsonFromFile(File dataFile, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(dataFile, typeReference);
    }

    /**
     * Writes JSON data to a file.
     * 
     * @param dataFile the file to write to
     * @param data the data to write
     * @throws IOException if file writing fails
     */
    public void writeJsonToFile(File dataFile, Object data) throws IOException {
        objectMapper.writeValue(dataFile, data);
    }

    /**
     * Gets the ObjectMapper instance used by this handler.
     * Useful for advanced operations or configuration.
     * 
     * @return the ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

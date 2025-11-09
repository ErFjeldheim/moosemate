package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;


// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

//Utility class for handling JSON file operations.

public class JsonFileHandler {
    
    private final ObjectMapper objectMapper;

    public JsonFileHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    //Gets the absolute path to a data file relative to the project root.
     
    public String getDataFilePath(String relativePath) {
        // Find project root by walking up until we find the persistence directory
        File dir = new File(System.getProperty("user.dir"));
        while (dir != null && !new File(dir, "persistence").exists()) {
            dir = dir.getParentFile();
        }
        return new File(dir != null ? dir : new File("."), relativePath).getAbsolutePath();
    }

    // Initializes a data file if it doesn't exist or is empty.
    public void initializeDataFile(File dataFile, Object initialData) throws IOException {
        if (!dataFile.exists() || dataFile.length() == 0) {
            File parentDir = dataFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (!created && !parentDir.exists()) {
                    throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }
            objectMapper.writeValue(dataFile, initialData);
        }
    }

    // Reads JSON data from a file.
    public <T> T readJsonFromFile(File dataFile, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(dataFile, typeReference);
    }

    //Writes JSON data to a file.
    public void writeJsonToFile(File dataFile, Object data) throws IOException {
        objectMapper.writeValue(dataFile, data);
    }

    /**
     * Gets the ObjectMapper instance used by this handler.
     * Useful for advanced operations or configuration (e.g., registering modules).
     * 
     * Note: This intentionally exposes the internal ObjectMapper to allow
     * repository classes to register custom modules (like JavaTimeModule).
     * The ObjectMapper is thread-safe after configuration and modifications
     * are only made during initialization before any serialization operations.
     * 
     * @return the ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

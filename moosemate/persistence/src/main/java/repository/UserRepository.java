package repository;

import model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository class for managing User data persistence using JSON files.
 * Handles CRUD operations for User objects using the same pattern as UserService.
 */
public class UserRepository {
    
    private static final String DATA_FILE_PATH = "persistence/src/main/resources/data/data.json";
    
    private final ObjectMapper objectMapper;
    private final File dataFile;

    public UserRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataFile = new File(getDataFilePath());
        initializeDataFile();
    }

    /**
     * Gets the data file path. Can be overridden by subclasses when testing.
     * 
     * @return the path to the data file
     */
    protected String getDataFilePath() {
        // Find project root by walking up until we find the persistence directory
        File dir = new File(System.getProperty("user.dir"));
        while (dir != null && !new File(dir, "persistence").exists()) {
            dir = dir.getParentFile();
        }
        return new File(dir != null ? dir : new File("."), DATA_FILE_PATH).getAbsolutePath();
    }

    /**
     * Initializes the data file if it doesn't exist or is empty.
     */
    private void initializeDataFile() {
        try {
            if (!dataFile.exists() || dataFile.length() == 0) {
                dataFile.getParentFile().mkdirs();
                Map<String, Object> initialData = new HashMap<>();
                initialData.put("users", new ArrayList<Map<String, String>>());
                objectMapper.writeValue(dataFile, initialData);
            }
        } catch (IOException e) {
            System.err.println("Initializing the data file failed: " + e.getMessage());
        }
    }

    /**
     * Creates a new user in the repository.
     * 
     * @param username the username
     * @param email the email address
     * @param password the password (should be hashed before calling this method)
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(String username, String email, String password) {
        try {
            // Check if user already exists
            if (userExists(username)) {
                System.err.println("Username already exists: " + username);
                return false;
            }

            if (emailExists(email)) {
                System.err.println("Email already exists: " + email);
                return false;
            }

            String userID = UUID.randomUUID().toString();

            // Validate input using User class (this will throw exceptions for invalid data)
            User newUser = new User(username, email, password, userID);

            // Read existing data
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            // Create new user entry with validated data
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", newUser.getUsername());
            userMap.put("email", newUser.getEmail());
            userMap.put("password", newUser.getPassword());
            userMap.put("userID", newUser.getUserID());

            // Add new user to the list
            users.add(userMap);

            // Write to file to store user in json
            objectMapper.writeValue(dataFile, data);
            System.out.println("User successfully registered: " + username);
            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a user by username or email.
     * 
     * @param usernameOrEmail the username or email to search for
     * @return Optional containing the user data if found, empty otherwise
     */
    public Optional<Map<String, String>> findByUsernameOrEmail(String usernameOrEmail) {
        try {
            if (!dataFile.exists()) {
                return Optional.empty();
            }

            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            if (users == null || users.isEmpty()) {
                return Optional.empty();
            }

            return users.stream()
                    .filter(user -> usernameOrEmail.equals(user.get("username")) || 
                                   usernameOrEmail.equals(user.get("email")))
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Error finding user: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Checks if a username already exists.
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean userExists(String username) {
        return findByUsernameOrEmail(username).isPresent();
    }

    /**
     * Checks if an email already exists.
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return findByUsernameOrEmail(email).isPresent();
    }

    /**
     * Reads data from the JSON file.
     * 
     * @return the data map
     * @throws IOException if file reading fails
     */
    private Map<String, Object> readDataFromFile() throws IOException {
        if (!dataFile.exists()) {
            Map<String, Object> emptyData = new HashMap<>();
            emptyData.put("users", new ArrayList<Map<String, String>>());
            return emptyData;
        }
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
        return objectMapper.readValue(dataFile, typeReference);
    }
}
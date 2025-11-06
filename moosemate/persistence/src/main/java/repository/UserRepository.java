package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import model.User;
import org.springframework.stereotype.Repository;
import util.IdGenerator;
import util.JsonFileHandler;
import util.ValidationUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing User data persistence using JSON files.
 * Handles CRUD operations for User objects using the same pattern as UserService.
 */
@Repository
public class UserRepository {
    
    private static final String DATA_FILE_PATH = "persistence/src/main/resources/data/data.json";
    
    private final JsonFileHandler fileHandler;
    private final File dataFile;

    public UserRepository() {
        this(new JsonFileHandler());
    }

    /**
     * Constructor for testing that accepts a custom JsonFileHandler.
     * Package-visible to allow test customization across modules.
     * 
     * @param fileHandler the JsonFileHandler to use
     */
    public UserRepository(JsonFileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.dataFile = new File(fileHandler.getDataFilePath(DATA_FILE_PATH));
        initializeDataFile();
    }

    /**
     * Initializes the data file if it doesn't exist or is empty.
     */
    private void initializeDataFile() {
        try {
            Map<String, Object> initialData = new HashMap<>();
            initialData.put("users", new ArrayList<Map<String, String>>());
            fileHandler.initializeDataFile(dataFile, initialData);
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

            String userID = IdGenerator.generateUserId();

            // Validate input using User class (this will throw exceptions for invalid data)
            User newUser = new User(username, email, password, userID);

            // Read existing data
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");
            
            // Handle null users array
            if (users == null) {
                System.err.println("Users array is null or missing in data file");
                return false;
            }

            // Create new user entry with validated data
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", newUser.getUsername());
            userMap.put("email", newUser.getEmail());
            userMap.put("password", newUser.getPassword());
            userMap.put("userID", newUser.getUserID());

            // Add new user to the list
            users.add(userMap);

            // Write to file to store user in json
            fileHandler.writeJsonToFile(dataFile, data);

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
        // Handle null or empty input
        if (ValidationUtils.isNullOrEmpty(usernameOrEmail)) {
            return Optional.empty();
        }
        
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
                    .filter(user -> usernameOrEmail.equals(user.get("username"))
                                   || usernameOrEmail.equals(user.get("email")))
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Error finding user: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds a user by userID, so that MoosageRepository can convert authorID (String UUID) 
     * to User object when a moosage loads.
     * 
     * @param userId the user ID to search for
     * @return Optional containing the User object if found, empty otherwise
     */
    public Optional<User> getUserById(String userId) {
        if (ValidationUtils.isNullOrEmpty(userId)) {
            return Optional.empty();
        }
        
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
                    .filter(user -> userId.equals(user.get("userID")))
                    .findFirst()
                    .map(userMap -> new User(
                        userMap.get("username"),
                        userMap.get("email"),
                        userMap.get("password"),
                        userMap.get("userID")
                    ));

        } catch (IOException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
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
        try {
            if (username == null || !dataFile.exists()) {
                return false;
            }

            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            if (users == null || users.isEmpty()) {
                return false;
            }

            return users.stream()
                    .anyMatch(user -> username.equals(user.get("username")));

        } catch (IOException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an email already exists.
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        try {
            if (email == null || !dataFile.exists()) {
                return false;
            }

            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            if (users == null || users.isEmpty()) {
                return false;
            }

            return users.stream()
                    .anyMatch(user -> email.equals(user.get("email")));

        } catch (IOException e) {
            System.err.println("Error checking if email exists: " + e.getMessage());
            return false;
        }
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
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() { };
        return fileHandler.readJsonFromFile(dataFile, typeReference);
    }
}

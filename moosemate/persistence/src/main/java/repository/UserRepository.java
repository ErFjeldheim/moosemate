package repository;

import model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

 // Repository class for managing User data persistence using JSON files.
 // Handles CRUD operations for User objects using the same pattern as UserService.
@Repository
public class UserRepository {
    
    private static final String DATA_FILE_PATH = "persistence/src/main/resources/data/data.json";
    
    private final ObjectMapper objectMapper;
    private final File dataFile;

    public UserRepository() {
        this(null);
    }

    public UserRepository(String dataFilePath) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Use provided path for testing, otherwise use getDataFilePath()
        if (dataFilePath != null && !dataFilePath.equals(DATA_FILE_PATH)) {
            // Test mode: use provided path directly
            this.dataFile = new File(dataFilePath);
        } else {
            // Production mode: find correct path
            this.dataFile = new File(getDataFilePath());
        }
        initializeDataFile();
    }

     // Gets the data file path.
    protected String getDataFilePath() {
        // Find project root by walking up until we find the persistence directory
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        while (dir != null && !new File(dir, "persistence").exists()) {
            dir = dir.getParentFile();
        }
        // Once we find the moosemate root, construct path directly to persistence module
        if (dir != null) {
            File persistenceDir = new File(dir, "persistence");
            File dataFile = new File(persistenceDir, "src/main/resources/data/data.json");
            return dataFile.getAbsolutePath();
        }
        return new File(DATA_FILE_PATH).getAbsolutePath();
    }


    // Initializes the data file if it doesn't exist or is empty
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

    // Creates a new user in the repository.
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

    // Finds a user by username or email.
    public Optional<Map<String, String>> findByUsernameOrEmail(String usernameOrEmail) {
        // Handle null or empty input
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
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
                    .filter(user -> usernameOrEmail.equals(user.get("username")) || 
                                   usernameOrEmail.equals(user.get("email")))
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Error finding user: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Finds a user by userID, so that MoosageRepository can convert authorID (String UUID) to User object when a moosage loads
    public Optional<User> getUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
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

    // Checks if a username already exists
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

    // Checks if an email already exists
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

    // Reads data from the JSON file
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

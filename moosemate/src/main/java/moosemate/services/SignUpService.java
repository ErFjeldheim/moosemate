package moosemate.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import moosemate.core.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for handling user sign-up operations.
 * Stores user credentials in JSON format for permanent registration.
 */
public class SignUpService {

    private static final String DATA_FILE_PATH = "data.json";
    private final ObjectMapper objectMapper;
    private final File dataFile;

    
    public SignUpService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Makes JSON readable using Jackson, which handles conversions from Java objects to JSON and vice versa.
        this.dataFile = new File(DATA_FILE_PATH);
        
        initializeDataFile();
    }

  
    private void initializeDataFile() {
        try {
            if (!dataFile.exists() || dataFile.length() == 0) { //checks if file exists or is empty
                Map<String, Object> initialData = new HashMap<>(); //creates a new map including a list of users
                initialData.put("users", new ArrayList<Map<String, String>>()); // initializes the list of users
                objectMapper.writeValue(dataFile, initialData); // Create new file with empty users list


            }
            //error handling
        } catch (IOException e) { 
            System.err.println("Initializing the data file failed: " + e.getMessage());
        }
    }

    //Registers a new user by storing their username, email and password in JSON format.
        public boolean signUpUser(String username, String email, String password) throws IllegalArgumentException {
        try {
            // Validate input using User class (this will throw exceptions for invalid data)
            User newUser = new User(username, email, password);
            
            // Read existing data
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            // Check if username already exists
            if (userExists(username)) {
                System.err.println("Username already exists: " + username);
                return false;
            }

            // Check if email already exists
            if (emailExists(email)) {
                System.err.println("Email already exists: " + email);
                return false;
            }

            // Create new user entry with validated data
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", newUser.getUsername());
            userMap.put("email", newUser.getEmail());
            userMap.put("password", newUser.getPassword());

            // Add new user to the list
            users.add(userMap);

            // Write back to file
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
//Alternative method that accepts a User object, for better flexibility. 

    public boolean signUpUser(User user) {
        try {
            return signUpUser(user.getUsername(), user.getEmail(), user.getPassword());
        } catch (Exception e) {
            System.err.println("Error processing User object: " + e.getMessage());
            return false;
        }
    }

    
    //Reads data from the JSON file.
    private Map<String, Object> readDataFromFile() throws IOException {
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
        return objectMapper.readValue(dataFile, typeReference);
    }

    // Gets the list of all registered users (for testing/admin purposes).
    public List<String> getAllUsernames() {
        try {
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");
            
            return users.stream()
                    .map(user -> user.get("username"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading usernames: " + e.getMessage());
            return new ArrayList<>();
        }
    }

   
    // Checks if a user exists with the given username.
    public boolean userExists(String username) {
        try {
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");
            
            return users.stream()
                    .anyMatch(user -> username.equals(user.get("username")));
        } catch (IOException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    //Check if an email is already registered
    public boolean emailExists(String email) {
        try {
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");
            
            return users.stream()
                    .anyMatch(user -> email.equals(user.get("email")));
        } catch (IOException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        }
    }
}

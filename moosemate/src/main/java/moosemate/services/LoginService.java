package moosemate.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class LoginService {

    private static final String DATA_FILE_PATH = "data.json";
    private final ObjectMapper objectMapper;
    private final File dataFile;

    public LoginService() {
        this.objectMapper = new ObjectMapper();
        this.dataFile = new File(DATA_FILE_PATH);
    }

//authentication of user input to see if it matches stored data
    public boolean loginUser(String usernameOrEmail, String password) {
        try {
            // Check if data file exists
            if (!dataFile.exists()) {
                System.err.println("No data file exists");
                return false;
            }

            // Read existing data
            Map<String, Object> data = readDataFromFile();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> users = (List<Map<String, String>>) data.get("users");

            // Check if users list exists and is not empty
            if (users == null || users.isEmpty()) {
                System.err.println("No users registered yet. Please register first.");
                return false;
            }

            // Find user by username or email and validate password
            for (Map<String, String> user : users) {
                if (usernameOrEmail.equals(user.get("username")) || 
                    usernameOrEmail.equals(user.get("email"))) {
                    
                    if (password.equals(user.get("password"))) {
                        System.out.println("Login successful for: " + usernameOrEmail);
                        return true;
                    } else {
                        System.err.println("Invalid password for: " + usernameOrEmail);
                        return false;
                    }
                }
            }

            // User not found
            System.err.println("User not found: " + usernameOrEmail);
            return false;

        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    //Checks if a username exists in the system (for login validation).
     
    public boolean userExists(String username) {
        try {
            if (!dataFile.exists()) {
                return false;
            }

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

    
    //Reads data from the JSON file.
    private Map<String, Object> readDataFromFile() throws IOException {
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
        return objectMapper.readValue(dataFile, typeReference);
    }
}

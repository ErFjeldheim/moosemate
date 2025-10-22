package service;

import model.User;
import repository.UserRepository;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Service class for handling user data operations.
 * Acts as a bridge between other services and the persistence layer.
 * Focuses on user CRUD operations and data validation.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // Constructor for dependency injection (useful for testing)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user with hashed password.
     * 
     * @param username the username
     * @param email the email address
     * @param hashedPassword the already hashed password
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(String username, String email, String hashedPassword) {
        return userRepository.createUser(username, email, hashedPassword);
    }

    /**
     * Finds a user by username or email.
     * 
     * @param usernameOrEmail the username or email to search for
     * @return User object if found, null otherwise
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        Optional<Map<String, String>> userDataOptional = userRepository.findByUsernameOrEmail(usernameOrEmail);
        
        if (userDataOptional.isPresent()) {
            Map<String, String> userData = userDataOptional.get();
            
            try {
                // Convert Map to User object
                User user = new User(
                    userData.get("username"),
                    userData.get("email"),
                    userData.get("password"),
                    userData.get("userID")
                );
                
                return user;
            } catch (IllegalArgumentException e) {
                // User data is incomplete or invalid, return null
                return null;
            }
        }
        
        return null;
    }

    /**
     * Checks if a username already exists.
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean userExists(String username) {
        return userRepository.userExists(username);
    }

    /**
     * Checks if an email already exists.
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.emailExists(email);
    }
}
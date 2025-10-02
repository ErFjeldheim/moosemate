package impl;

import repository.UserRepository;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for handling user data operations.
 * Acts as a bridge between other services and the persistence layer.
 * Focuses on user CRUD operations and data validation.
 */
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
     * @return Optional containing the user data if found, empty otherwise
     */
    public Optional<Map<String, String>> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
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
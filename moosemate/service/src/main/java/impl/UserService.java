package impl;

import repository.UserRepository;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for handling user business logic.
 * Acts as a bridge between controllers and the persistence layer.
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
     * Creates a new user with the given password (should be hashed before calling this method).
     * 
     * @param username the username
     * @param email the email address
     * @param password the password (should be already hashed)
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(String username, String email, String password) {
        return userRepository.createUser(username, email, password);
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

    /**
     * Gets a user by username.
     * 
     * @param username the username
     * @return Optional containing the user data if found
     */
    public Optional<Map<String, String>> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Gets a user by email.
     * 
     * @param email the email
     * @return Optional containing the user data if found
     */
    public Optional<Map<String, String>> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
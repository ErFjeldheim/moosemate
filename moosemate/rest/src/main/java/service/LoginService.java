package service;

import java.util.Map;
import java.util.Optional;

/**
 * Service class for handling user authentication operations.
 * Handles login business logic and delegates user data operations to UserService.
 */
public class LoginService {

    private final UserService userService;
    private final PasswordService passwordService;

    public LoginService() {
        this.userService = new UserService();
        this.passwordService = new PasswordService();
    }

    // Constructor for dependency injection (useful for testing)
    public LoginService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    /**
     * Authenticates a user by username/email and password.
     * Uses UserService for user lookup and PasswordService for verification.
     * 
     * @param usernameOrEmail username or email to authenticate
     * @param password plain text password to verify
     * @return true if authentication is successful, false otherwise
     */
    public boolean loginUser(String usernameOrEmail, String password) {
        try {
            // Check for null inputs
            if (usernameOrEmail == null || password == null) {
                return false;
            }

            // Find user using UserService
            Optional<Map<String, String>> userOpt = userService.findByUsernameOrEmail(usernameOrEmail);
            
            if (!userOpt.isPresent()) {
                System.err.println("User not found: " + usernameOrEmail);
                return false;
            }

            Map<String, String> user = userOpt.get();

            // Validate password using PasswordService
            if (validatePassword(user, password)) {
                System.out.println("Login successful for: " + usernameOrEmail);
                return true;
            } else {
                System.err.println("Invalid password for: " + usernameOrEmail);
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates a password against the stored hashed password.
     * 
     * @param user the user data containing the hashed password
     * @param rawPassword the plain text password to validate
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(Map<String, String> user, String rawPassword) {
        String storedHashedPassword = user.get("password");
        return passwordService.verifyPassword(rawPassword, storedHashedPassword);
    }
}
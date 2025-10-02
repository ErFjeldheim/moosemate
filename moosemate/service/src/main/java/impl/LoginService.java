package impl;

import repository.UserRepository;
import java.util.Map;
import java.util.Optional;

public class LoginService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public LoginService() {
        this.userRepository = new UserRepository();
        this.passwordService = new PasswordService();
    }

    /**
     * Delegates to UserRepository for user lookup and password validation.
     * Verifies password by using BCrypt
     * 
     * @param usernameOrEmail username or email to authenticate
     * @param password plain text password to verify
     * @return true if authentication is successful, false otherwise
     */
    public boolean loginUser(String usernameOrEmail, String password) {
        try {
            // Find user by username or email
            Optional<Map<String, String>> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail);
            
            if (!userOpt.isPresent()) {
                System.err.println("User not found: " + usernameOrEmail);
                return false;
            }

            Map<String, String> user = userOpt.get();

            // Validate password using BCrypt verification
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
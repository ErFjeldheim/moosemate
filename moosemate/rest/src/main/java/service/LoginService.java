package service;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.ValidationUtils;

// Service class for handling user authentication operations.
// Handles login business logic and delegates user data operations to UserService.
@Service
public class LoginService {

    private final UserService userService;
    private final PasswordService passwordService;

    // Spring Constructor injection
    @Autowired
    public LoginService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    // Log in user - returns User if successful, otherwise null
    public User loginUser(String usernameOrEmail, String password) {
        
        // Validation if input is null or empty
        ValidationUtils.requireNonEmpty(usernameOrEmail, "Username/email");
        ValidationUtils.requireNonEmpty(password, "Password");

        // Find user
        User user = userService.findByUsernameOrEmail(usernameOrEmail);

        if (user == null) {
            System.out.println("User not found: " + usernameOrEmail);
            return null;
        }

        // Validate password
        boolean isValidPassword = passwordService.verifyPassword(password, user.getPassword());

        if (isValidPassword) {
            System.out.println("Login successful for: " + user.getUsername());
            return user;  // Return User object
        } else {
            System.out.println("Invalid password for: " + user.getUsername());
            return null;
        }
    }
}
package service;

import org.springframework.stereotype.Service;
import util.ValidationUtils;

// Service class for handling user sign-up operations.
// Handles sign-up business logic and delegates user creation to UserService.
@Service
public class SignUpService {

    private final UserService userService;
    private final PasswordService passwordService;

    // Spring Constructor injection
    public SignUpService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    //Registers a new user by handling validation, password hashing, and user creation.
    public boolean signUpUser(String username, String email, String password) throws IllegalArgumentException {
        
        // validation before checking database
        ValidationUtils.validateUsername(username);
        ValidationUtils.validateEmail(email);
        ValidationUtils.validatePassword(password);

        // Check if user already exists using UserService
        if (userService.userExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userService.emailExists(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash the password before storing
        String hashedPassword = passwordService.hashPassword(password);
        
        // Delegate to UserService for actual user creation
        return userService.createUser(username, email, hashedPassword);
    }
}
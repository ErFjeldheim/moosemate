package service;

import org.springframework.stereotype.Service;

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
        // Validate inputs
        if (username == null || email == null || password == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        

        // Check if user already exists using UserService
        if (userService.userExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userService.emailExists(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

         // Validate email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format: ...@email.com");
        }

        //ensures stronger password
        //at least 8 characters/numbers long
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters");
            
        }
        // at least 1 digit
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }

        // Hash the password before storing
        String hashedPassword = passwordService.hashPassword(password);
        
        // Delegate to UserService for actual user creation
        return userService.createUser(username, email, hashedPassword);
    }
}
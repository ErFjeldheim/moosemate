package service;

/**
 * Service class for handling user sign-up operations.
 * Handles sign-up business logic and delegates user creation to UserService.
 */
public class SignUpService {

    private final UserService userService;
    private final PasswordService passwordService;

    public SignUpService() {
        this.userService = new UserService();
        this.passwordService = new PasswordService();
    }

    // Constructor for dependency injection (useful for testing)
    public SignUpService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    /**
     * Registers a new user by handling validation, password hashing, and user creation.
     * 
     * @param username the username
     * @param email the email address
     * @param password the plain text password (will be hashed)
     * @return true if user was successfully registered, false otherwise
     * @throws IllegalArgumentException if input validation fails
     */
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
            throw new IllegalArgumentException("Invalid email format");
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
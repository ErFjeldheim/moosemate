package moosemate.services;

/**
 * Service class for handling user sign-up operations.
 * Delegates user creation to UserService which handles password hashing and data persistence.
 */
public class SignUpService {

    private final UserService userService;

    
    public SignUpService() {
        this.userService = new UserService();
    }

    /**
     * Registers a new user by delegating to UserService which handles password hashing and validation.
     * This class is kept until further decision, as we might implement email verification during signup later on.
     * Might be deleted and implenented in UserService if no further logic is implemented
     * 
     * @param username the username
     * @param email the email address
     * @param password the plain text password (will be hashed by UserService)
     * @return true if user was successfully registered, false otherwise
     * @throws IllegalArgumentException if input validation fails
     */
    public boolean signUpUser(String username, String email, String password) throws IllegalArgumentException {
        // Validate inputs
        if (username == null || email == null || password == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        // Delegate to UserService which handles password hashing and data persistence
        return userService.createUser(username, email, password);
    }
}

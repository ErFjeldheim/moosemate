package impl;

import repository.UserRepository;

/**
 * Service class for handling user sign-up operations.
 * Delegates user creation to UserRepository which handles password hashing and data persistence.
 */
public class SignUpService {

    private final UserRepository userRepository;

    
    public SignUpService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Registers a new user by delegating to UserRepository which handles password hashing and validation.
     * This class is kept until further decision, as we might implement email verification during signup later on.
     * Might be deleted and implenented in UserRepository if no further logic is implemented
     * 
     * @param username the username
     * @param email the email address
     * @param password the plain text password (will be hashed by UserRepository)
     * @return true if user was successfully registered, false otherwise
     * @throws IllegalArgumentException if input validation fails
     */
    public boolean signUpUser(String username, String email, String password) throws IllegalArgumentException {
        // Validate inputs
        if (username == null || email == null || password == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        // Delegate to UserRepository which handles password hashing and data persistence
        return userRepository.createUser(username, email, password);
    }
}
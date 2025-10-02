package impl;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Service for handling password operations using BCrypt hashing.
 * Provides secure password hashing and verification functionality.
 */
public class PasswordService {
    
    private static final int BCRYPT_ROUNDS = 12;
    
    /**
     * Hashes the password using BCrypt.
     * 
     * @param plainPassword the plain password
     * @return the BCrypt hashed password
     * @throws IllegalArgumentException if plainPassword is null or empty
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verifies a raw password against a hashed password.
     * 
     * @param plainPassword the non-encrypted password to be verified
     * @param hashedPassword the BCrypt hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false; 
        }
    }
}
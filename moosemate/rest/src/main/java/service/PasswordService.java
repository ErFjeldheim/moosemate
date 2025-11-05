package service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import util.ValidationUtils;

// Service for handling password operations using BCrypt hashing.
// Provides secure password hashing and verification functionality.
@Service
public class PasswordService {
    
    private static final int BCRYPT_ROUNDS = 12;
    
    // Hashes the password using BCrypt.
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    // Verifies a raw password against a hashed password.
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        ValidationUtils.requireNonEmpty(plainPassword, "Password");
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false; 
        }
    }
}
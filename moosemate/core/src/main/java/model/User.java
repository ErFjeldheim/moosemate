package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import util.ValidationUtils;

public final class User {
    
    private String username;
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Only deserialize, never serialize
    private String password;
    
    private String userID;

    // Default constructor for setting values later
    public User() {
    }

    // Constructor
    public User(String username, String email, String password,
                String userID) {
        // Validate parameters first to prevent finalizer attacks
        if (ValidationUtils.anyNullOrEmpty(username, email, password, userID)) {
            throw new IllegalArgumentException(
                    "User parameters cannot be null or empty");
        }
        
        // Set fields directly to avoid potential issues with setter methods
        this.username = username.trim();
        this.email = email.trim();
        this.password = password;
        this.userID = userID.trim();
        
        // Additional validation after assignment to ensure object is fully constructed
        validateUserData();
    }
    
    // Private method to validate user data integrity (if cannot create valid user)
    private void validateUserData() {
        if (ValidationUtils.anyNullOrEmpty(this.username, this.email, this.password, this.userID)) {
            throw new IllegalArgumentException("User parameters cannot be empty");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        ValidationUtils.validateUsername(username);
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ValidationUtils.validateEmail(email);
        this.email = email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // password is hidden
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        ValidationUtils.validatePassword(password);
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}

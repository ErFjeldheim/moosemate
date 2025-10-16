package model;

public final class User {
    
    private String username;
    private String email;
    private String password;
    private String userID;

    // Default constructor for setting values later
    public User() {
    }

    // Constructor
    public User(String username, String email, String password, String userID) {
        // Validate parameters first to prevent finalizer attacks
        if (username == null || email == null || password == null || userID == null) {
            throw new IllegalArgumentException("User parameters cannot be null");
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
        if (this.username.isEmpty() || this.email.isEmpty() || this.password.isEmpty() || this.userID.isEmpty()) {
            throw new IllegalArgumentException("User parameters cannot be empty");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() > 20) {
            throw new IllegalArgumentException("Username must be less than 20 characters");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format: ...@email.com");
        }
        
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter from A-Z");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
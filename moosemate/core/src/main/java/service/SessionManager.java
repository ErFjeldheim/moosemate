package service;

import dto.LoginResponse;

// singleton class for managing user session in application. 
// stores session token and user information for current user.

public class SessionManager {
    private static SessionManager instance;

    private String sessionToken;
    private String username;
    private String email;
    private String userId;

    // private constructor to prevent multiple instances
    private SessionManager() {} 

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // initialize session with login response data
    public void login(LoginResponse loginResponse) {
        this.sessionToken = loginResponse.getSessionToken();
        this.username = loginResponse.getUsername();
        this.email = loginResponse.getEmail();
        this.userId = loginResponse.getUserId();
    }

    // clear session data on logout
    public void logout() {
        this.sessionToken = null;
        this.username = null;
        this.email = null;
        this.userId = null;
    }

    // checks if user is currently logged in
    public boolean isLoggedIn() {
        return sessionToken != null && !sessionToken.isEmpty();
    }

    // returns current session token
    public String getSessionToken() {
        return sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }
}

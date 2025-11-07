package service;

import dto.LoginResponse;

// singleton class for managing user session in application. 
// stores session token and user information for current user.

// Bill Pugh singleton pattern - safe when multiple threads call getInstance() at once.

public class SessionManager {
    
    // Private constructor to prevent instantiation
    private SessionManager() { }
    
    // Singleton holder (Bill Pugh pattern - thread-safe without synchronization)
    private static class SingletonHolder {
        private static final SessionManager INSTANCE = new SessionManager();
    }
    
    public static SessionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private String sessionToken;
    private String username;
    private String email;
    private String userId;

    // initialize session with login response data
    public void login(LoginResponse loginResponse) {
        this.sessionToken = loginResponse.getSessionToken();
        this.username = loginResponse.getUser().getUsername();
        this.email = loginResponse.getUser().getEmail();
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

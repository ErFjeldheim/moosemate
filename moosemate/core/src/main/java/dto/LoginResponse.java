package dto;

// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

// Response DTO for login requests.
public class LoginResponse {
    private UserDto user;
    private String sessionToken;
    private String userId;  // Internal userId for session management, not part of public user profile

    public LoginResponse() { }

    public LoginResponse(UserDto user, String sessionToken, String userId) {
        // Defensive copy to avoid exposing internal representation
        this.user = user != null ? new UserDto(user) : null;
        this.sessionToken = sessionToken;
        this.userId = userId;
    }

    public UserDto getUser() {
        // Defensive copy to avoid exposing internal representation
        return user != null ? new UserDto(user) : null;
    }

    public void setUser(UserDto user) {
        // Defensive copy to avoid exposing internal representation
        this.user = user != null ? new UserDto(user) : null;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

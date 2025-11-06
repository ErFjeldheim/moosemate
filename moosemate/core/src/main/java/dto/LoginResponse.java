package dto;

public class LoginResponse {
    private UserDto user;
    private String sessionToken;
    private String userId;  // Internal userId for session management, not part of public user profile

    public LoginResponse() { }

    public LoginResponse(UserDto user, String sessionToken, String userId) {
        this.user = user;
        this.sessionToken = sessionToken;
        this.userId = userId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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

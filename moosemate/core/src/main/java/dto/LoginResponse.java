package dto;

public class LoginResponse {
    private String username;
    private String email;
    private String sessionToken;

    public LoginResponse() {}

    public LoginResponse(String username, String email, String sessionToken) {
        this.username = username;
        this.email = email;
        this.sessionToken = sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
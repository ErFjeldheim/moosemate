package dto;

// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

// Request DTO for sign-up requests.
public class SignUpRequest {
    private String username;
    private String password;
    private String email;

    public SignUpRequest() {
    }

    public SignUpRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

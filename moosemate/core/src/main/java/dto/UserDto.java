package dto;

public class UserDto {
    private String username;
    private String email;

    public UserDto() {
    }

    // only returns: username, email

    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Factory method for converting from User model
    public static UserDto fromUser(model.User user) {
        return new UserDto(user.getUsername(), user.getEmail());
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
}

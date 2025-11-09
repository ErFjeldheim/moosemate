package dto;

// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

// Used for transferring user data without sensitive information.
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

    // Copy constructor for defensive copying
    public UserDto(UserDto other) {
        if (other != null) {
            this.username = other.username;
            this.email = other.email;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return java.util.Objects.equals(username, userDto.username)
                && java.util.Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, email);
    }
}


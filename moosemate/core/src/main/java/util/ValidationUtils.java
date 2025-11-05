package util;

// class for gathering validation handling for user, email, password, etc

public class ValidationUtils {
    
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // email validation

    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.matches(EMAIL_REGEX);
    }

    public static void validateEmail(String email) {
        requireNonEmpty(email, "Email");
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: example@email.com");
        }
    }

    // password validation

    public static void validatePassword(String password) {
        requireNonEmpty(password, "Password");
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
    }

    // username validation

    public static void validateUsername(String username) {
        requireNonEmpty(username, "Username");
        if (username.length() > 20) {
            throw new IllegalArgumentException("Username must be less than 20 characters");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
    }

    // null/empty validation
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void requireNonEmpty(String value, String fieldName) { // fieldname for error logging
        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }
}

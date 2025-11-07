package util;

import java.util.UUID;

// class for gathering UUID (unique identifier) generations

public class IdGenerator {
    
    private IdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateUUID()  {
        return UUID.randomUUID().toString();
    }

    // UUID with prefix for easy identification
    public static String generateUserId() {
        return "USER-" + UUID.randomUUID().toString();
    }
}

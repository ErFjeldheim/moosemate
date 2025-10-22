package service;

import model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    
    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();

    // creates new session, returns unique UUID token for each session
    public String createSession(User user) {
        String sessionToken = UUID.randomUUID().toString();
        activeSessions.put(sessionToken, user);
        System.out.println("Created session for user " + user.getUsername() + sessionToken);
        return sessionToken;
    }

    // returns user from session token
    public User getUser(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    // checks if session is valid
    public boolean isValidSession(String sessionToken) {
        return sessionToken != null && activeSessions.containsKey(sessionToken);
    }

    // removes session when user logs out
    public void terminateSession(String sessionToken) {
        User user = activeSessions.remove(sessionToken);
        if (user != null) {
            System.out.println("Logout successful for: " + user.getUsername());
        }
    }
}
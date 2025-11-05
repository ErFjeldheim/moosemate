package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import model.User;
import util.IdGenerator;

@Service
public class SessionService {
    
    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();

    // creates new session, returns unique UUID token for each session
    public String createSession(User user) {
        String sessionToken = IdGenerator.generateUUID();
        activeSessions.put(sessionToken, user);
        System.out.println("Created session for user " + user.getUsername() + " with sessionID: " + sessionToken);
        return sessionToken;
    }

    // returns user from session token
    public User getUser(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    // returns user ID from session token
    public String getUserIdByToken(String sessionToken) {
        User user = activeSessions.get(sessionToken);
        return (user != null) ? user.getUserID() : null;
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
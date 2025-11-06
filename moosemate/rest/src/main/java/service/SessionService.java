package service;

import model.User;
import org.springframework.stereotype.Service;
import util.IdGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    
    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();

    // creates new session, returns unique UUID token for each session
    public String createSession(User user) {
        String sessionToken = IdGenerator.generateUUID();
        activeSessions.put(sessionToken, user);
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

    public void terminateSession(String sessionToken) {
        activeSessions.remove(sessionToken);
    }
}

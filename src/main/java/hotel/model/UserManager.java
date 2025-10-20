package hotel.model;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final Map<String, String> users = new HashMap<>();
    private String loggedInUser; // ✅ keeps track of current session

    public UserManager() {
        // Default test account
        users.put("guest@example.com", "password123");
    }

    // ✅ Add new users (signup)
    public void addUser(String email, String password) {
        users.put(email.toLowerCase(), password);
    }

    // ✅ Authenticate and set session
    public boolean authenticate(String email, String password) {
        if (email == null || password == null) return false;
        String stored = users.get(email.toLowerCase());
        boolean success = stored != null && stored.equals(password);
        if (success) {
            loggedInUser = email;
        }
        return success;
    }

    // ✅ Return the logged-in user’s email
    public String getLoggedInUser() {
        return loggedInUser;
    }

    // ✅ Log out current user
    public void logout() {
        loggedInUser = null;
    }

    // Optional helper
    public boolean userExists(String email) {
        return users.containsKey(email.toLowerCase());
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        addUser(username, password);
        return true;
    }
}

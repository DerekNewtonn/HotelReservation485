package hotel.model;

import hotel.service.Db;
import java.sql.*;
import java.util.Objects;

public class UserManager {
    private String loggedInUser; // email for current session

    public boolean authenticate(String userOrEmail, String password) {
        if (userOrEmail == null || password == null) return false;
        userOrEmail = userOrEmail.trim();
        password    = password.trim();
        if (userOrEmail.isEmpty() || password.isEmpty()) return false;

        final String sql = """
            SELECT email
            FROM users
            WHERE (LOWER(username)=LOWER(?) OR LOWER(email)=LOWER(?))
              AND password_hash = SHA2(?, 256)
            LIMIT 1
        """;
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userOrEmail);
            ps.setString(2, userOrEmail);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    loggedInUser = rs.getString("email");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // optional: surface this in your UI
        }
        return false;
    }

// Backward-compatible overload: treat the single identifier as both username and email
public boolean register(String userOrEmail, String password) {
    return register(userOrEmail, userOrEmail, password);
}

public boolean register(String username, String email, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        if (username.isBlank() || email.isBlank() || password.isBlank()) return false;

        final String check = "SELECT 1 FROM users WHERE LOWER(username)=LOWER(?) OR LOWER(email)=LOWER(?) LIMIT 1";
        final String insert = "INSERT INTO users(username,email,password_hash) VALUES (?,?,SHA2(?,256))";
        try (Connection c = Db.get()) {
            try (PreparedStatement ps = c.prepareStatement(check)) {
                ps.setString(1, username);
                ps.setString(2, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return false;
                }
            }
            try (PreparedStatement ps = c.prepareStatement(insert)) {
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLoggedInUser() { return loggedInUser; }
    public void logout() { loggedInUser = null; }
}

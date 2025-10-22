package hotel.model;

// Db helper moved inside this class (see bottom)
import hotel.service.Db;
import java.sql.*;
import java.util.Objects;

public class UserManager {
    private String loggedInUser; // email (session)

    // --- AUTH: username OR email + password
    public boolean authenticate(String userOrEmail, String password) {
        if (userOrEmail == null || password == null || userOrEmail.isBlank() || password.isBlank())
            return false;

        final String sql = """
            SELECT email
            FROM users
            WHERE (LOWER(username)=LOWER(?) OR LOWER(email)=LOWER(?))
              AND password_hash = SHA2(?, 256)
            LIMIT 1
        """;

        try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
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
            e.printStackTrace(); // consider showing a dialog in your UI
        }
        return false;
    }

    // --- SIGN UP (returns false if username/email exists)
    public boolean register(String username, String email, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        // (Optional) quick, basic validation
        if (username.isBlank() || email.isBlank() || password.isBlank()) return false;
        if (!email.contains("@")) return false; // simple check; replace with a better regex if you like

        String checkSql  = "SELECT 1 FROM users WHERE LOWER(username)=LOWER(?) OR LOWER(email)=LOWER(?) LIMIT 1";
        String insertSql = "INSERT INTO users(username, email, password_hash) VALUES (?,?,SHA2(?,256))";

        try (Connection c = Db.get()) {
            // already taken?
            try (PreparedStatement ps = c.prepareStatement(checkSql)) {
                ps.setString(1, username);
                ps.setString(2, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return false; // username or email exists
                }
            }
            // create user
            try (PreparedStatement ps = c.prepareStatement(insertSql)) {
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            // unique constraint hit (race condition)
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLoggedInUser() { return loggedInUser; }
    public void logout() { loggedInUser = null; }

    // Simple Db helper — configure get() to return a real Connection for your environment
    private static class Db {
        public static Connection get() throws SQLException {
            // Example using DriverManager (uncomment and configure):
            // return DriverManager.getConnection("jdbc:yourdb://host:port/dbname","user","pass");
            throw new SQLException("Db.get() is not configured. Configure the Db.get() method to return a valid Connection.");
        }
    }
}


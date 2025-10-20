package hotel.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String passwordHash; // 🔐 stored securely
    private final List<Reservation> reservations = new ArrayList<>();

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public List<Reservation> getReservations() { return reservations; }

    // Reservation management
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", reservations=" + reservations.size() +
                '}';
    }
}
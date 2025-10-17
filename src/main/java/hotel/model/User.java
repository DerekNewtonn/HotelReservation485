package hotel.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private final List<Reservation> reservations = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public List<Reservation> getReservations() { return reservations; }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
}
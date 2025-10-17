package hotel.service;

import hotel.model.*;
import java.util.*;

public class ReservationService {
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public Reservation bookRoom(Customer customer, int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && room.isAvailable()) {
                room.setAvailable(false);
                Reservation res = new Reservation(customer, room, java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(1));
                reservations.add(res);
                return res;
            }
        }
        return null;
    }
}
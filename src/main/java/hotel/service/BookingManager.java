package hotel.service;

import hotel.model.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static final List<Booking> bookings = new ArrayList<>();

    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public static List<Booking> getBookings() {
        return bookings;
    }
}
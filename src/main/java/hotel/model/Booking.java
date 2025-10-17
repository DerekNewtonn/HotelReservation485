package hotel.model;

public class Booking {
    private String guestName;
    private RoomType roomType;

    public Booking(String guestName, RoomType roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public RoomType getRoomType() {
        return roomType;
    }
}
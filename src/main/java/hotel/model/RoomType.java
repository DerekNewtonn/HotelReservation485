package hotel.model;

public class RoomType {
    private String name;
    private String bedType;
    private double pricePerNight;
    private int availableRooms;

    public RoomType(String name, String bedType, double pricePerNight, int availableRooms) {
        this.name = name;
        this.bedType = bedType;
        this.pricePerNight = pricePerNight;
        this.availableRooms = availableRooms;
    }

    public String getName() {
        return name;
    }

    public String getBedType() {
        return bedType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void bookRoom() {
        if (availableRooms > 0) {
            availableRooms--;
        }
    }

    public boolean isAvailable() {
        return availableRooms > 0;
    }
}
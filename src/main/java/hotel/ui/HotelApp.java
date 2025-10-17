package hotel.ui;

import hotel.model.*;
import hotel.service.ReservationService;
import java.util.Scanner;

public class HotelApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ReservationService service = new ReservationService();

        // sample rooms
        service.addRoom(new Room(101, "Single", 100.0));
        service.addRoom(new Room(102, "Double", 150.0));
        service.addRoom(new Room(103, "Suite", 250.0));

        System.out.println("=== Welcome to the Hotel Reservation System ===");

        while (true) {
            System.out.println("\n1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    service.getAvailableRooms().forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter your email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter room number: ");
                    int roomNum = sc.nextInt();

                    Customer customer = new Customer(name, email);
                    Reservation res = service.bookRoom(customer, roomNum);

                    if (res != null) {
                        System.out.println("Room booked successfully!");
                        System.out.println(res);
                    } else {
                        System.out.println("Room not available.");
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
            }
        }
    }
}
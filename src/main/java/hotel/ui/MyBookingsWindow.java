package hotel.ui;

import hotel.model.Booking;
import hotel.service.BookingManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyBookingsWindow {
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My Bookings");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        if (BookingManager.getBookings().isEmpty()) {
            vbox.getChildren().add(new Label("No bookings found."));
        } else {
            for (Booking booking : BookingManager.getBookings()) {
                vbox.getChildren().add(new Label(
                        booking.getGuestName() + " - " + booking.getRoomType().getName()
                ));
            }
        }

        stage.setScene(new Scene(vbox, 300, 200));
        stage.show();
    }
}
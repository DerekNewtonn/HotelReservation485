package hotel.ui;

import hotel.model.RoomType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ViewRoomsWindow {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Available Rooms");

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f9f9f9, #eaeaea);");

        // Sample room types
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(new RoomType("King Suite", "1 King Bed", 250.0, 2));
        roomTypes.add(new RoomType("Double Queen", "2 Queen Beds", 180.0, 5));
        roomTypes.add(new RoomType("Standard Single", "1 Twin Bed", 100.0, 4));

        // Load images
        Image kingImage = new Image(getClass().getResource("/images/king_suite.jpg").toExternalForm());
        Image queenImage = new Image(getClass().getResource("/images/double_queen.jpg").toExternalForm());
        Image singleImage = new Image(getClass().getResource("/images/standard_single.jpg").toExternalForm());

        for (RoomType rt : roomTypes) {
            HBox roomCard = new HBox(20);
            roomCard.setPadding(new Insets(20));
            roomCard.setAlignment(Pos.CENTER_LEFT);
            roomCard.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-background-color: linear-gradient(to bottom right, #ffffff, #f7f7f7);
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 3);
            """);
            roomCard.setPrefWidth(950);
            roomCard.setMinHeight(250);

            // Hover animation
            roomCard.setOnMouseEntered(e -> roomCard.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-background-color: linear-gradient(to bottom right, #fefefe, #f9f9f9);
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 10, 0, 0, 5);
                -fx-scale-x: 1.01;
                -fx-scale-y: 1.01;
            """));

            roomCard.setOnMouseExited(e -> roomCard.setStyle("""
                -fx-border-color: #ccc;
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-background-color: linear-gradient(to bottom right, #ffffff, #f7f7f7);
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 3);
                -fx-scale-x: 1.0;
                -fx-scale-y: 1.0;
            """));

            // Room image
            ImageView imageView = switch (rt.getName()) {
                case "King Suite" -> new ImageView(kingImage);
                case "Double Queen" -> new ImageView(queenImage);
                case "Standard Single" -> new ImageView(singleImage);
                default -> null;
            };
            if (imageView != null) {
                imageView.setFitWidth(300);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageView.setStyle("""
                    -fx-border-radius: 12;
                    -fx-background-radius: 12;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);
                """);
            }

            // Left: room info
            VBox infoBox = new VBox(10);
            infoBox.setPrefWidth(550);
            infoBox.setPadding(new Insets(5, 10, 5, 10));

            Label nameLabel = new Label(rt.getName());
            nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

            // ⭐ Gold stars
            HBox ratingBox = new HBox(3);
            ratingBox.setAlignment(Pos.CENTER_LEFT);
            String goldStar = "-fx-text-fill: #FFD700; -fx-font-size: 16px;";
            String grayStar = "-fx-text-fill: #CCCCCC; -fx-font-size: 16px;";
            for (int i = 1; i <= 5; i++) {
                Label star = new Label("★");
                star.setStyle(i <= 4 ? goldStar : grayStar);
                ratingBox.getChildren().add(star);
            }
            Label reviews = new Label("(124 reviews)");
            reviews.setStyle("-fx-text-fill: #777; -fx-font-size: 12px;");
            ratingBox.getChildren().add(reviews);

            Label bedLabel = new Label(rt.getBedType());
            bedLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

            // Feature icons
            HBox featuresRow = new HBox(25);
            featuresRow.setAlignment(Pos.CENTER_LEFT);
            featuresRow.setPadding(new Insets(5, 0, 5, 0));

            switch (rt.getName()) {
                case "King Suite" -> featuresRow.getChildren().addAll(
                        createFeature("/icons/ac.png", "A/C"),
                        createFeature("/icons/bathroom.png", "Bathroom"),
                        createFeature("/icons/wifi.png", "Wi-Fi"),
                        createFeature("/icons/valet.png", "Valet")
                );
                case "Double Queen" -> featuresRow.getChildren().addAll(
                        createFeature("/icons/ac.png", "A/C"),
                        createFeature("/icons/bathroom.png", "Bathroom"),
                        createFeature("/icons/wifi.png", "Wi-Fi")
                );
                case "Standard Single" -> featuresRow.getChildren().addAll(
                        createFeature("/icons/ac.png", "A/C"),
                        createFeature("/icons/wifi.png", "Wi-Fi"),
                        createFeature("/icons/bathroom.png", "Bathroom")
                );
            }

            Separator line = new Separator();
            line.setStyle("-fx-background-color: #eee;");
            infoBox.getChildren().addAll(nameLabel, ratingBox, bedLabel, line, featuresRow);

            // Right: pricing + buttons
            VBox rightBox = new VBox(15);
            rightBox.setAlignment(Pos.CENTER);
            rightBox.setPadding(new Insets(0, 15, 0, 15));

            Label priceLabel = new Label("$" + rt.getPricePerNight() + " / night");
            priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E8B57;");

            Label availLabel = new Label(
                    rt.getAvailableRooms() > 3 ? "Available" :
                            rt.getAvailableRooms() > 0 ? "Limited rooms!" :
                                    "Sold out"
            );
            availLabel.setStyle(rt.getAvailableRooms() == 0
                    ? "-fx-text-fill: gray; -fx-font-weight: bold;"
                    : rt.getAvailableRooms() <= 3
                    ? "-fx-text-fill: #ff8800; -fx-font-weight: bold;"
                    : "-fx-text-fill: #2E8B57; -fx-font-weight: bold;");

            Button bookBtn = new Button("Book Now");
            bookBtn.setDisable(!rt.isAvailable());
            bookBtn.setStyle("""
                -fx-background-color: #4CAF50;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-background-radius: 10;
                -fx-padding: 10 30 10 30;
            """);
            bookBtn.setOnAction(e -> {
                if (rt.isAvailable()) {
                    rt.bookRoom();
                    bookBtn.setDisable(!rt.isAvailable());
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Booking Confirmed");
                    alert.setHeaderText(null);
                    alert.setContentText("You booked a " + rt.getName() +
                            " (" + rt.getBedType() + ") for $" + rt.getPricePerNight() + " per night.");
                    alert.showAndWait();
                }
            });

            Button detailsBtn = new Button("View Details");
            detailsBtn.setStyle("""
                -fx-background-color: #2196F3;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 8 20 8 20;
            """);
            detailsBtn.setOnAction(e -> showRoomDetails(rt));

            Button reviewsBtn = new Button("View Reviews");
            reviewsBtn.setStyle("""
                -fx-background-color: #FF9800;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 8 20 8 20;
            """);
            reviewsBtn.setOnAction(e -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Coming Soon");
                alert.setHeaderText(null);
                alert.setContentText("User reviews will be available in a future update!");
                alert.showAndWait();
            });

            rightBox.getChildren().addAll(priceLabel, availLabel, bookBtn, detailsBtn, reviewsBtn);

            HBox.setHgrow(infoBox, Priority.ALWAYS);
            roomCard.getChildren().addAll(imageView, infoBox, rightBox);
            root.getChildren().add(roomCard);
        }

        Scene scene = new Scene(root, 1020, 780);
        stage.setScene(scene);
        stage.show();
    }

    // Details popup
    private void showRoomDetails(RoomType room) {
        Stage detailStage = new Stage();
        detailStage.setTitle(room.getName() + " Details");

        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        Label title = new Label(room.getName());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label desc = new Label("Spacious " + room.getBedType() +
                " featuring premium bedding, private bathroom, Wi-Fi, and city view.");
        desc.setWrapText(true);

        Label price = new Label("Price: $" + room.getPricePerNight() + " / night");
        price.setStyle("-fx-font-weight: bold;");

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> detailStage.close());

        box.getChildren().addAll(title, desc, price, closeBtn);
        Scene scene = new Scene(box, 420, 250);
        detailStage.setScene(scene);
        detailStage.show();
    }

    // Icon + label
    private VBox createFeature(String iconPath, String labelText) {
        VBox feature = new VBox(5);
        feature.setAlignment(Pos.CENTER);
        try {
            Image img = new Image(getClass().getResource(iconPath).toExternalForm());
            ImageView icon = new ImageView(img);
            icon.setFitWidth(26);
            icon.setFitHeight(26);
            icon.setPreserveRatio(true);
            Label label = new Label(labelText);
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
            feature.getChildren().addAll(icon, label);
        } catch (Exception e) {
            Label label = new Label(labelText);
            label.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
            feature.getChildren().add(label);
        }
        return feature;
    }
}
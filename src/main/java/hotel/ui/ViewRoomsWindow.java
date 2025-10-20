package hotel.ui;

import hotel.model.RoomType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ViewRoomsWindow {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Sunset Bay Resort — Rooms");

        // 🌅 Background + gradient
        Image bgImage = new Image(getClass().getResource("/images/hotel_background.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);

        Region gradient = new Region();
        gradient.setStyle("""
            -fx-background-color: linear-gradient(
                to bottom,
                rgba(0,0,0,0.8) 0%,
                rgba(0,0,0,0.55) 25%,
                rgba(0,0,0,0.75) 100%
            );
        """);

        // 🧭 Navbar
        HBox navBar = new HBox(28);
        navBar.setPadding(new Insets(14, 28, 14, 28));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: rgba(255,255,255,0.25);
            -fx-border-width: 0 0 1 0;
        """);

        Label logo = new Label("🏖️ Sunset Bay");
        logo.setFont(Font.font("Georgia", 24));
        logo.setTextFill(Color.web("#f5f5f5"));
        logo.setStyle("-fx-cursor: hand;");
        logo.setOnMouseClicked(e -> {
            stage.close();
            new HomeWindow().start(new Stage());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label linkHome = navLink("Home", () -> {
            stage.close();
            new HomeWindow().start(new Stage());
        });
        Label linkLogin = navLink("Sign In", () -> new LoginWindow().show());
        Label linkSignUp = navLink("Sign Up", () -> new SignupWindow(new hotel.model.UserManager()).show());

        HBox links = new HBox(22, linkHome, linkLogin, linkSignUp);
        links.setAlignment(Pos.CENTER_RIGHT);
        navBar.getChildren().addAll(logo, spacer, links);

        // 🌴 Room cards container
        VBox roomsContainer = new VBox(30);
        roomsContainer.setPadding(new Insets(40));
        roomsContainer.setAlignment(Pos.CENTER);

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
                -fx-border-color: rgba(255,255,255,0.15);
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-background-color: rgba(255,255,255,0.15);
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);
            """);
            roomCard.setPrefWidth(950);
            roomCard.setMinHeight(250);

            roomCard.setOnMouseEntered(e -> roomCard.setStyle("""
                -fx-border-color: #f4a261;
                -fx-background-color: rgba(255,255,255,0.22);
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.45), 14, 0, 0, 6);
                -fx-scale-x: 1.01;
                -fx-scale-y: 1.01;
            """));

            roomCard.setOnMouseExited(e -> roomCard.setStyle("""
                -fx-border-color: rgba(255,255,255,0.15);
                -fx-background-color: rgba(255,255,255,0.15);
                -fx-border-radius: 14;
                -fx-background-radius: 14;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);
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
                """);
            }

            // Room info
            VBox infoBox = new VBox(10);
            infoBox.setPrefWidth(550);
            infoBox.setPadding(new Insets(5, 10, 5, 10));

            Label nameLabel = new Label(rt.getName());
            nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #fff8e1;");

            HBox ratingBox = new HBox(3);
            ratingBox.setAlignment(Pos.CENTER_LEFT);
            String goldStar = "-fx-text-fill: #FFD700; -fx-font-size: 16px;";
            String grayStar = "-fx-text-fill: #BBBBBB; -fx-font-size: 16px;";
            for (int i = 1; i <= 5; i++) {
                Label star = new Label("★");
                star.setStyle(i <= 4 ? goldStar : grayStar);
                ratingBox.getChildren().add(star);
            }
            Label reviews = new Label("(124 reviews)");
            reviews.setStyle("-fx-text-fill: #e8e8e8; -fx-font-size: 12px;");
            ratingBox.getChildren().add(reviews);

            Label bedLabel = new Label(rt.getBedType());
            bedLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #f0f0f0;");

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
            line.setStyle("-fx-background-color: rgba(255,255,255,0.3);");

            infoBox.getChildren().addAll(nameLabel, ratingBox, bedLabel, line, featuresRow);

            // Price + Book button
            VBox rightBox = new VBox(15);
            rightBox.setAlignment(Pos.CENTER);
            rightBox.setPadding(new Insets(0, 15, 0, 15));

            Label priceLabel = new Label("$" + rt.getPricePerNight() + " / night");
            priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFD580;");

            Button bookBtn = new Button("Book Now");
            bookBtn.setStyle("""
                -fx-background-color: #f4a261;
                -fx-text-fill: #1e3d59;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-background-radius: 10;
                -fx-padding: 10 30 10 30;
            """);
            bookBtn.setOnAction(e -> {
                stage.close(); // Close the room list window
                new BookingWindow(rt).show(); // Open the Booking window for that room
            });

            rightBox.getChildren().addAll(priceLabel, bookBtn);
            HBox.setHgrow(infoBox, Priority.ALWAYS);
            roomCard.getChildren().addAll(imageView, infoBox, rightBox);
            roomsContainer.getChildren().add(roomCard);
        }

        BorderPane layout = new BorderPane();
        layout.setTop(navBar);
        layout.setCenter(roomsContainer);

        StackPane root = new StackPane(bgView, gradient, layout);
        Scene scene = new Scene(root, 1020, 780);
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        gradient.prefWidthProperty().bind(scene.widthProperty());
        gradient.prefHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);
        stage.show();
    }

    private Label navLink(String text, Runnable onClick) {
        Label link = new Label(text);
        link.setTextFill(Color.web("#f2f2f2"));
        link.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;");
        link.setOnMouseEntered(e -> link.setStyle("-fx-text-fill: #FFD580; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-text-fill: #f2f2f2; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;"));
        if (onClick != null) link.setOnMouseClicked(e -> onClick.run());
        return link;
    }

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
            label.setStyle("-fx-font-size: 12px; -fx-text-fill: #fff8e1;");
            feature.getChildren().addAll(icon, label);
        } catch (Exception e) {
            Label label = new Label(labelText);
            label.setStyle("-fx-font-size: 12px; -fx-text-fill: #fff8e1;");
            feature.getChildren().add(label);
        }
        return feature;
    }
}
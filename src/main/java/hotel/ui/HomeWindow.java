package hotel.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeWindow extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sunset Bay Resort");

        // --- Background & gradient
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

        // --- Navbar (now properly blended)
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

        Label linkRooms = navLink("Rooms", () -> new ViewRoomsWindow().show());
        Label linkBookings = navLink("My Bookings", () -> new MyBookingsWindow().show());
        Label linkSignIn = navLink("Sign In", () -> new LoginWindow().show());

        HBox links = new HBox(22, linkRooms, linkBookings, linkSignIn);
        links.setAlignment(Pos.CENTER_RIGHT);
        navBar.getChildren().addAll(logo, spacer, links);

        // --- Hero content
        VBox heroBox = new VBox(18);
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(40));
        heroBox.setStyle("""
            -fx-background-color: rgba(0,0,0,0.35);
            -fx-background-radius: 16;
        """);

        Label title = new Label("Sunset Bay Resort");
        title.setFont(Font.font("Georgia", 42));
        title.setStyle("-fx-text-fill: #fff8e1; -fx-font-weight: bold;");

        Label subtitle = new Label("Experience coastal serenity and five-star comfort.");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setStyle("-fx-text-fill: #f2d785; -fx-font-style: italic;");

        // ⭐ Review highlight
        HBox reviewBox = new HBox(6);
        reviewBox.setAlignment(Pos.CENTER);

        String gold = "-fx-text-fill: #FFD700; -fx-font-size: 18px;";
        String gray = "-fx-text-fill: #CCCCCC; -fx-font-size: 18px;";
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("★");
            star.setStyle(i <= 5 ? gold : gray);
            reviewBox.getChildren().add(star);
        }

        Label ratingText = new Label("4.9 / 5  •  Based on 1,200 guest reviews");
        ratingText.setStyle("-fx-text-fill: #f5f5f5; -fx-font-size: 14px;");
        reviewBox.getChildren().add(ratingText);

        Button reviewsBtn = new Button("Read More Reviews");
        reviewsBtn.setStyle("""
            -fx-background-color: #FF9800;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-background-radius: 10;
            -fx-padding: 8 20 8 20;
        """);
        reviewsBtn.setOnAction(e -> new ViewRoomsWindow().show());

        heroBox.getChildren().addAll(title, subtitle, reviewBox, reviewsBtn);

        // --- Footer
        HBox footer = new HBox(24);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(12));
        footer.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.55), CornerRadii.EMPTY, Insets.EMPTY)));

        Label address = new Label("📍 Miami, FL");
        Label phone = new Label("☎ (305) 555-0147");
        Label promo = new Label("✨ Book your dream stay today!");
        for (Label l : new Label[]{address, phone, promo}) {
            l.setTextFill(Color.WHITE);
            l.setStyle("-fx-font-size: 13px;");
        }
        footer.getChildren().addAll(address, phone, promo);

        // --- Layout hierarchy
        BorderPane content = new BorderPane();
        content.setTop(navBar);
        content.setCenter(heroBox);
        content.setBottom(footer);

        // --- Stack everything so gradient affects navbar too
        StackPane root = new StackPane(bgView, gradient, content);
        StackPane.setAlignment(content, Pos.CENTER);

        // --- Scene setup
        Scene scene = new Scene(root, 1020, 780);
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        gradient.prefWidthProperty().bind(scene.widthProperty());
        gradient.prefHeightProperty().bind(scene.heightProperty());

        // --- Animations
        fadeIn(heroBox, 1.2);
        fadeIn(navBar, 1.0);
        fadeIn(footer, 1.0);

        stage.setScene(scene);
        stage.show();
    }

    // --- Helpers
    private Label navLink(String text, Runnable onClick) {
        Label link = new Label(text);
        link.setTextFill(Color.web("#f2f2f2"));
        link.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;");
        link.setOnMouseEntered(e -> link.setStyle("-fx-text-fill: #FFD580; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-text-fill: #f2f2f2; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;"));
        if (onClick != null) link.setOnMouseClicked(e -> onClick.run());
        return link;
    }

    private void fadeIn(Region node, double seconds) {
        FadeTransition ft = new FadeTransition(Duration.seconds(seconds), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
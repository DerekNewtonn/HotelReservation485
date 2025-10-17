package hotel.ui;

import hotel.model.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuWindow {

    private final UserManager userManager;

    public MainMenuWindow(UserManager userManager) {
        this.userManager = userManager;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Main Menu");

        String username = userManager.getLoggedInUser();
        Label welcomeLabel = new Label("Welcome, " + (username != null ? username : "Guest") + "!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button viewRooms = new Button("View Available Rooms");
        Button accountBtn = new Button("My Account");
        Button logoutBtn = new Button("Log Out");

        viewRooms.setOnAction(e -> new ViewRoomsWindow().show());
        accountBtn.setOnAction(e -> welcomeLabel.setText("Feature coming soon!"));
        logoutBtn.setOnAction(e -> {
            userManager.logout();
            stage.close();
            new LoginWindow().show();
        });

        VBox layout = new VBox(15, welcomeLabel, viewRooms, accountBtn, logoutBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #e8e8e8);");

        Scene scene = new Scene(layout, 400, 250);
        stage.setScene(scene);
        stage.show();
    }
}
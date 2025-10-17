package hotel.ui;

import hotel.model.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupWindow {

    private final UserManager userManager; // ✅ shared user manager instance

    // ✅ Constructor that receives the same UserManager used by LoginWindow
    public SignupWindow(UserManager userManager) {
        this.userManager = userManager;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Hotel Reservation - Sign Up");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #f3f3f3);");

        Label title = new Label("Create a New Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        Button registerBtn = new Button("Register");
        registerBtn.setStyle("""
            -fx-background-color: #4CAF50;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 8 30 8 30;
        """);

        Button backBtn = new Button("Back to Login");
        backBtn.setStyle("""
            -fx-background-color: #888;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 8 20 8 20;
        """);

        // ✅ Registration logic
        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            if (userManager.register(username, password)) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Account created successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Username already exists.");
            }
        });

        // ✅ Back to login
        backBtn.setOnAction(e -> {
            stage.close();
            new LoginWindow(userManager).show(); // pass same manager here too
        });

        root.getChildren().addAll(title, usernameField, passwordField, confirmPasswordField, messageLabel, registerBtn, backBtn);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }
}
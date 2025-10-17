package hotel.ui;

import hotel.model.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginWindow {

    private final UserManager userManager;

    public LoginWindow(UserManager userManager) {
        this.userManager = userManager;
    }

    public LoginWindow() {
        this(new UserManager());
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Sunset Bay Resort — Login");

        // 🌅 Background image
        BackgroundImage bg = new BackgroundImage(
                new Image(getClass().getResource("/images/BackgroundLogin.jpg").toExternalForm(),
                        1000, 700, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        StackPane background = new StackPane();
        background.setBackground(new Background(bg));

        // 🌑 Dark overlay for readability
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // 💎 Transparent outline card
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(400);
        card.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.08);
            -fx-border-color: rgba(255,255,255,0.5);
            -fx-border-width: 1.2;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);
        card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.35)));

        // 🏝 Title
        Label title = new Label("Welcome to Sunset Bay");
        title.setFont(Font.font("Arial Rounded MT Bold", 26));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Please sign in to continue");
        subtitle.setTextFill(Color.web("#dddddd"));
        subtitle.setStyle("-fx-font-size: 13px; -fx-font-style: italic;");

        // Login fields
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Email or Username");
        styleTransparentField(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleTransparentField(passwordField);

        Label feedback = new Label();
        feedback.setTextFill(Color.web("#ffd166"));
        feedback.setStyle("-fx-font-size: 12px;");

        // ✨ Sign-in button
        Button loginBtn = new Button("Sign In");
        styleGoldButton(loginBtn);

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();
            if (userManager.authenticate(user, pass)) {
                feedback.setText("✅ Welcome back, " + user + "!");
                stage.close();
                new MainMenuWindow(userManager).show();
            } else {
                feedback.setText("❌ Invalid username or password.");
            }
        });

        // ✨ Create account link
        Hyperlink createAccount = new Hyperlink("Don’t have an account? Create one");
        createAccount.setTextFill(Color.web("#ffe8a3"));
        createAccount.setOnAction(e -> {
            stage.close();
            new SignupWindow(userManager).show();
        });

        loginBox.getChildren().addAll(usernameField, passwordField, loginBtn, feedback, createAccount);

        card.getChildren().addAll(title, subtitle, loginBox);

        StackPane root = new StackPane(background, overlay, card);
        StackPane.setAlignment(card, Pos.CENTER);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    // 🌫 Transparent field with white border
    private void styleTransparentField(TextField field) {
        field.setPrefWidth(250);
        field.setStyle("""
            -fx-background-color: rgba(255,255,255,0.1);
            -fx-text-fill: white;
            -fx-border-color: rgba(255,255,255,0.4);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 8;
            -fx-prompt-text-fill: #cccccc;
        """);
    }

    // 🟨 Gold-accented button
    private void styleGoldButton(Button button) {
        button.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: #f4a261;
            -fx-border-width: 1.3;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-padding: 8 25 8 25;
        """);

        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-background-color: #f4a261;
            -fx-text-fill: #1e3d59;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-padding: 8 25 8 25;
        """));

        button.setOnMouseExited(e -> button.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: #f4a261;
            -fx-border-width: 1.3;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-padding: 8 25 8 25;
        """));
    }
}
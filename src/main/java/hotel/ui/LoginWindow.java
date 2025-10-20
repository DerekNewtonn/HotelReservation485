package hotel.ui;

import hotel.model.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        // 🌅 Background setup (same as HomeWindow)
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

        StackPane backgroundStack = new StackPane(bgView, gradient);

        // 🧭 Navbar (matches HomeWindow)
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
        Label linkRooms = navLink("Rooms", () -> new ViewRoomsWindow().show());
        Label linkSignUp = navLink("Sign Up", () -> {
            stage.close();
            new SignupWindow(userManager).show();
        });

        HBox links = new HBox(22, linkHome, linkRooms, linkSignUp);
        links.setAlignment(Pos.CENTER_RIGHT);
        navBar.getChildren().addAll(logo, spacer, links);

        // 💎 Login card (refined glass style)
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setPrefWidth(380);
        card.setStyle("""
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-border-color: rgba(255,255,255,0.15);
            -fx-border-width: 1.0;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);
        card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.4)));

        Label title = new Label("Welcome Back");
        title.setFont(Font.font("Georgia", 28));
        title.setTextFill(Color.web("#fff8e1"));

        Label subtitle = new Label("Sign in to continue your stay with us");
        subtitle.setTextFill(Color.web("#d6d6d6"));
        subtitle.setStyle("-fx-font-size: 13px; -fx-font-style: italic;");

        // ✨ Fields (smaller, focused)
        TextField usernameField = new TextField();
        usernameField.setPromptText("Email or Username");
        styleLoginField(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleLoginField(passwordField);

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

        VBox loginBox = new VBox(15, usernameField, passwordField, loginBtn, feedback, createAccount);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setFillWidth(false); // prevent children from stretching horizontally

        card.getChildren().addAll(title, subtitle, loginBox);

        // Main layout with navbar + login card
        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(card);

        StackPane stack = new StackPane(backgroundStack, root);
        StackPane.setAlignment(root, Pos.CENTER);

        Scene scene = new Scene(stack, 1000, 700);
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        gradient.prefWidthProperty().bind(scene.widthProperty());
        gradient.prefHeightProperty().bind(scene.heightProperty());

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

    private void styleLoginField(TextField field) {
        field.setPrefWidth(220);
        field.setMaxWidth(220);
        field.setAlignment(Pos.CENTER_LEFT);
        field.setStyle("""
        -fx-background-color: rgba(255,255,255,0.05);
        -fx-text-fill: white;
        -fx-border-color: rgba(255,255,255,0.25);
        -fx-border-radius: 8;
        -fx-background-radius: 8;
        -fx-padding: 8 10 8 10;
        -fx-prompt-text-fill: #bfbfbf;
        -fx-font-size: 13px;
    """);

        field.focusedProperty().addListener((obs, oldVal, focused) -> {
            if (focused) {
                field.setStyle("""
                -fx-background-color: rgba(255,255,255,0.08);
                -fx-border-color: #f4a261;
                -fx-border-width: 1.3;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-text-fill: white;
                -fx-prompt-text-fill: #e0e0e0;
                -fx-font-size: 13px;
                -fx-padding: 8 10 8 10;
            """);
            } else {
                field.setStyle("""
                -fx-background-color: rgba(255,255,255,0.05);
                -fx-text-fill: white;
                -fx-border-color: rgba(255,255,255,0.25);
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 8 10 8 10;
                -fx-prompt-text-fill: #bfbfbf;
                -fx-font-size: 13px;
            """);
            }
        });
    }

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

package hotel.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // 🚪 Start with the login window
        new LoginWindow().show();
    }

    public static void main(String[] args) {
        launch();
    }
}
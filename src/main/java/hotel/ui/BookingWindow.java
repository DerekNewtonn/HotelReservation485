package hotel.ui;

import hotel.model.RoomType;
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
import javafx.util.Callback;
import javafx.scene.control.ListCell;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class BookingWindow {

    private final RoomType room;

    public BookingWindow(RoomType room) {
        this.room = room;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Book Your Stay — " + room.getName());

        // 🌅 Background + gradient
        Image bg = new Image(getClass().getResource("/images/hotel_background.jpg").toExternalForm());
        ImageView bgView = new ImageView(bg);
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
        Label linkRooms = navLink("Rooms", () -> {
            stage.close();
            new ViewRoomsWindow().show();
        });
        HBox links = new HBox(22, linkRooms);
        links.setAlignment(Pos.CENTER_RIGHT);
        navBar.getChildren().addAll(logo, spacer, links);

        // 🌟 Card container
        StackPane cardStack = new StackPane();
        cardStack.setAlignment(Pos.CENTER);

        VBox bookingCard = createBookingStep(stage, cardStack);
        VBox paymentCard = createPaymentStep(stage, cardStack);

        cardStack.getChildren().addAll(bookingCard, paymentCard);
        paymentCard.setVisible(false);

        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(cardStack);

        StackPane stack = new StackPane(bgView, gradient, root);
        Scene scene = new Scene(stack, 950, 680);
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        gradient.prefWidthProperty().bind(scene.widthProperty());
        gradient.prefHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);
        stage.show();
    }

    // STEP 1 – Booking details
    private VBox createBookingStep(Stage stage, StackPane stack) {
        VBox card = makeGlassCard();

        Label title = header("Booking Details");
        Label subtitle = subHeader("Enter your stay information");

        DatePicker checkIn = new DatePicker();
        DatePicker checkOut = new DatePicker();
        checkIn.setPromptText("Check-in Date");
        checkOut.setPromptText("Check-out Date");
        styleDatePicker(checkIn);
        styleDatePicker(checkOut);

        Label total = new Label("Total: $0.00");
        total.setTextFill(Color.web("#ffe8a3"));
        total.setStyle("-fx-font-size:14px; -fx-font-weight:bold;");

        checkIn.valueProperty().addListener((a,b,c)->updatePrice(checkIn,checkOut,total));
        checkOut.valueProperty().addListener((a,b,c)->updatePrice(checkIn,checkOut,total));

        Button next = goldButton("Continue to Payment");
        next.setOnAction(e -> {
            if (checkIn.getValue()==null || checkOut.getValue()==null) {
                showAlert(Alert.AlertType.WARNING,"Missing Info","Please fill out both check-in and check-out dates.");
                return;
            }
            long nights = ChronoUnit.DAYS.between(checkIn.getValue(), checkOut.getValue());
            if (nights <= 0) {
                showAlert(Alert.AlertType.WARNING,"Invalid Dates","Check-out must be after check-in.");
                return;
            }

            // Save data
            stack.getProperties().put("nights", nights);
            stack.getProperties().put("total", nights * room.getPricePerNight());
            stack.getProperties().put("checkin", checkIn.getValue());
            stack.getProperties().put("checkout", checkOut.getValue());

            // Switch to payment screen
            card.setVisible(false);
            stack.getChildren().get(1).setVisible(true);
        });

        Label roomLine = new Label(room.getName() + " — $" + room.getPricePerNight() + " / night");
        roomLine.setStyle("-fx-font-size: 15px; -fx-text-fill: #f4a261; -fx-font-weight: bold;");

        VBox content = new VBox(15, roomLine, checkIn, checkOut, total, next);
        content.setAlignment(Pos.CENTER);
        card.getChildren().addAll(title, subtitle, content);
        return card;
    }

    // STEP 2 – Payment information
    private VBox createPaymentStep(Stage stage, StackPane stack) {
        VBox card = makeGlassCard();

        Label title = header("Payment Information");
        Label subtitle = subHeader("Securely complete your payment");

        VBox fieldBox = new VBox(12);
        fieldBox.setAlignment(Pos.CENTER);

        // 💰 Total label directly above Cardholder Name
        Label totalLabel = new Label("Total: $0.00");
        totalLabel.setTextFill(Color.web("#ffe8a3"));
        totalLabel.setStyle("-fx-font-size:14px; -fx-font-weight:bold;");

        // When shown, update immediately
        card.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && stack.getProperties().get("total") != null) {
                double total = (double) stack.getProperties().get("total");
                totalLabel.setText(String.format("Total: $%.2f", total));
            }
        });

        TextField name = createField("Cardholder Name");
        TextField cardNum = createField("Card Number");

        // Expiration date + CVV row
        HBox expRow = new HBox(10);
        expRow.setAlignment(Pos.CENTER);

        ComboBox<String> month = new ComboBox<>();
        ComboBox<String> year = new ComboBox<>();
        month.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12");
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 10; i++)
            year.getItems().add(String.valueOf(currentYear + i).substring(2));
        month.setPromptText("MM");
        year.setPromptText("YY");
        styleComboBox(month, 70);
        styleComboBox(year, 70);
        forceWhiteText(month);
        forceWhiteText(year);

        // Smaller CVV field
        PasswordField cvv = new PasswordField();
        cvv.setPromptText("CVV");
        cvv.setPrefWidth(60);
        cvv.setStyle("""
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-text-fill: white;
            -fx-border-color: rgba(255,255,255,0.3);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-prompt-text-fill: #cccccc;
            -fx-font-size: 13px;
            -fx-padding: 8 10 8 10;
        """);

        expRow.getChildren().addAll(month, year, cvv);

        TextField email = createField("Billing Email");

        Button pay = goldButton("Pay Now");
        Button back = grayButton("← Back");

        pay.setOnAction(e -> {
            if (name.getText().isBlank() || cardNum.getText().isBlank() ||
                    month.getValue() == null || year.getValue() == null ||
                    cvv.getText().isBlank() || email.getText().isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Missing Info", "Please fill out all payment fields.");
                return;
            }
            showReceipt(stack, email.getText());
            stage.close();
        });

        back.setOnAction(e -> {
            card.setVisible(false);
            stack.getChildren().get(0).setVisible(true);
        });

        fieldBox.getChildren().addAll(totalLabel, name, cardNum, expRow, email, pay, back);
        card.getChildren().addAll(title, subtitle, fieldBox);
        return card;
    }

    // --- Styling helpers ---
    private void forceWhiteText(ComboBox<String> comboBox) {
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        setTextFill(empty ? Color.GRAY : Color.WHITE);
                        setStyle("-fx-background-color: rgba(20,20,20,0.9);");
                    }
                };
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: transparent;");
            }
        });
    }

    private TextField createField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        styleField(f);
        return f;
    }

    private void styleField(TextField field) {
        field.setPrefWidth(200);
        field.setMaxWidth(200);
        field.setStyle("""
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-text-fill: white;
            -fx-border-color: rgba(255,255,255,0.3);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-prompt-text-fill: #cccccc;
            -fx-padding: 8 10 8 10;
            -fx-font-size: 13px;
        """);
    }

    private void styleComboBox(ComboBox<?> box, double width) {
        box.setPrefWidth(width);
        box.setStyle("""
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-border-color: rgba(255,255,255,0.3);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-prompt-text-fill: #cccccc;
            -fx-text-fill: white;
            -fx-font-size: 13px;
        """);
        if (box.getEditor() != null) {
            box.getEditor().setStyle("""
                -fx-text-fill: white;
                -fx-prompt-text-fill: #cccccc;
            """);
        }
    }

    private void styleDatePicker(DatePicker picker) {
        picker.setPrefWidth(200);
        picker.setStyle("""
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-border-color: rgba(255,255,255,0.3);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-font-size: 13px;
            -fx-control-inner-background: rgba(20,20,20,0.9);
        """);
        picker.getEditor().setStyle("""
            -fx-text-fill: white;
            -fx-prompt-text-fill: #cccccc;
        """);
    }

    private VBox makeGlassCard(){
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setPrefWidth(420);
        card.setStyle("""
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-border-color: rgba(255,255,255,0.2);
            -fx-border-width: 1.0;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);
        card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.4)));
        return card;
    }

    private Label header(String text){
        Label l=new Label(text);
        l.setFont(Font.font("Georgia",26));
        l.setTextFill(Color.web("#fff8e1"));
        return l;
    }

    private Label subHeader(String text){
        Label l=new Label(text);
        l.setTextFill(Color.web("#d6d6d6"));
        l.setStyle("-fx-font-size:13px; -fx-font-style:italic;");
        return l;
    }

    private Button goldButton(String text){
        Button b=new Button(text);
        b.setStyle("""
            -fx-background-color:#f4a261;
            -fx-text-fill:#1e3d59;
            -fx-font-weight:bold;
            -fx-font-size:14px;
            -fx-background-radius:10;
            -fx-padding:8 25 8 25;
        """);
        return b;
    }

    private Button grayButton(String text){
        Button b=new Button(text);
        b.setStyle("""
            -fx-background-color:transparent;
            -fx-border-color:#ccc;
            -fx-border-radius:10;
            -fx-text-fill:#ddd;
            -fx-font-size:13px;
            -fx-padding:8 20 8 20;
        """);
        return b;
    }

    private void updatePrice(DatePicker in, DatePicker out, Label label){
        if(in.getValue()!=null && out.getValue()!=null){
            long n=ChronoUnit.DAYS.between(in.getValue(),out.getValue());
            label.setText(n>0?String.format("Total: $%.2f",n*room.getPricePerNight()):"Total: $0.00");
        }
    }

    private Label navLink(String text,Runnable onClick){
        Label link=new Label(text);
        link.setTextFill(Color.web("#f2f2f2"));
        link.setStyle("-fx-font-size:15px; -fx-font-weight:bold; -fx-cursor:hand;");
        link.setOnMouseEntered(e->link.setStyle("-fx-text-fill:#FFD580; -fx-font-size:15px; -fx-font-weight:bold; -fx-cursor:hand;"));
        link.setOnMouseExited(e->link.setStyle("-fx-text-fill:#f2f2f2; -fx-font-size:15px; -fx-font-weight:bold; -fx-cursor:hand;"));
        if(onClick!=null) link.setOnMouseClicked(e->onClick.run());
        return link;
    }

    private void showAlert(Alert.AlertType type,String title,String msg){
        Alert a=new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showReceipt(StackPane data, String email){
        long nights = (long)data.getProperties().get("nights");
        double total = (double)data.getProperties().get("total");
        String id = "SB-" + (100000 + new Random().nextInt(900000));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Receipt");
        alert.setHeaderText("Payment Successful ✅");
        alert.setContentText(String.format("""
                Confirmation #: %s
                Room: %s
                Stay: %s to %s  (%d nights)
                Total Paid: $%.2f
                Email Receipt Sent To: %s
                """, id, room.getName(),
                data.getProperties().get("checkin"),
                data.getProperties().get("checkout"),
                nights, total, email
        ));
        alert.showAndWait();
    }
}
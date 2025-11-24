module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens br.com.washii.main to javafx.fxml;
    exports br.com.washii.main;
}
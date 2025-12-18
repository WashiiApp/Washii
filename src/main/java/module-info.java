module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens br.com.washii.features.hello to javafx.fxml;
    exports br.com.washii.features.hello to javafx.graphics;
    exports br.com.washii.app;
}
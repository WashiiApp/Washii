module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens br.com.washii.feature.teste to javafx.fxml;
    exports br.com.washii.app;
}
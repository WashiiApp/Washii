module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens br.com.washii.presentation.login to javafx.fxml;
    opens br.com.washii.presentation.home to javafx.fxml;
    exports br.com.washii.app;
}
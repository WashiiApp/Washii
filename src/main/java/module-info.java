module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.com.washii to javafx.fxml;
    exports br.com.washii;
}
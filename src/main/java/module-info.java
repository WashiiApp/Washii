module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires javafx.base;
    requires jbcrypt;

    opens br.com.washii.domain.entities to javafx.base, javafx.fxml;
    opens br.com.washii.presentation.layout to javafx.fxml;
    opens br.com.washii.presentation.acesso to javafx.fxml;
    opens br.com.washii.presentation.servico to javafx.fxml;
    opens br.com.washii.presentation.agendamentos to javafx.fxml;
    exports br.com.washii.app;
}
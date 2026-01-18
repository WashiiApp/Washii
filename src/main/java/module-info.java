module br.com.washii {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires javafx.base;
    requires jbcrypt;
    requires java.datatransfer;
    requires org.kordamp.ikonli.javafx;

    opens br.com.washii.domain.entities to javafx.base, javafx.fxml;
    opens br.com.washii.presentation.layout to javafx.fxml;
    opens br.com.washii.presentation.screens.acesso to javafx.fxml;
    opens br.com.washii.presentation.screens.servico to javafx.fxml;
    opens br.com.washii.presentation.screens.agendamentos to javafx.fxml;
    opens br.com.washii.presentation.screens.home to javafx.fxml;
    opens br.com.washii.presentation.screens.perfil to javafx.fxml;
    opens br.com.washii.presentation.screens.relatorio to javafx.fxml;
    opens br.com.washii.presentation.components.cards to javafx.fxml;
    
    exports br.com.washii.app;
}
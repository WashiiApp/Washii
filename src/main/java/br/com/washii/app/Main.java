package br.com.washii.app;

import br.com.washii.presentation.core.SceneManager;
import br.com.washii.presentation.core.StyleManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // TESTES
        StyleManager styleManager = new StyleManager(
            "/br/com/washii/style/global.css",
            "/br/com/washii/style/componentes.css",
            "/br/com/washii/style/tema-claro.css"
        );

        SceneManager sceneManager = new SceneManager(primaryStage, styleManager);
        primaryStage.setTitle("Tela de Login");
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(400);
        primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-blue-32.png")),
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-blue-128.png"))
        );

        // Testa carregar a primeira tela
        sceneManager.switchFullScene("/br/com/washii/view/login/login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
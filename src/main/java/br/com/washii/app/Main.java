package br.com.washii.app;

import br.com.washii.core.SceneManager;
import br.com.washii.domain.enums.CategoriaVeiculo;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // TESTES
        SceneManager sceneManager = new SceneManager(primaryStage);
        primaryStage.setTitle("Tela de Login");
        primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-black-32.png")),
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-black-128.png"))
        );


        // Testa carregar a primeira tela
        sceneManager.switchFullScene("/br/com/washii/feature/teste/main.fxml");
        // Testa carregar css
        sceneManager.addGlobalStyle("/br/com/washii/style/base.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/components.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/tema-claro.css");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
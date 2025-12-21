package br.com.washii.app;

import br.com.washii.core.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // TESTES
        SceneManager sceneManager = new SceneManager(primaryStage);

        // 1. Testa carregar a primeira tela
        sceneManager.switchFullScene("/br/com/washii/feature/teste/main.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
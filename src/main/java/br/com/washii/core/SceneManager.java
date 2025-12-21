package br.com.washii.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private final Stage primaryStage;
    private Pane contentArea; // O painel central do BorderPane (opcional)

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Define qual é a área que receberá as telas internas (chamado após o login)
    public void setContentArea(Pane contentArea) {
        this.contentArea = contentArea;
    }

    // 1. Troca a TELA COMPLETA (ex: Login para MainView)
    public void switchFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Injetar o Screen Manager no Controller
            BaseController controller = loader.getController();
            if (controller != null) {
                controller.setSceneManager(this);
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. Troca apenas o CONTEÚDO CENTRAL (Navegação fluida)
    public void loadInternalScreen(String fxmlPath) {
        if (contentArea == null) {
            throw new IllegalStateException(
            "Erro no SceneManager: A 'contentArea' não foi definida. " +
            "Certifique-se de chamar setContentArea() no Controller principal antes de navegar."
        );
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent node = loader.load();

            // Injetar o Screen Manager no Controller
            BaseController controller = loader.getController();
            if (controller != null) {
                controller.setSceneManager(this);
            }

            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 3. Abre um POPUP (Janela modal)
    public void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            Parent root = loader.load();

            // Injetar o ScreenManager no controller
            BaseController controller = loader.getController();
            if (controller != null) {
                controller.setSceneManager(this);
            }

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela de trás
            popupStage.initOwner(primaryStage);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
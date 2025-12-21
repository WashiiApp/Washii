package br.com.washii.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Responsável pelo gerenciamento de navegação e fluxo de telas da aplicação.
 * Centraliza a lógica de carregamento de FXML e injeção de dependências nos Controllers.
 */
public class SceneManager {
    private final Stage primaryStage;
    private Pane contentArea; 

    /**
     * @param primaryStage O estágio principal criado pelo JavaFX na classe Main.
     */
    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Define o contêiner que receberá
     * as telas internas do sistema após o login.
     * @param contentArea Painel central localizado no MainLayout.
     */
    public void setContentArea(Pane contentArea) {
        this.contentArea = contentArea;
    }

    /**
     * Substitui toda a cena atual da janela principal. 
     * @param fxmlPath Caminho do arquivo FXML.
     */
    public void switchFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Injeta este manager no controller para permitir navegação futura
            injectManager(loader);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar cena completa: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Carrega um FXML dentro da área de conteúdo definida (contentArea).
     * @param fxmlPath Caminho do arquivo FXML da funcionalidade.
     * @throws IllegalStateException Se a contentArea não tiver sido inicializada.
     */
    public void loadInternalScreen(String fxmlPath) {
        if (contentArea == null) {
            throw new IllegalStateException("Erro: A 'contentArea' (painel central) não foi definida.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent node = loader.load();

            injectManager(loader);

            // Substitui apenas os filhos do painel central pelo novo conteúdo
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            System.err.println("Erro ao carregar tela interna: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Abre uma nova janela modal (bloqueia a interação com a tela de trás).
     * @param fxmlPath Caminho do arquivo FXML do popup.
     * @param title Título da janela popup.
     */
    public void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            injectManager(loader);

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initModality(Modality.APPLICATION_MODAL); 
            popupStage.initOwner(primaryStage); // Vincula o popup à janela principal
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erro ao abrir popup: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar privado para evitar repetição de código.
     * Verifica se o Controller da tela carregada estende BaseController e injeta o Manager.
     */
    private void injectManager(FXMLLoader loader) {
        Object controller = loader.getController();
        if (controller instanceof BaseController) {
            ((BaseController) controller).setSceneManager(this);
        }
    }
}
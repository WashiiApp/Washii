package br.com.washii.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Responsável pelo gerenciamento de navegação e fluxo de telas da aplicação.
 * Gerencia dinamicamente os estilos CSS aplicados ao sistema.
 */
public class SceneManager {
    private final Stage primaryStage;
    private Pane contentArea;

    // Armazena as referências externas dos CSS ativos
    private final Set<String> activeStylesheets = new LinkedHashSet<>();

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // --- MÉTODOS DE GERENCIAMENTO DE ESTILO ---

    /**
     * Adiciona um estilo globalmente e o aplica na cena atual.
     * @param cssPath Caminho do recurso (ex: "/styles/base.css")
     */
    public void addGlobalStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        if (activeStylesheets.add(url)) {
            applyToAllActiveScenes();
        }
    }

    /**
     * Remove um estilo e atualiza a interface.
     */
    public void removeGlobalStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        if (activeStylesheets.remove(url)) {
            applyToAllActiveScenes();
        }
    }

    /**
     * Força a atualização dos estilos na janela principal.
     */
    private void applyToAllActiveScenes() {
        if (primaryStage.getScene() != null) {
            primaryStage.getScene().getStylesheets().setAll(activeStylesheets);
        }
    }

    // --- MÉTODOS DE NAVEGAÇÃO ADAPTADOS ---

    public void switchFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            injectManager(loader);

            Scene scene = new Scene(root);
            // Aplica os estilos ativos na nova cena
            scene.getStylesheets().addAll(activeStylesheets);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar cena completa: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Como telas internas herdam o estilo da Scene pai (primaryStage),
     * você não precisa reaplicar o CSS aqui, a menos que o nó raiz
     * tenha estilos específicos via FXML que você queira sobrescrever.
     */
    public void loadInternalScreen(String fxmlPath) {
        if (contentArea == null) {
            throw new IllegalStateException("Erro: A 'contentArea' não foi definida.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent node = loader.load();
            injectManager(loader);

            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            System.err.println("Erro ao carregar tela interna: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            injectManager(loader);

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(primaryStage);

            Scene popupScene = new Scene(root);
            // APLICAÇÃO DINÂMICA: Garante que o popup siga o tema atual (Dark/Light)
            popupScene.getStylesheets().addAll(activeStylesheets);

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erro ao abrir popup: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void setContentArea(Pane contentArea) {
        this.contentArea = contentArea;
    }

    private void injectManager(FXMLLoader loader) {
        Object controller = loader.getController();
        if (controller instanceof BaseController) {
            ((BaseController) controller).setSceneManager(this);
        }
    }
}
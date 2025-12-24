package br.com.washii.presentation.core;

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
 * Responsável pelo gerenciamento de navegação, fluxo de telas e temas da aplicação Washii.
 * Centraliza a lógica de carregamento de arquivos FXML e a aplicação dinâmica de estilos CSS.
 * * @author Grupo Washii
 */
public class SceneManager {

    /** Janela principal da aplicação (Stage). */
    private final Stage primaryStage;

    /** Área de conteúdo para carregamento de telas internas (ex: dashboard). */
    private Pane contentArea;

    /** * Armazena as URLs dos estilos CSS ativos de forma ordenada e única.
     * Utilizado para garantir que novas janelas e cenas herdem o tema atual.
     */
    private final Set<String> activeStylesheets = new LinkedHashSet<>();

    /**
     * Construtor do gerenciador de cenas.
     * @param primaryStage O Stage principal fornecido pela classe Application do JavaFX.
     */
    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // --- MÉTODOS DE GERENCIAMENTO DE ESTILO ---

    /**
     * Adiciona um arquivo CSS à lista de estilos ativos e o aplica imediatamente na janela principal.
     * @param cssPath O caminho relativo do recurso CSS.
     */
    public void addGlobalStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        if (activeStylesheets.add(url)) {
            applyToAllActiveScenes();
        }
    }

    /**
     * Remove um estilo CSS da lista de ativos e atualiza a interface.
     * @param cssPath O caminho relativo do recurso CSS a ser removido.
     */
    public void removeGlobalStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        if (activeStylesheets.remove(url)) {
            applyToAllActiveScenes();
        }
    }

    /**
     * Sincroniza os estilos da cena atual do Stage principal com a lista de estilos ativos.
     */
    private void applyToAllActiveScenes() {
        if (primaryStage.getScene() != null) {
            primaryStage.getScene().getStylesheets().setAll(activeStylesheets);
        }
    }

    // --- MÉTODOS DE NAVEGAÇÃO ---

    /**
     * Substitui a cena inteira do Stage principal por uma nova tela.
     * @param fxmlPath Caminho do arquivo FXML da nova tela.
     */
    public void switchFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            injectManager(loader);

            Scene scene = new Scene(root);
            // Aplica os estilos ativos na nova cena para manter consistência visual
            scene.getStylesheets().addAll(activeStylesheets);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar cena completa: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Carrega uma tela FXML dentro de um container específico (Pane).
     * @param fxmlPath Caminho do arquivo FXML da tela interna.
     * @throws IllegalStateException Caso a contentArea não tenha sido definida previamente via setContentArea.
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

    /**
     * Abre uma nova janela modal (Popup) que bloqueia a interação com a janela principal.
     * @param fxmlPath Caminho do arquivo FXML do popup.
     * @param title Título que será exibido na barra da janela.
     */
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
            // Garante que o popup herde o tema atual (Dark/Light)
            popupScene.getStylesheets().addAll(activeStylesheets);

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erro ao abrir popup: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Define o container onde as telas internas serão renderizadas.
     * @param contentArea O Pane (geralmente um StackPane ou AnchorPane) de destino.
     */
    public void setContentArea(Pane contentArea) {
        this.contentArea = contentArea;
    }

    /**
     * @return Primary Stage
     */
    public Stage getPrimaryStage(){
        return this.primaryStage;
    }

    /**
     * Injeta a instância deste SceneManager no Controller da tela carregada.
     * O Controller deve estender a classe BaseController para receber a referência.
     * @param loader O FXMLLoader utilizado para carregar a tela atual.
     */
    private void injectManager(FXMLLoader loader) {
        Object controller = loader.getController();
        if (controller instanceof BaseController) {
            ((BaseController) controller).setSceneManager(this);
        }
    }
}
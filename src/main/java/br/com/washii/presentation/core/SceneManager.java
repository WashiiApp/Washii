package br.com.washii.presentation.core;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

/**
 * Responsável pelo gerenciamento de navegação, fluxo de telas e temas da aplicação Washii.
 * Centraliza a lógica de carregamento de arquivos FXML e a aplicação dinâmica de estilos CSS.
 * * @author Grupo Washii
 */
public class SceneManager {

    /** Janela principal da aplicação (Stage). */
    private final Stage primaryStage;
    private final StyleManager styleManager;

    /** Área de conteúdo para carregamento de telas internas (ex: dashboard). */
    private Pane contentArea;

    /**
     * Construtor do gerenciador de cenas.
     * @param primaryStage O Stage principal fornecido pela classe Application do JavaFX.
     */
    public SceneManager(Stage primaryStage, StyleManager styleManager) {
        this.primaryStage = primaryStage;
        this.styleManager = styleManager;
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
            styleManager.applyTo(scene);

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

    public void loadCenterBorderPane(String fxmlPath) {
    try {
        // 1. Obtém a raiz da cena atual através do Stage principal
        Parent root = primaryStage.getScene().getRoot();

        // 2. Verifica se a raiz (ou o container principal) é um BorderPane
        if (root instanceof BorderPane mainLayout) {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newNode = loader.load();
            injectManager(loader);

            // 3. Troca o centro diretamente
            mainLayout.setCenter(newNode);
            
            // Opcional: Efeito visual
            applyFadeTransition(newNode);

        } else {
            throw new IllegalStateException("A tela atual não possui um BorderPane como raiz.");
        }
        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void applyFadeTransition(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(250), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
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
            styleManager.applyTo(popupScene);

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
            BaseController baseController = (BaseController) controller;

            baseController.setSceneManager(this);
        }
    }
}
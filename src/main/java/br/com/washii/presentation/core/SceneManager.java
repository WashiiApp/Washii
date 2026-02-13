package br.com.washii.presentation.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import br.com.washii.infra.factory.ControllerFactory;

/**
 * Gerenciador de navegação que delega o feedback visual ao OverlayManager.
 */
public class SceneManager {

    private final Stage primaryStage;
    private final StyleManager styleManager;
    private final ControllerFactory controllerFactory;
    private final OverlayManager overlayManager;

    private Pane contentArea;

    public SceneManager(Stage primaryStage, StyleManager styleManager, ControllerFactory controllerFactory) {
        this.primaryStage = primaryStage;
        this.styleManager = styleManager;
        this.controllerFactory = controllerFactory;
        this.overlayManager = new OverlayManager();
    }

    // --- DELEGAÇÃO DE CARREGAMENTO ---

    public void setModoCarregamento(boolean ativar) {
        overlayManager.setStatus(ativar, "Processando...");
    }

    public void setModoCarregamento(boolean ativar, String mensagem) {
        overlayManager.setStatus(ativar, mensagem);
    }

    // --- NAVEGAÇÃO ---

    public FXMLLoader switchFullScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(controllerFactory::criar);
            Parent root = loader.load();
            injectSceneManager(loader);

            // 1. Prepara o conteúdo dentro do OverlayManager
            overlayManager.setContent(root);

            // 2. RECUPERA o wrapper
            StackPane wrapper = overlayManager.getLoadingWrapper();

            // 3. A CHAVE PARA O ERRO: Se o wrapper já estiver em uma cena, remova-o de lá
            if (wrapper.getScene() != null) {
                wrapper.getScene().setRoot(new Pane()); // Define uma raiz vazia temporária para a cena antiga
            }

            // 4. Agora sim, cria a nova Scene com o wrapper "livre"
            Scene scene = new Scene(wrapper);
            styleManager.applyTo(scene);

            primaryStage.setScene(scene);
            primaryStage.show();

            return loader;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Troca apenas o centro de um BorderPane que já está na tela.
     * Como o switchFullScene já colocou o BorderPane dentro do OverlayManager,
     * basta trocar o nó do centro normalmente.
     */
    public FXMLLoader loadCenterBorderPane(String fxmlPath) {
        try {
            // Buscamos o BorderPane que está "escondido" dentro do wrapper do OverlayManager
            Parent root = primaryStage.getScene().getRoot(); // Isso retorna o StackPane do Overlay
            BorderPane mainLayout = findBorderPane(root);

            if (mainLayout != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                loader.setControllerFactory(controllerFactory::criar);
                Parent newNode = loader.load();
                injectSceneManager(loader);

                // Troca o centro diretamente.
                // O loading continua funcionando porque o BorderPane inteiro está sob o Overlay.
                mainLayout.setCenter(newNode);
                return loader;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Auxiliar para encontrar o BorderPane mesmo se ele estiver dentro de um StackPane de loading.
     */
    private BorderPane findBorderPane(Parent root) {
        // Caso 1: A própria raiz já é o BorderPane
        if (root instanceof BorderPane bp) return bp;

        // Caso 2: A raiz é o StackPane do OverlayManager (o mais provável)
        if (root instanceof StackPane sp) {
            // Procuramos nos filhos do StackPane se algum é o BorderPane
            return sp.getChildren().stream()
                    .filter(n -> n instanceof BorderPane)
                    .map(n -> (BorderPane) n)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    // --- MÉTODOS REUTILIZADOS (Sem alterações de lógica) ---

    public void loadInternalScreen(String fxmlPath) {
        if (contentArea == null) throw new IllegalStateException("contentArea não definida.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(controllerFactory::criar);
            Parent node = loader.load();
            injectSceneManager(loader);
            contentArea.getChildren().setAll(node);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(controllerFactory::criar);
            Parent root = loader.load();
            injectSceneManager(loader);
            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(primaryStage);
            Scene popupScene = new Scene(root);
            styleManager.applyTo(popupScene);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void setContentArea(Pane contentArea) { this.contentArea = contentArea; }
    public Stage getPrimaryStage(){ return this.primaryStage; }

    private void injectSceneManager(FXMLLoader loader) {
        Object controller = loader.getController();
        if (controller instanceof BaseController base) {
            base.setSceneManager(this);
        }
    }
}
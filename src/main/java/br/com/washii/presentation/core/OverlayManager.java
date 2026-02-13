package br.com.washii.presentation.core;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Gerencia exclusivamente a camada visual de carregamento (Overlay).
 */
public class OverlayManager {

    private final StackPane loadingWrapper;
    private final VBox loadingOverlay;
    private final Label loadingLabel;

    public OverlayManager() {
        this.loadingLabel = new Label("Processando...");
        this.loadingLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");

        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(60, 60);

        this.loadingOverlay = new VBox(indicator, loadingLabel);
        this.loadingOverlay.setAlignment(Pos.CENTER);
        this.loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        this.loadingOverlay.setVisible(false);
        this.loadingOverlay.setMouseTransparent(false);

        // O wrapper inicializa vazio, aguardando o conteúdo da tela
        this.loadingWrapper = new StackPane(loadingOverlay);
    }

    /**
     * Define o conteúdo principal atrás do overlay.
     */
    public void setContent(Node node) {
        loadingWrapper.getChildren().clear();
        // O node da tela entra primeiro (atrás), o overlay entra por último (frente)
        loadingWrapper.getChildren().addAll(node, loadingOverlay);
    }

    public void setStatus(boolean ativar, String mensagem) {
        Platform.runLater(() -> {
            if (mensagem != null) loadingLabel.setText(mensagem);
            loadingOverlay.setVisible(ativar);
        });
    }

    public StackPane getLoadingWrapper() {
        return loadingWrapper;
    }
}
package br.com.washii.presentation.utils;

import javafx.animation.FadeTransition;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class AvisoUtils {

    // --- Métodos Públicos (Atalhos) ---

    public static void exibirAvisoSucesso(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-sucesso");
    }

    public static void exibirAvisoErro(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-erro");
    }

    public static void exibirAvisoAlerta(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-alerta");
    }

    // --- Método Privado (A lógica centralizada) ---

    private static void configurarEExibir(TextFlow container, String mensagem, String classeCssTexto) {
        container.getChildren().clear();

        // 1. Cria o nó de texto
        Text texto = new Text(mensagem);
        
        // 2. Aplica as classes CSS ao TEXTO, não ao container
        texto.getStyleClass().addAll("mensagem-aviso", classeCssTexto);

        // 3. Adiciona ao container
        container.getChildren().add(texto);

        // Torna visível e ocupa espaço no layout
        container.setManaged(true);
        container.setVisible(true);

        // Animação de Surgimento (Fade In)
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}
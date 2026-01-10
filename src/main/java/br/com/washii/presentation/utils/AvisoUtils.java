package br.com.washii.presentation.utils;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class AvisoUtils {

    public static void exibirAvisoSucesso(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-sucesso");
    }

    public static void exibirAvisoErro(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-erro");
    }

    public static void exibirAvisoAlerta(TextFlow container, String mensagem) {
        configurarEExibir(container, mensagem, "msg-alerta");
    }

    public static void limparCampoAviso(TextFlow container, int segundos){
        // 1. Aguarda o tempo solicitado
        PauseTransition pause = new PauseTransition(Duration.seconds(segundos));
        
        pause.setOnFinished(e -> {
            // 2. Cria uma animação de SAÍDA (FadeOut)
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), container);
            fadeOut.setFromValue(1.0); // De totalmente visível
            fadeOut.setToValue(0.0);   // Para invisível
            
            // 3. Só desliga o Managed e Visible APÓS a animação acabar
            fadeOut.setOnFinished(event -> {
                container.setVisible(false);
                container.setManaged(false);
            });
        
            fadeOut.play();
        });
    
        pause.play();
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
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}
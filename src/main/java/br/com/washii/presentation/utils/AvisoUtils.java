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
        // Aguarda o tempo solicitado
        PauseTransition pause = new PauseTransition(Duration.seconds(segundos));
        
        pause.setOnFinished(e -> {
            // Cria uma animação de saída
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), container);
            fadeOut.setFromValue(1.0); // De totalmente visível
            fadeOut.setToValue(0.0);   // Para invisível
            
            // Só desliga o Managed e Visible APÓS a animação acabar
            fadeOut.setOnFinished(event -> {
                container.setVisible(false);
                container.setManaged(false);
            });
        
            fadeOut.play();
        });
    
        pause.play();
    }

    private static void configurarEExibir(TextFlow container, String mensagem, String classeCssTexto) {
        container.getChildren().clear();

        // Cria o nó de texto
        Text texto = new Text(mensagem);
        
        // Aplica as classes CSS ao TEXTO, não ao container
        texto.getStyleClass().addAll("mensagem-aviso", classeCssTexto);

        // Adiciona ao container
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
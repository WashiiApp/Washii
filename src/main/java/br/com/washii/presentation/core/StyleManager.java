package br.com.washii.presentation.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.Scene;

/**
 * Gerenciador de estilos CSS da aplicação Washii.
 * <p>
 * Esta classe é responsável por armazenar, organizar e aplicar arquivos de estilização 
 * em diferentes cenas (Scenes) do JavaFX, garantindo a consistência visual 
 * em toda a interface do usuário.
 * </p>
 * * @author Grupo Washii
 * @version 1.0
 */
public class StyleManager {

    /** * Armazena as URLs dos estilos CSS ativos de forma ordenada e única.
     * O uso de {@link LinkedHashSet} garante que a ordem de inserção seja respeitada,
     * o que é crucial para a cascata do CSS.
     */
    private final Set<String> activeStylesheets = new LinkedHashSet<>();

    /**
     * Construtor padrão da classe.
     * Inicializa um gerenciador de estilos vazio.
     */
    public StyleManager(){}

    /**
     * Construtor que inicializa o gerenciador com uma lista de folhas de estilo.
     * * @param stylesheet Varargs de caminhos relativos dos arquivos CSS (ex: "/styles/global.css").
     */
    public StyleManager(String... stylesheet){
        for (String s : stylesheet) {
            addStyle(s);
        }
    }

    /**
     * Adiciona um novo arquivo de estilo à lista de estilos ativos.
     * O caminho fornecido é convertido para uma URL externa válida.
     * * @param cssPath O caminho relativo do recurso CSS a partir da raiz do classpath.
     * @throws NullPointerException se o recurso no caminho especificado não for encontrado.
     */
    public void addStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        activeStylesheets.add(url);
    }

    /**
     * Remove um arquivo de estilo da lista de estilos ativos.
     * * @param cssPath O caminho relativo do recurso CSS a ser removido.
     */
    public void removeStyle(String cssPath) {
        String url = getClass().getResource(cssPath).toExternalForm();
        activeStylesheets.remove(url);
    }

    /**
     * Aplica todas as folhas de estilo armazenadas neste gerenciador a uma cena específica.
     * Este método substitui quaisquer estilos existentes na cena pelos estilos atuais do gerenciador.
     * * @param scene A {@link Scene} do JavaFX que receberá a estilização. 
     * Se a cena for nula, nenhuma operação será realizada.
     */
    public void applyTo(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().setAll(activeStylesheets);
        }
    }

    /**
     * Retorna uma cópia da lista de URLs de estilos atualmente ativos.
     * * @return Uma {@link List} contendo as URLs das folhas de estilo em formato String.
     */
    public List<String> getActiveStylesheets() {
        return new ArrayList<>(activeStylesheets);
    }
}
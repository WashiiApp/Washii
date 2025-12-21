package br.com.washii.feature.teste;


import br.com.washii.core.BaseController;
import br.com.washii.core.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainController extends BaseController {

    @FXML
    private StackPane contentArea; // O JavaFX injeta o componente do FXML aqui

    // Sobrescreve o metodo setSceneManager para que, assim que for injetodo o SceneManager, seje definido o contentArea
    @Override
    public void setSceneManager(SceneManager sceneManager) {
        super.setSceneManager(sceneManager);
        this.sceneManager.setContentArea(contentArea);
    }

    @FXML
    public void abrirTelaA() {
        sceneManager.loadInternalScreen("/br/com/washii/feature/teste/teste1.fxml");
    }

    @FXML
    public void abrirTelaB() {
        sceneManager.loadInternalScreen("/br/com/washii/feature/teste/teste2.fxml");
    }
}

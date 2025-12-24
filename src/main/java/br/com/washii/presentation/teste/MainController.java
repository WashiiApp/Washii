package br.com.washii.presentation.teste;


import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.core.SceneManager;
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
        sceneManager.loadInternalScreen("/br/com/washii/view/teste/teste1.fxml");
    }

    @FXML
    public void abrirTelaB() {
        sceneManager.loadInternalScreen("/br/com/washii/view/teste/teste2.fxml");
    }
}

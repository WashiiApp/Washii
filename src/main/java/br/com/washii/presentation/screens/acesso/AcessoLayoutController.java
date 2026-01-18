package br.com.washii.presentation.screens.acesso;

import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.core.SceneManager;
import javafx.fxml.FXML;
//import javafx.application.Platform;

public class AcessoLayoutController extends BaseController {

    // O JavaFX injeta automaticamente o controller do include aqui
    // O nome deve ser: idDoInclude + "Controller"
    @FXML private BaseController loginController;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        // Repassamos o manager para o controller que foi incluído via FXML
        if (loginController != null){
            loginController.setSceneManager(sceneManager);
        }
    }
}
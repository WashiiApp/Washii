package br.com.washii.presentation.acesso;

import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.core.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

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

    @FXML
    void initialize(){
        // Usamos o runLater para esperar a janela estar pronta e o manager injetado
        // Podemos desenvolver alguma lógica para pular o login, caso o sistema lembre quem está logado
        /*
        Platform.runLater(() -> {
            sceneManager.switchFullScene("/br/com/washii/view/home/main-layout-cliente.fxml");
        });
         */
    }

}

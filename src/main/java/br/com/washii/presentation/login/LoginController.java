package br.com.washii.presentation.login;

import br.com.washii.presentation.core.BaseController;
import javafx.fxml.FXML;

public class LoginController extends BaseController{

    @FXML
    void onEntrar(){
        sceneManager.switchFullScene("/br/com/washii/view/home/main-layout-negocio.fxml");
    }

}

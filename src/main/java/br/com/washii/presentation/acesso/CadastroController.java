package br.com.washii.presentation.acesso;

import br.com.washii.presentation.core.BaseController;
import javafx.fxml.FXML;

public class CadastroController extends BaseController {

    @FXML
    void irParaLogin(){
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/login.fxml");
    }
}

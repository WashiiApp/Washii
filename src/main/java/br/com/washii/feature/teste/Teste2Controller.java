package br.com.washii.feature.teste;

import br.com.washii.core.BaseController;
import javafx.fxml.FXML;

public class Teste2Controller extends BaseController{
    @FXML
    void onClick(){
        sceneManager.openPopup("/br/com/washii/feature/teste/teste1.fxml", "PopUP");
    }

}

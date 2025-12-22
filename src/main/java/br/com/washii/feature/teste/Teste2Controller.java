package br.com.washii.feature.teste;

import br.com.washii.core.BaseController;
import javafx.fxml.FXML;

public class Teste2Controller extends BaseController{
    @FXML
    void onClick(){
        sceneManager.removeGlobalStyle("/br/com/washii/style/tema-claro.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/tema-escuro.css");
    }

    @FXML
    void onClick2(){
        sceneManager.removeGlobalStyle("/br/com/washii/style/tema-escuro.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/tema-claro.css");
    }

}

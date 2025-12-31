package br.com.washii.presentation.acesso;

import br.com.washii.presentation.core.BaseController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.util.Duration;

public class LoginController extends BaseController{

    @FXML
    void onEntrar(){
        sceneManager.switchFullScene("/br/com/washii/view/home/main-layout-negocio.fxml");
    }

    @FXML
    void irParaCadastro(ActionEvent event){
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/cadastro.fxml");
    }

}

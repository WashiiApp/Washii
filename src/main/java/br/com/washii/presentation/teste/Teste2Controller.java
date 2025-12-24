package br.com.washii.presentation.teste;

import br.com.washii.presentation.core.BaseController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class Teste2Controller extends BaseController{
    @FXML
    void onClick(){
        sceneManager.removeGlobalStyle("/br/com/washii/style/tema-claro.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/tema-escuro.css");
        sceneManager.getPrimaryStage().getIcons().clear();
        sceneManager.getPrimaryStage().getIcons().addAll(
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-blue-32.png")),
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-blue-128.png"))
        );
    }

    @FXML
    void onClick2(){
        sceneManager.removeGlobalStyle("/br/com/washii/style/tema-escuro.css");
        sceneManager.addGlobalStyle("/br/com/washii/style/tema-claro.css");
        sceneManager.getPrimaryStage().getIcons().clear();
        sceneManager.getPrimaryStage().getIcons().addAll(
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-black-32.png")),
                new Image(getClass().getResourceAsStream("/br/com/washii/assets/icons/branding/app-icon/ic-washii-black-128.png"))
        );

    }

}

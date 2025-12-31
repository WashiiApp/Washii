package br.com.washii.presentation.home;

import br.com.washii.presentation.core.BaseController;
import javafx.fxml.FXML;

// Classe pai dos controllers do MainLayout 
// Agrupa metodos ou atributos comuns aos controllers do MainLayout 
public class MainLayoutController extends BaseController {
    @FXML
    void onSair(){
        // Realizar logout
        sceneManager.switchFullScene("/br/com/washii/view/acesso/acesso-layout.fxml");
    }
}

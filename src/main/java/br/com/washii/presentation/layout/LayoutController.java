package br.com.washii.presentation.layout;

import br.com.washii.domain.session.Sessao;
import br.com.washii.presentation.core.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// Classe pai dos controllers do MainLayout 
// Agrupa metodos ou atributos comuns aos controllers do MainLayout 
public abstract class LayoutController extends BaseController {

    @FXML
    private Label lblBoasVindas;

    @FXML
    void onSair(){
        // Realizar logout
        sceneManager.switchFullScene("/br/com/washii/view/acesso/acesso-layout.fxml");
    }

    protected void setBoasVindas(){
        String nomeUser = Sessao.getInstance().getUsuarioLogado().getNome();

        String pNomeUser = nomeUser.split(" ")[0];

        lblBoasVindas.setText("Bem vindo, " + pNomeUser);
    }
}

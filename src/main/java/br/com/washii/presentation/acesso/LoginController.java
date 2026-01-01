package br.com.washii.presentation.acesso;

import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.AutenticacaoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LoginController extends BaseController{

    

    @FXML
    private Button btnEntrar;

    @FXML
    private TextFlow lblMsgErro;

    @FXML
    private Hyperlink lnkCadastro;

    @FXML
    private Hyperlink lnkEsqueciSenha;

    @FXML
    private PasswordField pwdSenha;

    @FXML
    private TextField txtEmail;

    @FXML
    void onEntrar(ActionEvent event){
        if (txtEmail.getText().isBlank() || pwdSenha.getText().isBlank()) {
            exibirErro("Preencha todos os campos");
            return;
        }

        
       sceneManager.switchFullScene("/br/com/washii/view/home/main-layout-negocio.fxml");
      
    }

    @FXML
    void irParaCadastro(ActionEvent event){
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/cadastro.fxml");
    }

    private void exibirErro(String msg){
        lblMsgErro.setVisible(true);
        lblMsgErro.getChildren().clear();
        Text erro = new Text(msg);
        lblMsgErro.getChildren().add(erro);
    }

}

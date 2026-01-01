package br.com.washii.presentation.acesso;

import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.AutenticacaoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LoginController extends BaseController{

    private AutenticacaoService autenticacaoService;

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

    // AutenticacaoService é injetado no SceneManager
    public LoginController(AutenticacaoService authService){
        this.autenticacaoService = authService;
    }

    @FXML
    void onEntrar(ActionEvent event){
        if (txtEmail.getText().isBlank() || pwdSenha.getText().isBlank()) {
            exibirErro("Preencha todos os campos");
            return;
        }

        String email = txtEmail.getText();
        String senha = pwdSenha.getText();

        try {
            autenticacaoService.realizarLogin(email, senha);

            sceneManager.switchFullScene("/br/com/washii/view/home/main-layout-negocio.fxml");

        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            exibirErro("Ocorreu um erro inesperado.");
        }
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

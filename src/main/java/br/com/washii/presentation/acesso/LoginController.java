package br.com.washii.presentation.acesso;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.AutenticacaoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
    private TextFlow errorContainer;

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
        limparCampoErro();

        if (txtEmail.getText().isBlank() || pwdSenha.getText().isBlank()) {
            exibirErro("Preencha todos os campos");
            return;
        }

        if (!txtEmail.getText().isBlank() && !txtEmail.getText().contains("@")){
            exibirErro("E-mail inválido");
            return;
        }

        btnEntrar.setDisable(true);
        Scene secen = btnEntrar.getScene();
        secen.setCursor(Cursor.WAIT);

        String email = txtEmail.getText();
        String senha = pwdSenha.getText();

        try {
            Usuario user = autenticacaoService.realizarLogin(email, senha);

            Thread.sleep(1000);

            // Escolhe a tela com base no TipoUsuario
            if (user.getTipoUsuario() == TipoUsuario.CLIENTE){
                sceneManager.switchFullScene("/br/com/washii/view/layout/cliente-layout.fxml");
            } else if (user.getTipoUsuario() == TipoUsuario.NEGOCIO){
                sceneManager.switchFullScene("/br/com/washii/view/layout/negocio-layout.fxml");
            } else {
                exibirErro("Tipo de Usuário desconhecido.");
            }

        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            exibirErro("Ocorreu um erro inesperado.");
        } finally {
            btnEntrar.setDisable(false);
            secen.setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    void irParaCadastro(ActionEvent event){
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/cadastro.fxml");
    }

    @FXML
    void onEsqueciSenha(ActionEvent event) {
        limparCampoErro();
        exibirErro("Entre em contato com o suporte para resetar sua senha");
    }

    private void exibirErro(String msg){
        errorContainer.setVisible(true);
        Text erro = new Text(msg);
        errorContainer.getChildren().add(erro);
    }

    private void limparCampoErro(){
        errorContainer.setVisible(false);
        errorContainer.getChildren().clear();
    }
}

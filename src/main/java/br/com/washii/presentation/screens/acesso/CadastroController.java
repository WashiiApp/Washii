package br.com.washii.presentation.screens.acesso;

import br.com.washii.domain.entities.Cliente;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.UsuarioService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextFlow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadastroController extends BaseController {

    private static final int NUMERO_MINIMO_CARACTERE_PARA_SENHA = 4;

    private final UsuarioService usuarioService;

    @FXML private RadioButton rbNegocio, rbCliente;

    @FXML private ToggleGroup tipoUsuario;

    @FXML private Button btnCadastrar, btnIrParaLogin;

    @FXML private PasswordField pwdSenha, pwdSenhaConferida;

    @FXML private TextFlow avisoContainer;

    @FXML private TextField txtCEP, txtCidade, txtEmail, txtEstado, txtNome;


    public CadastroController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/login.fxml");
    }

   @FXML
    void onCadastrar(ActionEvent event) {
    limparCampoAviso();

    if (!validarDados()) return;

    cadastrarUsuarioAsync(criarUsuario());
   }

    private boolean validarDados() {
        if (verificarCamposEmBranco()) return false;

        if (!validarCampoEmail()) return false;

        if (!validarCampoSenha()) return false;

        return true;
    }

    private boolean verificarCamposEmBranco() {
        if (    tipoUsuario.getSelectedToggle() == null ||
                txtNome.getText().isBlank()   ||
                txtEmail.getText().isBlank()  ||
                txtCEP.getText().isBlank()    ||
                txtEstado.getText().isBlank() ||
                txtCidade.getText().isBlank() ||
                pwdSenha.getText().isBlank()  ||
                pwdSenhaConferida.getText().isBlank()) {
            exibirAvisoErro("Preencha todos os campos.");
            return true;
        }

        return false;
    }

    private boolean validarCampoEmail() {
        if (!txtEmail.getText().contains("@")){
            exibirAvisoErro("E-mail inválido");
            return false;
        }
        return true;
    }

    private boolean validarCampoSenha() {
        if (pwdSenha.getText().length() < NUMERO_MINIMO_CARACTERE_PARA_SENHA){
            exibirAvisoErro("A senha deve ter no mínimo " + NUMERO_MINIMO_CARACTERE_PARA_SENHA + " caracteres");
            return false;
        }
        if (!pwdSenha.getText().equals(pwdSenhaConferida.getText())) {
            exibirAvisoErro("As senhas não conferem.");
            return false;
        }

        return true;
    }

    private Usuario criarUsuario() {
        Endereco endereco = criarEndereco();

        RadioButton selecionado = (RadioButton) tipoUsuario.getSelectedToggle();
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = pwdSenha.getText().trim();

        Usuario usuario;

        if (selecionado == rbNegocio) {
            usuario = new LavaJato(nome, email, senha, endereco, TipoUsuario.NEGOCIO);
        } else if (selecionado == rbCliente) {
            usuario = new Cliente(nome, email, senha, endereco, TipoUsuario.CLIENTE);
        } else {
            exibirAvisoErro("Tipo de conta não identificado.");
            return null;
        }

        return usuario;
    }

    private Endereco criarEndereco(){
        String cep = txtCEP.getText();
        String estado = txtEstado.getText();
        String cidade = txtCidade.getText();

        return new Endereco(cep, estado, cidade, null, null, null, "Brasil");

    }

    private void cadastrarUsuarioAsync(Usuario usuario) {
        ativarModoCarregamento("Cadastrando...");

        CompletableFuture.supplyAsync(() -> {
            usuarioService.salvarNovoUsuario(usuario);
            return null;
        })
        .thenRun(() -> {
            Platform.runLater(() -> {
                exibirAvisoSucesso("Cadastro realizado com sucesso! Você já pode fazer login.");
            });
        })
        .exceptionally(this::tratarErroCadastro)
        .whenComplete((_, ex) -> {
            desativarModoCarregamento();
        });
    }

    private Void tratarErroCadastro(Throwable ex) {
        Throwable erro = ex.getCause() != null ? ex.getCause(): ex;

        Platform.runLater(() -> {
            if (erro instanceof NegocioException){
                exibirAvisoErro(erro.getMessage());
                return;
            }
            exibirAvisoErro("Ocorreu um erro inesperado: " + erro.getMessage());
            erro.printStackTrace();
        });
        return null;
    }

    private void ativarModoCarregamento(String mensagem) {
        btnCadastrar.setDisable(true);
        btnIrParaLogin.setDisable(true);
        sceneManager.setModoCarregamento(true, mensagem);
    }

    private void desativarModoCarregamento() {
        btnCadastrar.setDisable(false);
        btnIrParaLogin.setDisable(false);
        sceneManager.setModoCarregamento(false);
    }

    private void exibirAvisoErro(String msg){
        AvisoUtils.exibirAvisoErro(avisoContainer, msg);
    }

    private void exibirAvisoSucesso(String msg) {
        AvisoUtils.exibirAvisoSucesso(avisoContainer, msg);
    }

    private void limparCampoAviso(){
        avisoContainer.getChildren().clear();
    }
}
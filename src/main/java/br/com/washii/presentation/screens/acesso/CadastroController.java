package br.com.washii.presentation.screens.acesso;

import br.com.washii.domain.entities.Cliente;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.presentation.utils.ValidadorDeEmail;
import br.com.washii.service.UsuarioService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextFlow;
import java.util.concurrent.CompletableFuture;

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
    void irParaLogin() {
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/login.fxml");
    }

   @FXML
    void onCadastrar() {
        limparCampoAviso();

        if (!validarDados()) return;

        executarCadastroAsync(criarUsuario());
   }

    private boolean validarDados() {
        if (verificarCamposEmBranco()) {
            exibirAvisoErro("Preencha todos os campos.");
            return false;
        }

        if (!isEmailValido()) {
            exibirAvisoErro("E-mail inválido");
            return false;
        }

        if (!verificarNumeroMinimoDeCaracteresParaSenha()) {
            exibirAvisoErro("A senha deve ter no mínimo " + NUMERO_MINIMO_CARACTERE_PARA_SENHA + " caracteres");
            return false;
        }

        if (!verificarSeSenhasConferem()) {
            exibirAvisoErro("As senhas não conferem.");
            return false;
        }

        return true;
    }

    private boolean verificarCamposEmBranco() {
        return  tipoUsuario.getSelectedToggle() == null ||
                txtNome.getText().isBlank()   ||
                txtEmail.getText().isBlank()  ||
                txtCEP.getText().isBlank()    ||
                txtEstado.getText().isBlank() ||
                txtCidade.getText().isBlank() ||
                pwdSenha.getText().isBlank()  ||
                pwdSenhaConferida.getText().isBlank();
    }

    private boolean isEmailValido() {
        return ValidadorDeEmail.validar(txtEmail.getText());
    }

    private boolean verificarNumeroMinimoDeCaracteresParaSenha() {
        return pwdSenha.getText().length() >= NUMERO_MINIMO_CARACTERE_PARA_SENHA;
    }

    private boolean verificarSeSenhasConferem() {
        return pwdSenha.getText().equals(pwdSenhaConferida.getText());
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
            throw new IllegalStateException("Tipo de conta inválido");
        }

        return usuario;
    }

    private Endereco criarEndereco(){
        String cep = txtCEP.getText();
        String estado = txtEstado.getText();
        String cidade = txtCidade.getText();

        return new Endereco(cep, estado, cidade, null, null, null, "Brasil");
    }

    private void executarCadastroAsync(Usuario usuario) {
        ativarModoCarregamento("Cadastrando...");

        CompletableFuture
                .runAsync(() -> usuarioService.salvarNovoUsuario(usuario))
                .thenRun(this::exibirMensagemSucesso)
                .exceptionally(this::tratarErroCadastro)
                .whenComplete((_, _) -> desativarModoCarregamento());
    }

    private void ativarModoCarregamento(String mensagem) {
        btnCadastrar.setDisable(true);
        btnIrParaLogin.setDisable(true);
        sceneManager.setModoCarregamento(true, mensagem);
    }

    private void exibirMensagemSucesso() {
        Platform.runLater(() -> {
            exibirAvisoSucesso("Cadastro realizado com sucesso! Você já pode fazer login.");
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
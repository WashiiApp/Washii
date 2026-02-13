package br.com.washii.presentation.screens.acesso;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.AutenticacaoService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class LoginController extends BaseController {

    private final AutenticacaoService autenticacaoService;

    private final Map<TipoUsuario, String> rotasPorTipoUsuario = Map.of(
            TipoUsuario.NEGOCIO, "/br/com/washii/view/layout/negocio-layout.fxml",
            TipoUsuario.CLIENTE, "/br/com/washii/view/layout/cliente-layout.fxml"
    );

    @FXML private Button btnEntrar;

    @FXML private TextFlow containerAviso;

    @FXML private Hyperlink lnkCadastro, lnkEsqueciSenha;

    @FXML private PasswordField pwdSenha;

    @FXML private TextField txtEmail;

    public LoginController(AutenticacaoService autenticacaoService){
        this.autenticacaoService = autenticacaoService;
    }

    @FXML
    void onEntrar(){
        limparCampoErro();
        if (!validarDados()) return;
        executarLoginAsync();
    }

    private boolean validarDados() {
        if (verificarCamposEmBranco()){
            exibirErro("Preencha todos os campos");
            return false;
        }
        if (!validarEmail()) {
            exibirErro("E-mail inválido");
            return false;
        }
        return true;
    }

    private boolean verificarCamposEmBranco() {
        return txtEmail.getText().isBlank() || pwdSenha.getText().isBlank();
    }

    private boolean validarEmail() {
        if (!txtEmail.getText().contains("@")){
            exibirErro("E-mail inválido");
            return false;
        }
        return true;
    }

    private void executarLoginAsync() {
        ativarModoCarregamento("Validando dados...");

        CompletableFuture
                .supplyAsync(this::autenticarUsuario)
                .thenApply(user -> {
                    iniciarSessao(user);
                    return user.getTipoUsuario();
                })
                .thenAccept(this::navegarParaHome)
                .exceptionally(this::tratarErroLogin)
                .whenComplete((_, _) -> desativarModoCarregamento());
    }

    private void ativarModoCarregamento(String mensagem){
        btnEntrar.setDisable(true);
        sceneManager.setModoCarregamento(true, mensagem);
    }

    private void desativarModoCarregamento(){
        btnEntrar.setDisable(false);
        sceneManager.setModoCarregamento(false);
    }

    private Usuario autenticarUsuario() {
        return autenticacaoService.realizarLogin(
                txtEmail.getText(),
                pwdSenha.getText()
        );
    }

    private void iniciarSessao(Usuario user) {
        Sessao.getInstance().iniciarSessao(user);
    }

    private void navegarParaHome(TipoUsuario tipoUsuario) {
        Platform.runLater(() -> {
            String rota = rotasPorTipoUsuario.get(tipoUsuario);
            if (rota == null){
                throw new RuntimeException("Usuario não mapeado");
            }
            sceneManager.switchFullScene(rota);
        });
    }

    private Void tratarErroLogin(Throwable ex) {
        Throwable erro = ex.getCause() != null? ex.getCause(): ex;

        Platform.runLater(() -> {
            if (erro instanceof NegocioException){
                exibirErro(erro.getMessage());
                return;
            }
            exibirErro("Ocorreu um erro inesperado: " + erro.getMessage());
            erro.printStackTrace();
        });
        return null;
    }

    @FXML
    void irParaCadastro(ActionEvent event){
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/cadastro.fxml");
    }

    @FXML
    void onEsqueciSenha(ActionEvent event) {
        limparCampoErro();
        AvisoUtils.exibirAvisoAlerta(containerAviso, "Entre em contato com o suporte para resetar sua senha");
    }

    private void exibirErro(String msg){
        AvisoUtils.exibirAvisoErro(containerAviso, msg);
    }

    private void limparCampoErro(){
        containerAviso.getChildren().clear();
    }
}
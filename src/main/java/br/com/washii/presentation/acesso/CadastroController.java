package br.com.washii.presentation.acesso;

import br.com.washii.domain.entities.Cliente;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CadastroController extends BaseController {

    private UsuarioService usuarioService;

    @FXML
    private RadioButton rbNegocio;

    @FXML
    private RadioButton rbCliente;

    @FXML
    private ToggleGroup tipoUsuario;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnIrParaLogin;

    @FXML
    private PasswordField pwdSenha;

    @FXML
    private PasswordField pwdSenhaConferida;

    @FXML
    private TextFlow avisoContainer;

    @FXML
    private TextField txtCEP;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtNome;

    public CadastroController(UsuarioService userService){
        this.usuarioService = userService;
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        sceneManager.loadCenterBorderPane("/br/com/washii/view/acesso/login.fxml");
    }

   @FXML
    void onCadastrar(ActionEvent event) {
    limparCampoAviso();

    // 1. Captura e Validação do RadioButton
    RadioButton selecionado = (RadioButton) tipoUsuario.getSelectedToggle();
    if (selecionado == null) {
        exibirAvisoErro("Por favor, selecione o tipo da conta.");
        return;
    }

    // 2. Captura dos Textos
    String nome = txtNome.getText();
    String email = txtEmail.getText();
    String cep = txtCEP.getText();
    String estado = txtEstado.getText();
    String cidade = txtCidade.getText();
    String senha = pwdSenha.getText();
    String senha2 = pwdSenhaConferida.getText();

    // 3. Validação de campos vazios e senhas
    if (nome.isBlank() || email.isBlank() || cep.isBlank() || senha.isBlank()) {
        exibirAvisoErro("Por favor, preencha todos os campos.");
        return;
    }

    if (!email.isBlank() && !email.contains("@")){
        exibirAvisoErro("E-mail inválido");
        return;
    }

    if (!senha.equals(senha2)) {
        exibirAvisoErro("As senhas não conferem.");
        return;
    }

    // 4. Criação dos Objetos
    Endereco endereco = new Endereco(cep, estado, cidade, null, null, null, "Brasil");
    Usuario usuario;

    // Comparação mais segura (verifique se o texto no Scene Builder bate exatamente)
    if (selecionado == rbNegocio) {
        usuario = new LavaJato(nome, email, senha, endereco, TipoUsuario.NEGOCIO);
    } else if (selecionado == rbCliente) {
        usuario = new Cliente(nome, email, senha, endereco, TipoUsuario.CLIENTE);
    } else {
        exibirAvisoErro("Tipo de conta não identificado.");
        return;
    }

    try {
        // 5. Chamada ao Service
        usuarioService.salvarNovoUsuario(usuario);

        // 6. Feedback de Sucesso
        exibirAvisoSucesso("Cadastro realizado com sucesso! Você já pode fazer login.");

    } catch (NegocioException e) {
        exibirAvisoErro(e.getMessage());
    } catch (Exception e) {
        exibirAvisoErro("Erro inesperado: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void exibirAvisoErro(String msg){
        avisoContainer.setVisible(true);
        avisoContainer.getStyleClass().clear();

        avisoContainer.getStyleClass().add("error-container");

        Text erro = new Text(msg);
        avisoContainer.getChildren().add(erro);
    }

    private void exibirAvisoSucesso(String msg) {
        avisoContainer.setVisible(true);
        avisoContainer.getStyleClass().clear();

        avisoContainer.getStyleClass().add("success-container");

        Text aviso = new Text(msg);
        avisoContainer.getChildren().add(aviso);
    }

    private void limparCampoAviso(){
        avisoContainer.getStyleClass().clear();
        avisoContainer.setVisible(false);
        avisoContainer.getChildren().clear();
    }
}

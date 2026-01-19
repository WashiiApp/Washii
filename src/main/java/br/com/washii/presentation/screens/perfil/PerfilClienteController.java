package br.com.washii.presentation.screens.perfil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import br.com.washii.domain.entities.Cliente;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.UsuarioService;

import java.io.File;
import java.util.List;

public class PerfilClienteController {

    private final UsuarioService usuarioService;

    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;
    @FXML private Circle circleFoto;
    @FXML private FontIcon iconCamera;
    @FXML private StackPane paneFoto;
    @FXML private TextFlow containerAviso;
    @FXML private TextField txtNomeCliente; 
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCep;
    @FXML private TextField txtEstado;
    @FXML private TextField txtCidade;
    @FXML private TextField txtRua;
    @FXML private TextField txtBairro;
    @FXML private TextField txtNumero;

    private List<TextField> todosCampos;
    private boolean modoEdicao = false;

    public PerfilClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @FXML
    public void initialize() {
        todosCampos = List.of(
            txtNomeCliente, txtEmail, txtTelefone, txtCep, 
            txtEstado, txtCidade, txtRua, txtBairro, txtNumero
        );

        configurarEstado(false);
        carregarDados();
    }

    @FXML
    void handleAlternarEdicao(ActionEvent event) {
        if (modoEdicao) {
            if (salvarAlteracoes()) {
                configurarEstado(false);
                modoEdicao = false;
            }
        } else {
            configurarEstado(true);
            modoEdicao = true;
        }
    }

    @FXML
    void handleCancelarEdicao(ActionEvent event) {
        carregarDados();
        configurarEstado(false);
        modoEdicao = false;
    }

    @FXML
    void handleSelecionarFoto(MouseEvent event) {
        if (!modoEdicao) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Foto");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );

        File arquivo = fileChooser.showOpenDialog(circleFoto.getScene().getWindow());
        if (arquivo != null) {
            Image imagem = new Image(arquivo.toURI().toString());
            circleFoto.setFill(new ImagePattern(imagem));
        }
    }

    private void configurarEstado(boolean editavel) {
        todosCampos.forEach(tf -> {
            tf.setEditable(editavel);
            tf.setFocusTraversable(editavel);
        });

        if (editavel) {
            btnEditar.setText("Salvar");
            ((FontIcon) btnEditar.getGraphic()).setIconLiteral("mdi2c-check");
            btnCancelar.setVisible(true);
            iconCamera.setVisible(true);
            paneFoto.setCursor(Cursor.HAND);
            circleFoto.setOpacity(0.7);
        } else {
            btnEditar.setText("Editar");
            ((FontIcon) btnEditar.getGraphic()).setIconLiteral("mdi2p-pencil");
            btnCancelar.setVisible(false);
            iconCamera.setVisible(false);
            paneFoto.setCursor(Cursor.DEFAULT);
            circleFoto.setOpacity(1.0);
        }
    }

    private void carregarDados() {
        Usuario logado = buscarUsuarioLogado();
        
        if (logado instanceof Cliente cliente) {
            txtNomeCliente.setText(cliente.getNome());
            txtEmail.setText(cliente.getEmail());
            txtTelefone.setText(cliente.getTelefone());

            if (cliente.getEndereco() != null) {
                txtCep.setText(cliente.getEndereco().getCep());
                txtEstado.setText(cliente.getEndereco().getEstado());
                txtCidade.setText(cliente.getEndereco().getCidade());
                txtRua.setText(cliente.getEndereco().getRua());
                txtBairro.setText(cliente.getEndereco().getBairro());
                txtNumero.setText(cliente.getEndereco().getNumero());
            }

            try {
                Image img = new Image(getClass().getResourceAsStream("/br/com/washii/assets/images/perfil/profile-placeholder.png"));
                circleFoto.setFill(new ImagePattern(img));
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem: " + e.getMessage());
            }
        }
    }

    private boolean salvarAlteracoes() {
        try {
            validarCampos();

            Cliente cliente = (Cliente) buscarUsuarioLogado();
            cliente.setNome(txtNomeCliente.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setTelefone(txtTelefone.getText());

            if (cliente.getEndereco() != null) {
                cliente.getEndereco().setCep(txtCep.getText());
                cliente.getEndereco().setEstado(txtEstado.getText());
                cliente.getEndereco().setCidade(txtCidade.getText());
                cliente.getEndereco().setRua(txtRua.getText());
                cliente.getEndereco().setBairro(txtBairro.getText());
                cliente.getEndereco().setNumero(txtNumero.getText());
            }

            usuarioService.atualizarUsuario(cliente);
            AvisoUtils.exibirAvisoSucesso(containerAviso, "Perfil atualizado!");
            return true;

        } catch (IllegalArgumentException e) {
            AvisoUtils.exibirAvisoAlerta(containerAviso, e.getMessage());
        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(containerAviso, e.getMessage());
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Erro ao salvar.");
        } finally {
            limparAvisos(5);
        }
        return false;
    }

    private void validarCampos() throws IllegalArgumentException {
        if (txtNomeCliente.getText().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (txtEmail.getText().isBlank() || !txtEmail.getText().contains("@")) throw new IllegalArgumentException("E-mail inválido.");
    }

    private void limparAvisos(int tempo) {
        AvisoUtils.limparCampoAviso(containerAviso, tempo);
    }

    private Usuario buscarUsuarioLogado() {
        long id = Sessao.getInstance().getUsuarioLogado().getId();
        return usuarioService.buscarUsuarioPorId(id);
    }
}
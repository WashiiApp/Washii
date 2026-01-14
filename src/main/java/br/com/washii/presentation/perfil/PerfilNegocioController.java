package br.com.washii.presentation.perfil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
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

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.UsuarioService;

import java.io.File;
import java.util.List;

public class PerfilNegocioController {

    private UsuarioService usuarioService;

    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;
    @FXML private Circle circleFoto;
    @FXML private FontIcon iconCamera;
    @FXML private StackPane paneFoto;
    @FXML private TextFlow containerAviso;
    
    // Campos de Texto
    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtCapacidade;

    @FXML
    private TextField txtCep;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtFim;

    @FXML
    private TextField txtInicio;

    @FXML
    private TextField txtNomeNegocio;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private TextField txtRua;


    private List<TextField> todosCampos;
    private boolean modoEdicao = false;

    public PerfilNegocioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;

    }

    @FXML
    public void initialize() {
        // Agrupamos os campos para facilitar a manipulação em massa
        todosCampos = List.of(
            txtBairro, txtCapacidade, txtCep, txtCidade, txtCnpj, 
            txtEmail, txtEstado, txtFim, txtInicio, txtNomeNegocio, 
            txtNumero, txtRazaoSocial, txtRua
        );

        // Estado inicial: Apenas leitura
        configurarEstado(false);
        carregarDados(buscarUsuarioLogado());
    }

    /**
     * Alterna entre o modo de visualização e edição.
     */
    @FXML
    void handleAlternarEdicao(ActionEvent event) {
        if (modoEdicao) {
            // Se já estava editando, a ação agora é SALVAR
            salvarAlteracoes();
            // Se salvou com sucesso:
            configurarEstado(false);
            modoEdicao = false;
        } else {
            // Se estava visualizando, a ação agora é EDITAR
            configurarEstado(true);
            modoEdicao = true;
        }
    }

    @FXML
    void handleCancelarEdicao(ActionEvent event) {
        carregarDados(buscarUsuarioLogado());
        configurarEstado(false);
        modoEdicao = !modoEdicao;
    }

    /**
     * Gerencia a troca de foto de perfil usando o FileChooser.
     */
    @FXML
    void handleSelecionarFoto(MouseEvent event) {
        if (!modoEdicao) return; // Só permite trocar foto se estiver editando

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Logo do Negócio");
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
        // 1. Alterna a propriedade editable de todos os campos
        todosCampos.forEach(tf -> tf.setEditable(editavel));

        // 2. Atualiza o texto e estilo do botão
        if (editavel) {
            btnEditar.setText("Salvar");
            btnEditar.getStyleClass().remove("button-primary");
            btnEditar.getStyleClass().add("btn-salvar");
            iconCamera.setVisible(true);
            paneFoto.setCursor(Cursor.HAND);
            circleFoto.setOpacity(0.7);
        } else {
            btnEditar.setText("Editar");
            btnEditar.getStyleClass().remove("btn-salvar");
            btnEditar.getStyleClass().add("button-primary");
            iconCamera.setVisible(false);
            paneFoto.setCursor(Cursor.DEFAULT);
            circleFoto.setOpacity(1.0);
        }

        btnCancelar.setVisible(editavel);
        btnCancelar.setManaged(editavel);

    }

    private void carregarDados(Usuario logado) {
        
        // 2. Verifica se o usuário logado é de fato um Negocio (Cast seguro)
        if (logado instanceof Negocio negocio) {
            
            // Dados Principais
            txtNomeNegocio.setText(negocio.getNome());
            txtEmail.setText(negocio.getEmail());
            
            // Endereço
            // Assumindo que seu objeto Negocio tem um objeto Endereco interno
            if (negocio.getEndereco() != null) {
                txtCep.setText(negocio.getEndereco().getCep());
                txtRua.setText(negocio.getEndereco().getRua());
                txtNumero.setText(negocio.getEndereco().getNumero());
                txtBairro.setText(negocio.getEndereco().getBairro());
                txtCidade.setText(negocio.getEndereco().getCidade());
                txtEstado.setText(negocio.getEndereco().getEstado()); // Nome por extenso
            }

            // Configurações Operacionais
            txtInicio.setText(negocio.getInicioExpediente().toString());
            txtFim.setText(negocio.getFimExpediente().toString());
            txtCapacidade.setText(String.valueOf(negocio.getCapacidadeAtendimentoSimultaneo()));

            // Dados Jurídicos
            txtCnpj.setText(negocio.getCnpj());
            txtRazaoSocial.setText(negocio.getRazaoSocial());

            // Carregamento da Foto/Logo
            try {
                Image img = new Image(getClass().getResourceAsStream("/br/com/washii/assets/images/perfil/image.png")); // true para carregar em background
                circleFoto.setFill(new ImagePattern(img));
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem: " + e.getMessage());
                // Mantém o preenchimento padrão caso a imagem falhe
            }
            
        }
    }

    private void salvarAlteracoes() {
        try {
            // 1. Valida os dados da UI
            validarCampos();

            // 2. Pega o objeto para atualizar
            Negocio negocio = (Negocio) buscarUsuarioLogado();

            // 3. Mapeia os campos da tela para o objeto
            negocio.setNome(txtNomeNegocio.getText());
            negocio.setEmail(txtEmail.getText());
            negocio.setCnpj(txtCnpj.getText());
            negocio.setRazaoSocial(txtRazaoSocial.getText());
            negocio.setCapacidadeAtendimentoSimultaneo(Integer.parseInt(txtCapacidade.getText()));
            
            // Atualiza o Endereço (se houver objeto de endereço)
            if (negocio.getEndereco() != null) {
                negocio.getEndereco().setCep(txtCep.getText());
                negocio.getEndereco().setRua(txtRua.getText());
                negocio.getEndereco().setNumero(txtNumero.getText());
                negocio.getEndereco().setBairro(txtBairro.getText());
                negocio.getEndereco().setCidade(txtCidade.getText());
                negocio.getEndereco().setEstado(txtEstado.getText());
            }

            usuarioService.atualizarUsuario(negocio);

            
            AvisoUtils.exibirAvisoSucesso(containerAviso, "Usuario atualizado com sucesso!");

        } catch (IllegalArgumentException e) {
            AvisoUtils.exibirAvisoAlerta(containerAviso, e.getMessage());
            // Se deu erro de validação, forçamos o modo edição a continuar aberto
            configurarEstado(true); 
            modoEdicao = true;
        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(containerAviso, e.getMessage());
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            limparAvisos(5);
        }
    }
        
    private void validarCampos() throws IllegalArgumentException {
        if (txtNomeNegocio.getText().isBlank()) throw new IllegalArgumentException("O nome do negócio é obrigatório.");

        if (txtCnpj.getText() != null) {
            String apenasNumerosCnpj = txtCnpj.getText().replaceAll("[.\\-/]", "");

            if (!apenasNumerosCnpj.matches("\\d+")) { 
                throw new IllegalArgumentException("O CNPJ deve conter apenas números (letras não são permitidas).");
            }
        
            if (apenasNumerosCnpj.length() != 14) {
                throw new IllegalArgumentException("O CNPJ deve ter exatamente 14 números.");
            }
        }
        

        if (txtEmail.getText().isBlank() || !txtEmail.getText().contains("@")) {
            throw new IllegalArgumentException("E-mail inválido ou vazio.");
        }

        String capText = txtCapacidade.getText();
        if (capText == null || capText.isBlank()) {
            throw new IllegalArgumentException("A capacidade de atendimento deve ser informada.");
        }
        
        try {
            Integer.parseInt(txtCapacidade.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A capacidade deve ser um número inteiro.");
        }
    }

    private void limparAvisos(int tempo){
        AvisoUtils.limparCampoAviso(containerAviso, tempo);
    }

    private Usuario buscarUsuarioLogado(){
        long id = Sessao.getInstance().getUsuarioLogado().getId();

        return usuarioService.buscarUsuarioPorId(id);
    }
}
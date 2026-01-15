package br.com.washii.presentation.perfil;

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

import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.UsuarioService;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PerfilNegocioController {

    private UsuarioService usuarioService;

    @FXML private Button btnEditar, btnCancelar;
    @FXML private Circle circleFoto;
    @FXML private FontIcon iconCamera;
    @FXML private StackPane paneFoto;
    @FXML private TextFlow containerAviso;
    
    // Campos de Texto
    @FXML private TextField txtBairro, txtCapacidade, txtCep, txtCidade, 
                            txtCnpj, txtEmail, txtEstado, txtFim, txtInicio, 
                            txtNomeNegocio, txtNumero, txtRazaoSocial, txtRua;

    private List<TextField> todosCampos;
    private boolean modoEdicao = false;
    private Usuario usuarioLogado;

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
        configurarModoEdicao(false);
        carregarDados(buscarUsuarioLogado());
    }

    /**
     * Alterna entre o modo de visualização e edição.
     */
    @FXML
    void handleAlternarEdicao(ActionEvent event) {
        if (modoEdicao) {
            // Se já estava editando, a ação agora é SALVAR
            boolean sucesso = salvarAlteracoes();
            
            // Se salvou com sucesso:
            configurarModoEdicao(!sucesso);
            modoEdicao = !sucesso;
            
        } else {
            // Se estava visualizando, a ação agora é EDITAR
            configurarModoEdicao(true);
            modoEdicao = true;
        }
    }

    @FXML
    void handleCancelarEdicao(ActionEvent event) {
        carregarDados(buscarUsuarioLogado());
        configurarModoEdicao(false);
        modoEdicao = false;
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

    private void configurarModoEdicao(boolean editavel) {
        // 1. Alterna a propriedade editable de todos os campos
        todosCampos.forEach(tf -> tf.setEditable(editavel));

        // 2. Atualiza o texto e estilo do botão
        if (editavel) {
            btnEditar.setText("Salvar");
            btnEditar.getStyleClass().add("btn-salvar");
            iconCamera.setVisible(true);
            paneFoto.setCursor(Cursor.HAND);
            circleFoto.setOpacity(0.7);
        } else {
            btnEditar.setText("Editar");
            btnEditar.getStyleClass().removeAll("btn-salvar", "button-primary");
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
            // Se negocio não tiver nenhum endereço (null) então é criado um novo
            if (negocio.getEndereco() == null) { negocio.setEndereco(new Endereco()); }

            txtCep.setText(negocio.getEndereco().getCep());
            txtRua.setText(negocio.getEndereco().getRua());
            txtNumero.setText(negocio.getEndereco().getNumero());
            txtBairro.setText(negocio.getEndereco().getBairro());
            txtCidade.setText(negocio.getEndereco().getCidade());
            txtEstado.setText(negocio.getEndereco().getEstado());

            // Carrega hora formatada
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            txtInicio.setText(negocio.getInicioExpediente().format(formatter));
            txtFim.setText(negocio.getFimExpediente().format(formatter));

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

    private boolean salvarAlteracoes() {
        try {
            // 1. Valida os dados da UI e lança uma exceção caso tenha dados invalidos
            validarCampos();

            // 2. Pega o objeto para atualizar
            Negocio negocio = (Negocio) buscarUsuarioLogado();

            // 3. Mapeia os campos da tela para o objeto
            negocio.setNome(txtNomeNegocio.getText());
            negocio.setEmail(txtEmail.getText());
            negocio.setCnpj(txtCnpj.getText());
            negocio.setRazaoSocial(txtRazaoSocial.getText());
            negocio.setCapacidadeAtendimentoSimultaneo(Integer.parseInt(txtCapacidade.getText()));


            // Converte e adiciona o horario no objeto
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            negocio.setInicioExpediente(LocalTime.parse(txtInicio.getText(), formatter));
            negocio.setFimExpediente(LocalTime.parse(txtFim.getText(), formatter));
            
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

            // Se salvar no banco, então salva localmente
            this.usuarioLogado = negocio;
            
            AvisoUtils.exibirAvisoSucesso(containerAviso, "Usuario atualizado com sucesso!");
            return true;

        } catch (IllegalArgumentException e) {
            AvisoUtils.exibirAvisoAlerta(containerAviso, e.getMessage());
          
        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(containerAviso, e.getMessage());
            
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            limparAvisos(10);
        }

        return false;
    }
        
    private void validarCampos() throws IllegalArgumentException {
        // Valida o nome
        if (txtNomeNegocio.getText().isBlank()) throw new IllegalArgumentException("O nome do negócio é obrigatório.");

        // Valida o CNPJ
        if (!txtCnpj.getText().isBlank()) {
            String apenasNumerosCnpj = txtCnpj.getText().replaceAll("[.\\-/]", "");

            if (!apenasNumerosCnpj.matches("\\d+")) { 
                throw new IllegalArgumentException("O CNPJ deve conter apenas números (letras não são permitidas).");
            }
        
            if (apenasNumerosCnpj.length() != 14) {
                throw new IllegalArgumentException("O CNPJ deve ter exatamente 14 números.");
            }
        }
        
        // Valida o email
        if (txtEmail.getText().isBlank() || !txtEmail.getText().contains("@")) {
            throw new IllegalArgumentException("E-mail inválido ou vazio.");
        }

        // Valida o horario
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime horaInicio = LocalTime.parse(txtInicio.getText(), formatter);
            LocalTime horaFim = LocalTime.parse(txtFim.getText(), formatter);

            // Valida se o fim é depois do início
            if (!horaFim.isAfter(horaInicio)) {
                throw new IllegalArgumentException("O horário de fechamento deve ser após o horário de abertura.");
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Horário inválido! Use o formato 00:00 (ex: 08:30)");
        }

        // Valida a capacidade (inteiro)
        String capText = txtCapacidade.getText();
        if (capText == null || capText.isBlank()) {
            throw new IllegalArgumentException("A capacidade de atendimento deve ser informada.");
        }
        
        try {
            Integer n = Integer.parseInt(txtCapacidade.getText());

            if (n <= 0) {
                throw new IllegalArgumentException("A capacidade deve ser um número maior que 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A capacidade deve ser um número inteiro.");
        }
    }

    private void limparAvisos(int tempo){
        AvisoUtils.limparCampoAviso(containerAviso, tempo);
    }

    private Usuario buscarUsuarioLogado(){
        if (usuarioLogado == null){
            long id = Sessao.getInstance().getUsuarioLogado().getId();

            this.usuarioLogado = usuarioService.buscarUsuarioPorId(id);
        }

        return usuarioLogado;
    }
}
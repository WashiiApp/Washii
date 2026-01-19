package br.com.washii.presentation.screens.agendamentos;

import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.entities.Veiculo;
import br.com.washii.domain.enums.CategoriaVeiculo;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.domain.entities.Cliente;
import br.com.washii.service.AgendamentoService;
import br.com.washii.service.UsuarioService;
import br.com.washii.presentation.utils.AvisoUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import br.com.washii.domain.entities.Agendamento;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.presentation.screens.home.HomeClienteController;

public class ClienteAgendamentoController extends BaseController {

    @FXML private Button btnCancelar;
    @FXML private Button btnConfirmar;
    @FXML private Button btnAdicionar; 
    @FXML private Button btnRemover;   
    @FXML private ComboBox<CategoriaVeiculo> cmbModeloCarro;
    @FXML private ComboBox<Servico> cmbTipoServico;
    @FXML private DatePicker dateData;
    @FXML private FlowPane fpHorarios;
    @FXML private Label lblEndereco;
    @FXML private Label lblNomeLavaJato;
    @FXML private Label lblValorTotal; 
    @FXML private TextFlow containerAviso;
    @FXML private TextField txtPlaca;
    @FXML private TableView<Servico> tblServicos;
    @FXML private TableColumn<Servico, String> colServico;
    @FXML private TableColumn<Servico, String> colPreco;
    private final AgendamentoService agendamentoService;
    private LavaJato lavaJato;
    private LocalTime horarioSelecionado;
    private Cliente usuarioLogado;
    private UsuarioService usuarioService;
    
    private final ObservableList<Servico> servicosSelecionados = FXCollections.observableArrayList();
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    
    public ClienteAgendamentoController(AgendamentoService agendamentoService, UsuarioService usuarioService) {
        this.agendamentoService = agendamentoService;
        this.usuarioService = usuarioService;
    }

    @FXML
    public void initialize() {
        configurarCalendario();
        configurarTabelaServicos();
        configurarBotoesAcao();

        
        dateData.valueProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                renderizarHorarios(novo);
            }
        });
    }

    public void setLavaJato(LavaJato lavaJato) {
        this.lavaJato = lavaJato;
        if (lavaJato != null) {
            lblNomeLavaJato.setText(lavaJato.getNome());
            configurarCombosIniciais();
            
            if (dateData.getValue() != null) {
                renderizarHorarios(dateData.getValue());
            }
        }
    }
    private Cliente buscarUsuarioLogado(){
        if (usuarioLogado == null){
            long id = Sessao.getInstance().getUsuarioLogado().getId();

            this.usuarioLogado = (Cliente)usuarioService.buscarUsuarioPorId(id);
        }

        return usuarioLogado;
    }

    // aqui configura a tabela com os servicos e os bagui pra fazer nela

    private void configurarTabelaServicos() {
        tblServicos.setItems(servicosSelecionados);

        colServico.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNome()));

        colPreco.setCellValueFactory(cellData -> {
            Double preco = cellData.getValue().getPrecoBase(); // Assumindo que getPreco retorna BigDecimal
            return new SimpleStringProperty(currencyFormat.format(preco));
        });
        
        atualizarValorTotal();
    }

    private void configurarBotoesAcao() {

    btnAdicionar.setOnAction(e -> adicionarServico());

    btnRemover.setOnAction(e -> removerServico());
    
    btnRemover.disableProperty().bind(tblServicos.getSelectionModel().selectedItemProperty().isNull());
    
    btnConfirmar.setOnAction(e -> onConfirmarAction());

    btnCancelar.setOnAction(e -> {
        try {
            FXMLLoader loader =sceneManager.loadCenterBorderPane("/br/com/washii/view/home/home-cliente.fxml");

            HomeClienteController controller = loader.getController();
            controller.carregarNegocios();
            
        } catch (Exception ex) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Não foi possível voltar para a Home: " + ex.getMessage());
            AvisoUtils.limparCampoAviso(containerAviso, 5);
            ex.printStackTrace();
        }
    });
}

    private void adicionarServico() {
        Servico servicoSelecionado = cmbTipoServico.getValue();

        if (servicoSelecionado == null) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Selecione um serviço no menu antes de adicionar.");
            AvisoUtils.limparCampoAviso(containerAviso, 5);
            return;
        }

        if (servicosSelecionados.contains(servicoSelecionado)) {
            AvisoUtils.exibirAvisoErro(containerAviso, "Este serviço já foi adicionado.");
            AvisoUtils.limparCampoAviso(containerAviso, 5);
            return;
        }

        servicosSelecionados.add(servicoSelecionado);
        atualizarValorTotal();
        
        cmbTipoServico.getSelectionModel().clearSelection();
    }

    private void removerServico() {
        Servico servicoSelecionado = tblServicos.getSelectionModel().getSelectedItem();
        
        if (servicoSelecionado != null) {
            servicosSelecionados.remove(servicoSelecionado);
            atualizarValorTotal();
        }
    }

    private void atualizarValorTotal() {
        Double total = Double.valueOf(0);

        for (Servico s : servicosSelecionados) {
            total = total + s.getPrecoBase();
        }
        
        lblValorTotal.setText(currencyFormat.format(total));
    }

    // Aqui eu vou fazer a logica do calendario e dos horarios

    private void configurarCalendario() {
        dateData.setValue(LocalDate.now());
        dateData.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    public void renderizarHorarios(LocalDate data) {
        if (lavaJato == null) return;

        fpHorarios.getChildren().clear();
        horarioSelecionado = null;

        List<LocalTime> horarios = agendamentoService.listarHorariosDisponiveis(data, lavaJato);

        if (horarios == null || horarios.isEmpty()) {
            Label lblAviso = new Label("Nenhum horário livre.");
            lblAviso.setStyle("-fx-text-fill: #999; -fx-font-size: 13px;");
            fpHorarios.getChildren().add(lblAviso);
            return;
        }

        for (LocalTime hora : horarios) {
            Button btnHora = new Button(hora.format(formatter));
            btnHora.setPrefWidth(80);
            btnHora.getStyleClass().add("button-horario-chip");
            
            String estiloPadrao = "-fx-background-color: #f0f0f0; -fx-background-radius: 20; -fx-cursor: hand;";
            String estiloSelecionado = "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 20;";
            
            btnHora.setStyle(estiloPadrao);

            btnHora.setOnAction(e -> {
                fpHorarios.getChildren().forEach(n -> {
                    if (n instanceof Button) n.setStyle(estiloPadrao);
                });
                btnHora.setStyle(estiloSelecionado);
                this.horarioSelecionado = hora;
            });

            fpHorarios.getChildren().add(btnHora);
        }
    }

    private void configurarCombosIniciais() {
        cmbModeloCarro.setItems(FXCollections.observableArrayList(CategoriaVeiculo.values()));
        cmbModeloCarro.setConverter(new StringConverter<>() {
            @Override public String toString(CategoriaVeiculo v) { return v == null ? "Selecione..." : v.name(); }
            @Override public CategoriaVeiculo fromString(String s) { return null; }
        });

        if (lavaJato.getServicosOferecidos() != null) {
            cmbTipoServico.setItems(FXCollections.observableArrayList(lavaJato.getServicosOferecidos()));
            cmbTipoServico.setConverter(new StringConverter<>() {
                @Override public String toString(Servico s) { return s == null ? "Selecione o serviço..." : s.getNome() + " (" + currencyFormat.format(s.getPrecoBase()) + ")"; }
                @Override public Servico fromString(String s) { return null; }
            });
        }
    }
    private void limparCampos() {
    
    txtPlaca.clear();
    cmbModeloCarro.getSelectionModel().clearSelection();
    cmbTipoServico.getSelectionModel().clearSelection();
    
    servicosSelecionados.clear();
    atualizarValorTotal();
    
    horarioSelecionado = null;
    
    if (dateData.getValue() != null) {
        renderizarHorarios(dateData.getValue());
    }
}

    private void onConfirmarAction() {
    if (horarioSelecionado == null || dateData.getValue() == null || servicosSelecionados.isEmpty()) {
        AvisoUtils.exibirAvisoErro(containerAviso, "Preencha a data, horário e adicione pelo menos um serviço.");
        AvisoUtils.limparCampoAviso(containerAviso, 5);
        return;
    }
    
    if (cmbModeloCarro.getValue() == null || txtPlaca.getText().trim().isEmpty()) {
        AvisoUtils.exibirAvisoErro(containerAviso, "Preencha o modelo do carro e a placa.");
        AvisoUtils.limparCampoAviso(containerAviso, 5);
        return;
    }
    
    try {
        Agendamento agendamento = new Agendamento();
        Veiculo veiculo = new Veiculo();
        veiculo.setCategoriaVeiculo(cmbModeloCarro.getValue());
        veiculo.setPlaca(txtPlaca.getText().trim());
        
        agendamento.setData(dateData.getValue());
        agendamento.setHora(horarioSelecionado);
        agendamento.setVeiculo(veiculo); // OBS: Certifique-se que o Service salva o veículo antes do agendamento
        agendamento.setNegocio(lavaJato);
        agendamento.setServicos(new ArrayList<>(servicosSelecionados));
        agendamento.setCliente(buscarUsuarioLogado());
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        
        agendamentoService.solicitarAgendamento(agendamento);
        
        AvisoUtils.exibirAvisoSucesso(containerAviso, "Agendamento confirmado com sucesso!");
        AvisoUtils.limparCampoAviso(containerAviso, 5);
        
        limparCampos(); 
        
    } catch (NegocioException e) {
        AvisoUtils.exibirAvisoErro(containerAviso, e.getMessage());
        AvisoUtils.limparCampoAviso(containerAviso, 5);
    } catch (Exception e) {
        
        e.printStackTrace();
        AvisoUtils.exibirAvisoErro(containerAviso, "Ocorreu um erro interno: " + e.getMessage());
        AvisoUtils.limparCampoAviso(containerAviso, 5);
    }
}
    
}
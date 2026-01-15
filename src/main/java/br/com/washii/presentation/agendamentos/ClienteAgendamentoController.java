package br.com.washii.presentation.agendamentos;

import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.enums.CategoriaVeiculo;
import br.com.washii.service.AgendamentoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;

public class ClienteAgendamentoController {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private ComboBox<CategoriaVeiculo> cmbModeloCarro;

    @FXML
    private ComboBox<Servico> cmbTipoServico;

    @FXML
    private DatePicker dateData;

    @FXML
    private FlowPane flowHorarios;

    @FXML
    private Label lblEndereco;

    @FXML
    private Label lblNomeLavaJato;

    @FXML
    private TextField txtPlaca;

    private AgendamentoService agendamentoService;

    private LavaJato lavaJato;

    public ClienteAgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    public void setLavaJato(LavaJato lavaJato){
        this.lavaJato = lavaJato;
    }
    @FXML
    public void initialize() {
        configurarChoiceBox();
    }
    @FXML
    private void configurarChoiceBox(){
        cmbModeloCarro.setItems(FXCollections.observableArrayList(CategoriaVeiculo.values()));
        cmbModeloCarro.setConverter(new StringConverter<CategoriaVeiculo>() {
            @Override
            public String toString(CategoriaVeiculo veiculo) {
                return (veiculo == null) ? "Selecione..." : veiculo.name();
            }

            @Override
            public CategoriaVeiculo fromString(String string) {
                return null; 
            }
        });
        cmbTipoServico.setItems(FXCollections.observableArrayList(lavaJato.getServicosOferecidos()));
        cmbTipoServico.setConverter(new StringConverter<Servico>() {
            @Override
            public String toString(Servico servico) {
                return (servico == null) ? "Selecione..." : servico.getNome();
            }

            @Override
            public Servico fromString(String string) {
                return null; 
            }
        });
    }
}

package br.com.washii.presentation.screens.agendamentos;

import java.io.IOException;
import java.util.List;
import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.presentation.components.cards.AgendamentoCardClienteController;
import br.com.washii.service.AgendamentoMock;
import br.com.washii.service.AgendamentoService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.FlowPane;

public class MeusAgendamentosClienteController {

    @FXML
    private Button btnFiltrar;

    @FXML
    private Button btnLimpar;

    @FXML
    private ComboBox<StatusAgendamento> cmbStatus;

    @FXML
    private DatePicker dtpData;

    @FXML
    private FlowPane flowCards;

    private AgendamentoService agendamentoService;

    public MeusAgendamentosClienteController(AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    @FXML
    void initialize() {
        cmbStatus.setItems(FXCollections.observableArrayList(StatusAgendamento.values()));
        carregarAgendamentos();
    }

    @FXML
    void onFiltrar(ActionEvent event) {
        StatusAgendamento status = cmbStatus.getValue();
        java.time.LocalDate data = dtpData.getValue();

        // Aqui você filtraria sua lista original. 
        // Exemplo simples filtrando o Mock:
        List<Agendamento> filtrados = AgendamentoMock.gerarListaMock().stream()
            .filter(ag -> status == null || ag.getStatus() == status)
            .filter(ag -> data == null || ag.getData().equals(data))
            .toList();

        flowCards.getChildren().clear();
        filtrados.forEach(this::adicionarCardAoFluxo);
    }

    @FXML
    void onLimparFiltro(ActionEvent event) {
        cmbStatus.setValue(null);
        cmbStatus.setPromptText("Filtre por Status");
        dtpData.setValue(null);
        carregarAgendamentos();
    }

    public void carregarAgendamentos(){
        flowCards.getChildren().clear();
        // criar metodo no service para listar todos os agendamentos por usuario.
        List<Agendamento> agendamentos = AgendamentoMock.gerarListaMock();

        for (Agendamento ag : agendamentos) {
            adicionarCardAoFluxo(ag);
        }
    }

    private void adicionarCardAoFluxo(Agendamento ag){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/components/cards/agendamento-card-cliente.fxml"));
            Parent card = loader.load();
            
            AgendamentoCardClienteController controller = loader.getController();
            controller.setService(agendamentoService);
            controller.setDados(ag);
            controller.setOnUpdate(() -> {
                this.carregarAgendamentos();
            });

            flowCards.getChildren().add(card);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


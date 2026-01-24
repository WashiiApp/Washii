package br.com.washii.presentation.screens.agendamentos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.components.cards.AgendamentoCardNegocioController; 
import br.com.washii.service.AgendamentoService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class MeusAgendamentosNegocioController {

    @FXML private ComboBox<StatusAgendamento> cmbStatus;
    @FXML private DatePicker dtpData;
    @FXML private Button btnFiltrar;
    @FXML private Button btnLimpar;
    @FXML private FlowPane flowCards;

    private AgendamentoService agendamentoService;
    private List<Agendamento> listaCompletaCache;

    public MeusAgendamentosNegocioController(AgendamentoService service) {
        this.agendamentoService = service;
    }

    @FXML
    public void initialize() {
        
        cmbStatus.setItems(FXCollections.observableArrayList(StatusAgendamento.values()));
        carregarAgendamentos();
    }

    public void carregarAgendamentos() {
        flowCards.getChildren().clear(); // Limpa a tela antes de adicionar
        
        try {
            Long idNegocio = Sessao.getInstance().getUsuarioLogado().getId();

            // Intervalo de busca amplo para garantir que venha tudo
            LocalDate inicio = LocalDate.now().minusYears(2);
            LocalDate fim = LocalDate.now().plusYears(2);

            // Busca no banco
            this.listaCompletaCache = agendamentoService.listarPorPeriodoENegocio(inicio, fim, idNegocio);

            if (listaCompletaCache == null || listaCompletaCache.isEmpty()) {
                mostrarMensagemVazio("Nenhum agendamento encontrado no banco de dados.");
            } else {
                aplicarFiltros(); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensagemVazio("Erro ao buscar dados: " + e.getMessage());
        }
    }

    private void aplicarFiltros() {
        flowCards.getChildren().clear();

        if (listaCompletaCache == null) return;

        StatusAgendamento statusFiltro = cmbStatus.getValue();
        LocalDate dataFiltro = dtpData.getValue();

        List<Agendamento> filtrados = listaCompletaCache.stream()
            .filter(ag -> {
                if (statusFiltro != null && ag.getStatus() != statusFiltro) return false;
                if (dataFiltro != null && !ag.getData().equals(dataFiltro)) return false;
                
                return true;
            })
            .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            mostrarMensagemVazio("Nenhum agendamento com esses filtros.");
        } else {
            for (Agendamento ag : filtrados) {
                adicionarCardAoFluxo(ag);
            }
        }
    }

    private void adicionarCardAoFluxo(Agendamento ag) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/components/cards/agendamento-card-negocio.fxml"));
            Parent card = loader.load();

            AgendamentoCardNegocioController controller = loader.getController();
            
            controller.setService(this.agendamentoService);
            // Transforma o card para visualização, não podendo execultar comandos de iniciar ou
            // concluir agendamento, apenas cancelar.
            // Importante ser definido antes do metodo setDados.
            controller.setCardVisualizacao(true);
            controller.setDados(ag); 

            controller.setOnUpdate(() -> {
                this.carregarAgendamentos(); 
            });

            flowCards.getChildren().add(card);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensagemVazio(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-font-size: 16px; -fx-text-fill: #999; -fx-padding: 20;");
        flowCards.getChildren().add(lbl);
    }
    
    @FXML
    void onFiltrar(ActionEvent event) {
        aplicarFiltros();
    }

    @FXML
    void onLimparFiltro(ActionEvent event) {
        cmbStatus.setValue(null);
        dtpData.setValue(null);
        aplicarFiltros(); 
    }
}
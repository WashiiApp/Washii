package br.com.washii.presentation.screens.agendamentos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.components.cards.AgendamentoCardController; // Importe o controller certo!
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

    // --- Construtor ---
    public MeusAgendamentosNegocioController(AgendamentoService service) {
        // Garante que o service exista antes de tudo
        this.agendamentoService = service;
    }

    @FXML
    public void initialize() {
        System.out.println("--- INICIANDO TELA DE MEUS AGENDAMENTOS ---");
        
        // 1. Configura o ComboBox
        cmbStatus.setItems(FXCollections.observableArrayList(StatusAgendamento.values()));
        
        // 2. IMPORTANTE: Chama o carregamento assim que a tela abre
        carregarAgendamentos();
    }

    public void carregarAgendamentos() {
        flowCards.getChildren().clear(); // Limpa a tela antes de adicionar
        
        try {
            Long idNegocio = Sessao.getInstance().getUsuarioLogado().getId();
            System.out.println("Carregando agendamentos para o Negócio ID: " + idNegocio);

            // Intervalo de busca amplo para garantir que venha tudo
            LocalDate inicio = LocalDate.now().minusYears(2);
            LocalDate fim = LocalDate.now().plusYears(2);

            // Busca no banco
            this.listaCompletaCache = agendamentoService.listarPorPeriodoENegocio(inicio, fim, idNegocio);
            System.out.println("Total de agendamentos carregados: " + listaCompletaCache);

            if (listaCompletaCache == null || listaCompletaCache.isEmpty()) {
                System.out.println("O Service retornou 0 agendamentos (Lista vazia ou nula).");
                mostrarMensagemVazio("Nenhum agendamento encontrado no banco de dados.");
            } else {
                System.out.println("Total encontrados no banco: " + listaCompletaCache.size());
                aplicarFiltros(); // Exibe na tela
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

        System.out.println("Filtrando... Status: " + statusFiltro + " | Data: " + dataFiltro);

        List<Agendamento> filtrados = listaCompletaCache.stream()
            .filter(ag -> {
                // Filtro de Status
                if (statusFiltro != null && ag.getStatus() != statusFiltro) return false;
                
                // Filtro de Data
                if (dataFiltro != null && !ag.getData().equals(dataFiltro)) return false;
                
                return true;
            })
            .collect(Collectors.toList());

        System.out.println("Total a exibir após filtro: " + filtrados.size());

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
            // Certifique-se que o caminho do FXML está 100% correto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/components/cards/agendamento-card.fxml"));
            Parent card = loader.load();

            AgendamentoCardController controller = loader.getController();
            
            controller.setService(this.agendamentoService);
            controller.setDados(ag); // Isso aqui preenche o visual

            controller.setOnUpdate(() -> {
                System.out.println("Card atualizado. Recarregando lista...");
                this.carregarAgendamentos(); 
            });

            flowCards.getChildren().add(card);

        } catch (IOException e) {
            System.err.println("ERRO AO CARREGAR CARD:");
            e.printStackTrace();
        }
    }

    private void mostrarMensagemVazio(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-font-size: 16px; -fx-text-fill: #999; -fx-padding: 20;");
        flowCards.getChildren().add(lbl);
    }
    
    // Ações dos botões da tela principal
    @FXML
    void onFiltrar(ActionEvent event) {
        aplicarFiltros();
    }

    @FXML
    void onLimparFiltro(ActionEvent event) {
        cmbStatus.setValue(null);
        dtpData.setValue(null);
        aplicarFiltros(); // Re-exibe tudo sem filtro
    }
}
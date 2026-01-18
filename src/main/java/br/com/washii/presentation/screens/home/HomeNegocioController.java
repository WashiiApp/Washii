package br.com.washii.presentation.screens.home;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.components.cards.AgendamentoCardController;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.AgendamentoMock;
import br.com.washii.service.AgendamentoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class HomeNegocioController extends BaseController {

    private AgendamentoService agendamentoService;

    @FXML private Label countAgendados, countAndamento, countFinalizado, lblDataAtual;
    @FXML private Label lblFaturamentoTotal, lblQtdAgendados, lblQtdAndamento, lblQtdFinalizados;
    @FXML private FlowPane flowAgendados, flowAndamento, flowFinalizados;

    public HomeNegocioController(AgendamentoService ag) {
        this.agendamentoService = ag;
    }

    @FXML
    public void initialize() {
        configurarDataAtual();
        carregarDados(); 
    }

    private void configurarDataAtual() {
        Locale locale = Locale.of("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", locale);
        lblDataAtual.setText(LocalDate.now().format(formatter));
    }

    public void carregarDados() {
        Long userId = Sessao.getInstance().getUsuarioLogado().getId();
        LocalDate dataHoje = LocalDate.now();
        List<Agendamento> agendamentos = agendamentoService.listarPorPeriodoENegocio(dataHoje, dataHoje, userId);
        
        // Limpar painéis para evitar duplicidade
        flowAgendados.getChildren().clear();
        flowAndamento.getChildren().clear();
        flowFinalizados.getChildren().clear();

        int qtdAgendados = 0;
        int qtdPendentes = 0;
        int qtdAndamento = 0;
        int qtdFinalizado = 0;
        double faturamento = 0;

        for (Agendamento ag : agendamentos) {
            adicionarCardAoFluxo(ag);

            // Lógica dos contadores e faturamento
            if (ag.getStatus() == StatusAgendamento.CONCLUIDO) {
                qtdFinalizado ++;
                faturamento += ag.calcularValorTotal();

                qtdAgendados ++;
            } else if (ag.getStatus() == StatusAgendamento.AGENDADO) {
                qtdPendentes++;

                qtdAgendados ++;
            } else if (ag.getStatus() == StatusAgendamento.EM_ANDAMENTO) { 
                qtdAndamento++;

                qtdAgendados ++;
            } 
        }

        verificarFlowsVazios();

        // Atualizar Labels de resumo
        lblQtdAgendados.setText(String.valueOf(qtdAgendados));
        lblQtdAndamento.setText(String.valueOf(qtdAndamento));
        lblQtdFinalizados.setText(String.valueOf(qtdFinalizado));
        lblFaturamentoTotal.setText(String.format("R$ %.2f", faturamento));
        
        countAgendados.setText(String.valueOf(qtdPendentes));
        countAndamento.setText(String.valueOf(qtdAndamento));
        countFinalizado.setText(String.valueOf(qtdFinalizado));
    }

    private void adicionarCardAoFluxo(Agendamento ag) {
        try {
            // Caminho para o FXML do Card que criamos antes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/components/cards/agendamento-card.fxml"));
            Parent card = loader.load();

            // Pegar o controller do card e passar os dados e o service
            AgendamentoCardController cardController = loader.getController();
            cardController.setDados(ag);
            cardController.setService(agendamentoService);
            cardController.setOnUpdate(() -> {
                this.carregarDados();
            });

            // Adicionar ao FlowPane correspondente ao status
            switch (ag.getStatus()) {
                case AGENDADO -> flowAgendados.getChildren().add(card);
                case EM_ANDAMENTO -> flowAndamento.getChildren().add(card);
                case CONCLUIDO -> flowFinalizados.getChildren().add(card);
                default -> { }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verificarFlowsVazios() {
        verificarEAdicionarMensagemVazia(flowAndamento, "Nenhum serviço em execução no momento.");
        verificarEAdicionarMensagemVazia(flowAgendados, "Não há novos agendamentos para hoje.");
        verificarEAdicionarMensagemVazia(flowFinalizados, "Nenhum agendamento foi finalizado ainda.");
    }

    private void verificarEAdicionarMensagemVazia(FlowPane flow, String mensagem) {
        if (flow.getChildren().isEmpty()) {
            Label lblVazio = new Label(mensagem);
            
            // Estilização via código ou CSS
            lblVazio.getStyleClass().add("label-empty-state");
            
            // Centraliza a mensagem no FlowPane
            lblVazio.setMinWidth(flow.getWidth() > 0 ? flow.getWidth() - 40 : 200); 
            lblVazio.setAlignment(javafx.geometry.Pos.CENTER);
            
            flow.getChildren().add(lblVazio);
        }
    }
}
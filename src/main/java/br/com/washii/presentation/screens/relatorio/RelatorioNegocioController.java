package br.com.washii.presentation.screens.relatorio;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.AgendamentoMock;
import br.com.washii.service.AgendamentoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

public class RelatorioNegocioController {

    private AgendamentoService agendamentoService;
    
    // Objeto de formatação para Real Brasileiro (R$)
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    @FXML private LineChart<?, ?> chartLinha;
    @FXML private PieChart chartPizza;
    @FXML private TextFlow containerAvisos;
    @FXML private DatePicker dpFim;
    @FXML private DatePicker dpInicio;
    @FXML private Label lblAgendamentos;
    @FXML private Label lblFaturamento;
    @FXML private Label lblTicketMedio;

    public RelatorioNegocioController(AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    @FXML
    void initialize(){
        dpInicio.setValue(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
        dpFim.setValue(LocalDate.now());

        // Para carregar dados reais, você usaria o idNegocio abaixo:
        // Long idNegocio = Sessao.getInstance().getUsuarioLogado().getId();
        
        List<Agendamento> dados = AgendamentoMock.gerarListaMock();
        carregarDados(dados);
    }

    @FXML
    void onFiltrar(ActionEvent event) {
        // Se precisar buscar dados novos baseados nas datas dos DatePickers:
        // List<Agendamento> dados = agendamentoService.listarPorPeriodoENegocio(dpInicio.getValue(), dpFim.getValue(), idNegocio);
        
        List<Agendamento> dados = AgendamentoMock.gerarListaMock();
        carregarDados(dados);
    }

    public void carregarDados(List<Agendamento> dados){
        AvisoUtils.limparCampoAviso(containerAvisos, 0);
        if (dados.isEmpty()) {
            AvisoUtils.exibirAvisoAlerta(containerAvisos, "Nenhum registro encontrado para o período selecionado");

            return;
        }

        double faturamentoTotal = calcularFaturamento(dados);
        int qtdTotalAgendamentos = dados.size();
        double ticketMedio = calcularTicketMedio(faturamentoTotal, qtdTotalAgendamentos);

        // APLICAÇÃO DA FORMATAÇÃO
        lblFaturamento.setText(currencyFormat.format(faturamentoTotal));
        lblAgendamentos.setText(String.valueOf(qtdTotalAgendamentos));
        lblTicketMedio.setText(currencyFormat.format(ticketMedio));
    }

    private double calcularFaturamento(List<Agendamento> dados) {
        if (dados == null) return 0.0;
        return dados.stream()
                .mapToDouble(ag -> ag.calcularValorTotal())
                .sum();
    }

    private double calcularTicketMedio(double valorTotal, int qtdTotal) {
        // Proteção contra divisão por zero caso não haja agendamentos
        return (qtdTotal == 0) ? 0.0 : valorTotal / qtdTotal;
    }
}
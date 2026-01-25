package br.com.washii.presentation.screens.relatorio;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.AgendamentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

public class RelatorioNegocioController {

    private AgendamentoService agendamentoService;
    
    // Objeto de formatação para Real Brasileiro (R$)
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    @FXML private LineChart<String, Number> chartLinha;
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

        // Regra: Bloquear qualquer dia que venha DEPOIS de hoje
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;"); // Deixa cinza claro
                }
            }
        };

        // Aplica a regra nos dois campos
        dpInicio.setDayCellFactory(dayCellFactory);
        dpFim.setDayCellFactory(dayCellFactory);

        // Para carregar dados reais, você usaria o idNegocio abaixo:
        Long idNegocio = Sessao.getInstance().getUsuarioLogado().getId();
        
        List<Agendamento> dados = agendamentoService.listarPorPeriodoENegocio(dpInicio.getValue(), dpFim.getValue(), idNegocio);
        carregarDados(dados);
    }

    @FXML
    void onFiltrar(ActionEvent event) {
        // Se precisar buscar dados novos baseados nas datas dos DatePickers:
        
        Long idNegocio = Sessao.getInstance().getUsuarioLogado().getId();

        List<Agendamento> dados = agendamentoService.listarPorPeriodoENegocio(dpInicio.getValue(), dpFim.getValue(), idNegocio);

        carregarDados(dados);
    }

    public void carregarDados(List<Agendamento> dados){
        AvisoUtils.limparCampoAviso(containerAvisos, 10);

        if (!validarData(dpInicio, dpFim)){
            return;
        }

        if (dados.isEmpty()) {
            AvisoUtils.exibirAvisoErro(containerAvisos, "Nenhum registro encontrado para o período selecionado");

            return;
        }

        // Filtra os agendamentos para apenas aqueles que já forma concluídos
        List<Agendamento> dadosFiltrados = dados.stream().filter(ag -> ag.getStatus() == StatusAgendamento.CONCLUIDO).toList();

        double faturamentoTotal = calcularFaturamento(dadosFiltrados);
        int qtdTotalAgendamentos = dadosFiltrados.size();
        double ticketMedio = calcularTicketMedio(faturamentoTotal, qtdTotalAgendamentos);

        // APLICAÇÃO DA FORMATAÇÃO
        lblFaturamento.setText(currencyFormat.format(faturamentoTotal));
        lblAgendamentos.setText(String.valueOf(qtdTotalAgendamentos));
        lblTicketMedio.setText(currencyFormat.format(ticketMedio));

        // Carregar graficos
        atualizarGraficoPizza(dadosFiltrados);
        atualizarGraficoLinha(dadosFiltrados);
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

    private void atualizarGraficoPizza(List<Agendamento> dados) {
        // 1. Mapeamos cada agendamento para seus serviços e agrupamos por nome do serviço
        Map<String, Double> faturamentoPorServico = dados.stream()
            .flatMap(ag -> ag.getServicos().stream()) // "Abre" a lista de serviços de cada agendamento
            .collect(Collectors.groupingBy(
                servico -> servico.getNome(), // Agrupa pelo nome do serviço individual
                Collectors.summingDouble(servico -> servico.getPrecoBase()) // Soma o preço de cada um
            ));

        // 2. Prepara a lista de dados para o JavaFX
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        
        faturamentoPorServico.forEach((nomeServico, valorTotal) -> {
            String legenda = nomeServico + " (" + currencyFormat.format(valorTotal) + ")";
            pieData.add(new PieChart.Data(legenda, valorTotal));
        });

        // 3. Alimenta o gráfico
        chartPizza.setData(pieData);
    }

    private void atualizarGraficoLinha(List<Agendamento> dados) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        Map<LocalDate, Long> contagemPorData = dados.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Agendamento::getData, 
                java.util.stream.Collectors.counting()
            ));

        // Usando Generics corretos para evitar warnings da IDE
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Volume de Agendamentos");

        contagemPorData.keySet().stream()
            .sorted()
            .forEach(data -> {
                series.getData().add(new XYChart.Data<>(data.format(formatter), contagemPorData.get(data)));
            });

        chartLinha.getData().clear();
        // Aqui fazemos um cast seguro ou garantimos que o chartLinha aceite a série
        chartLinha.getData().add(series);

        if (chartLinha.getYAxis() instanceof NumberAxis yAxis) {
            yAxis.setTickUnit(1.0);
            yAxis.setMinorTickVisible(false);
            yAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
                @Override
                public String toString(Number object) {
                    return (object.doubleValue() % 1 == 0) ? String.valueOf(object.intValue()) : "";
                }
                @Override
                public Number fromString(String string) {
                    return Double.valueOf(string);
                }
            });
        }
    }

    private boolean validarData(DatePicker dpInicio, DatePicker dpFim) {

        // Extrai os valores locais para facilitar a leitura
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();

        // Verifica se o usuário deixou algum campo vazio
        if (inicio == null || fim == null) {
            AvisoUtils.exibirAvisoErro(containerAvisos, "Por favor, selecione as datas de início e fim.");
            return false;
        }

        // Verifica se a data de início é maior que a data de fim
        if (inicio.isAfter(fim)) {
            AvisoUtils.exibirAvisoErro(containerAvisos, "A data inicial não pode ser posterior à data final.");
            return false;
        }

        // Verifica se selecionaram uma data futura
        if (fim.isAfter(LocalDate.now())) {
            AvisoUtils.exibirAvisoAlerta(containerAvisos, "Não é possível gerar relatórios de datas futuras.");
            return false;
        }

        return true;
    }
}
package br.com.washii.presentation.relatorio;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

public class RelatorioNegocioController {

    @FXML
    private LineChart<?, ?> chartLinha;

    @FXML
    private PieChart chartPizza;

    @FXML
    private TextFlow containerAvisos;

    @FXML
    private DatePicker dpFim;

    @FXML
    private DatePicker dpInicio;

    @FXML
    private Label lblAgendamentos;

    @FXML
    private Label lblFaturamento;

    @FXML
    private Label lblTicketMedio;

}

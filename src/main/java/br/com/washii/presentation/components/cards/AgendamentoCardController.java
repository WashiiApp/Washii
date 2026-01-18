package br.com.washii.presentation.components.cards;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.washii.domain.entities.Agendamento; // Ajuste o pacote conforme seu projeto
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.service.AgendamentoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AgendamentoCardController {

    private AgendamentoService agendamentoService;

    @FXML private Button btnCancelar;
    @FXML private Button btnIniciar;
    @FXML private Label lblDataHora;
    @FXML private Label lblModeloVeiculo;
    @FXML private Label lblNomeCliente;
    @FXML private Label lblNumeroAgendamento;
    @FXML private Label lblPlacaVeiculo;
    @FXML private Label lblStatus;
    @FXML private VBox vboxServicos;
    @FXML private VBox cardRoot;
    private Agendamento agendamento;

    private boolean emAndamento = false;

    private Runnable onUpdate;

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setService(AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    /**
     * Preenche o card com os dados do agendamento e aplica a estilização dinâmica.
     */
    public void setDados(Agendamento ag) {
        this.agendamento = ag;

        // 1. Textos Básicos
        lblNumeroAgendamento.setText("AGENDAMENTO #" + ag.getId());
        lblNomeCliente.setText(ag.getCliente().getNome());
        lblPlacaVeiculo.setText(ag.getVeiculo().getPlaca());
        lblModeloVeiculo.setText(ag.getVeiculo().getCategoriaVeiculo().toString());
        
        // Exemplo de formatação: "Segunda, 14:30" ou "12/01, 14:30"
        lblDataHora.setText(formatarDataExibicao(ag.getData(),ag.getHora())); 

        // 2. Status e Estilização CSS
        configurarStatus(ag.getStatus());

        // 3. Lista de Serviços (Dinâmica)
        vboxServicos.getChildren().clear();
        ag.getServicos().forEach(servico -> {
            // Container para o serviço (Nome + Valor)
            javafx.scene.layout.HBox hbServico = new javafx.scene.layout.HBox();
            hbServico.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            // Nome do serviço
            Label lblNome = new Label("• " + servico.getNome());
            lblNome.getStyleClass().add("label-servico");
            
            // Espaçador para empurrar o valor para a direita
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            
            // Valor formatado
            String valorFormatado = String.format("R$ %.2f", servico.getPrecoBase());
            Label lblValor = new Label(valorFormatado);
            lblValor.getStyleClass().add("label-servico-preco"); // Classe nova para o preço
            
            hbServico.getChildren().addAll(lblNome, spacer, lblValor);
            vboxServicos.getChildren().add(hbServico);
        });

        // 4. Controle de Visibilidade dos Botões
        boolean podeInteragir = (ag.getStatus() == StatusAgendamento.EM_ANDAMENTO || 
                                 ag.getStatus() == StatusAgendamento.AGENDADO);

        btnIniciar.setVisible(podeInteragir);
        btnIniciar.setManaged(podeInteragir);
        btnCancelar.setVisible(podeInteragir);
        btnCancelar.setManaged(podeInteragir);

        if (ag.getStatus() == StatusAgendamento.EM_ANDAMENTO) {
            btnIniciar.setText("CONCLUIR");
            btnCancelar.setVisible(false);
            btnCancelar.setManaged(false);
            emAndamento = true;
        } else {
            btnIniciar.setText("INICIAR");
            emAndamento = false;
        }
    }

    private void configurarStatus(StatusAgendamento status) {
        // Define o texto formatado (Ex: NAO_COMPARECEU -> Não Compareceu)
        lblStatus.setText(status.toString().replace("_", " ")); 

        // Limpa classes de cores antigas
        lblStatus.getStyleClass().removeAll(
            "status-em-andamento", "status-cancelado", "status-agendado", 
            "status-concluido", "status-nao-compareceu"
        );

        cardRoot.getStyleClass().removeAll(
            "agendamento-card-em-andamento", "agendamento-card-cancelado", "agendamento-card-agendado", "agendamento-card-concluido", "agendamento-card-nao-compareceu"
        );  

        // Adiciona a classe base (se não houver no FXML) e a específica das variáveis CSS
        if (!lblStatus.getStyleClass().contains("badge-status")) {
            lblStatus.getStyleClass().add("badge-status");
        }
        
        // Mapeia o Enum para a classe CSS: status-nome-do-enum
        String classeCssCard = "agendamento-card-" + status.name().toLowerCase().replace("_", "-");
        String classeCssStatus = "status-" + status.name().toLowerCase().replace("_", "-");

        cardRoot.getStyleClass().add(classeCssCard);
        lblStatus.getStyleClass().add(classeCssStatus);
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Deseja realmente CANCELAR o Agendamento #" + agendamento.getId() + "?");
        alert.setContentText("Esta ação não poderá ser desfeita.");

        if (alert.showAndWait().get() == ButtonType.OK ) {
            agendamentoService.cancelarAgendamento(agendamento);
            if (onUpdate != null) {
                onUpdate.run();
            }
        }
    }

    @FXML
    void handleIniciar(ActionEvent event) {
        if(emAndamento) {
            
            agendamentoService.atualizarStatus(StatusAgendamento.CONCLUIDO, agendamento);
        
        } else {
            agendamentoService.atualizarStatus(StatusAgendamento.EM_ANDAMENTO, agendamento);

        }

        if (onUpdate != null) {
            onUpdate.run();
        }
    }

    private String formatarDataExibicao(LocalDate data, LocalTime hora) {
        LocalDate hoje = LocalDate.now();
        LocalDate amanha = hoje.plusDays(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        String prefixo;
        
        if (data.isEqual(hoje)) {
            prefixo = "Hoje";
        } else if (data.isEqual(amanha)) {
            prefixo = "Amanhã";
        } else {
            // Para outras datas: "15 de jan."
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM", Locale.of("pt", "BR"));
            prefixo = data.format(dateFormatter);
        }

        String horaFormatada = hora.format(timeFormatter);
        return (prefixo + ", " + horaFormatada);
    }
}
package br.com.washii.presentation.agendamentos;

import br.com.washii.domain.entities.Agendamento; // Ajuste o pacote conforme seu projeto
import br.com.washii.domain.enums.StatusAgendamento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AgendamentoCardController {

    @FXML private Button btnCancelar;
    @FXML private Button btnIniciar;
    @FXML private Label lblDataHora;
    @FXML private Label lblModeloVeiculo;
    @FXML private Label lblNomeCliente;
    @FXML private Label lblNumeroAgendamento;
    @FXML private Label lblPlacaVeiculo;
    @FXML private Label lblStatus;
    @FXML private VBox vboxServicos;

    private Agendamento agendamento;

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
        lblDataHora.setText(ag.getData() + ", " + ag.getHora()); 

        // 2. Status e Estilização CSS
        configurarStatus(ag.getStatus());

        // 3. Lista de Serviços (Dinâmica)
        vboxServicos.getChildren().clear();
        ag.getServicos().forEach(servico -> {
            Label lblServico = new Label("• " + servico.getNome());
            lblServico.getStyleClass().add("label-servico"); // Adicione no seu CSS
            vboxServicos.getChildren().add(lblServico);
        });

        // 4. Controle de Visibilidade dos Botões
        boolean podeInteragir = (ag.getStatus() == StatusAgendamento.PENDENTE || 
                                 ag.getStatus() == StatusAgendamento.AGENDADO);
        
        btnIniciar.setVisible(podeInteragir);
        btnIniciar.setManaged(podeInteragir);
        btnCancelar.setVisible(podeInteragir);
        btnCancelar.setManaged(podeInteragir);
    }

    private void configurarStatus(StatusAgendamento status) {
        // Define o texto formatado (Ex: NAO_COMPARECEU -> Não Compareceu)
        lblStatus.setText(status.toString().replace("_", " ")); 

        // Limpa classes de cores antigas
        lblStatus.getStyleClass().removeAll(
            "status-pendente", "status-cancelado", "status-agendado", 
            "status-concluido", "status-nao-compareceu"
        );

        // Adiciona a classe base (se não houver no FXML) e a específica das variáveis CSS
        if (!lblStatus.getStyleClass().contains("badge-status")) {
            lblStatus.getStyleClass().add("badge-status");
        }
        
        // Mapeia o Enum para a classe CSS: status-nome-do-enum
        String classeCss = "status-" + status.name().toLowerCase().replace("_", "-");
        lblStatus.getStyleClass().add(classeCss);
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        System.out.println("Solicitando cancelamento do agendamento: " + agendamento.getId());
        // Lógica para abrir modal de confirmação ou atualizar via Service
    }

    @FXML
    void handleIniciar(ActionEvent event) {
        System.out.println("Iniciando lavagem: " + agendamento.getVeiculo().getPlaca());
        // Lógica para mudar status para EM_ANDAMENTO (se você tiver esse status)
    }
}
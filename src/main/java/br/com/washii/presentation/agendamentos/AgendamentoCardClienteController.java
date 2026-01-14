package br.com.washii.presentation.agendamentos;

import java.time.format.DateTimeFormatter;
import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.service.AgendamentoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AgendamentoCardClienteController {

    @FXML private Button btnCancelar;

    @FXML private VBox cardRoot;

    @FXML private VBox containerServicos;

    @FXML private Label lblData, lblEndereco, lblHora,
                  lblIdAgendamento, lblNomeNegocio,
                  lblStatus, lblTotal;

    private AgendamentoService agendamentoService;

    private Runnable onUpdate;

    private Agendamento agendamento;

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setService(AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    @FXML
    void onCancelarAgendamento(ActionEvent event) {
        try {
            agendamentoService.cancelarAgendamento(agendamento);

            onUpdate.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDados(Agendamento ag){
        this.agendamento = ag;

        configurarStatus(ag.getStatus());

        lblIdAgendamento.setText("id #" + ag.getId().toString());
        lblNomeNegocio.setText(ag.getNegocio().getNome());

        // Endereço
        Endereco end = ag.getNegocio().getEndereco();
        lblEndereco.setText(end.getRua() + ", " + end.getNumero() + "\n" + end.getCidade() + "\n" + end.getEstado());

        // Data e Hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblData.setText(ag.getData().format(dateFormatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        lblHora.setText(ag.getHora().format(timeFormatter));

        // Serviços e valores
        for (Servico ser: ag.getServicos()){
            adicionarServicoAoContainer(ser.getNome(), ser.getPrecoBase());
        }

        lblTotal.setText(String.format("R$ %.2f", ag.calcularValorTotal()));
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

        // configura o botão cancelar
        if (status != StatusAgendamento.AGENDADO){
            btnCancelar.setVisible(false);
            btnCancelar.setManaged(false);
        } else {
            btnCancelar.setVisible(true);
            btnCancelar.setManaged(true);
        }
    }

    private void adicionarServicoAoContainer(String nome, double valor) {
        // 1. Cria o HBox da linha
        HBox linha = new HBox();
        
        Label lblNome = new Label(nome);
        
        // 3. Cria a Region para empurrar o valor para a direita
        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS); // Isso faz o efeito do "ALWAYS" no FXML
        
        // 4. Cria o Label do Valor formatado
        Label lblValor = new Label(String.format("R$ %.2f", valor));
        
        // 5. Adiciona os filhos ao HBox
        linha.getChildren().addAll(lblNome, espacador, lblValor);
        
        // 6. Adiciona a linha ao seu VBox (containerServicos)
        containerServicos.getChildren().add(linha);
    }
}

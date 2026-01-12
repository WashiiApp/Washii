package br.com.washii.presentation.agendamentos;


import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Servico;
import br.com.washii.presentation.core.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NegocioCardController extends BaseController{

    private LavaJato lavaJato;

    @FXML
    private Button btnAgendar;

    @FXML
    private VBox cardRoot;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lblAvaliacao;

    @FXML
    private Label lblEndereco;

    @FXML
    private Label lblNome;

    @FXML
    private VBox vboxServicos;

    @FXML
    void onAgendarAction(ActionEvent event) {
        FXMLLoader loader = sceneManager.loadCenterBorderPane("/br/com/washii/view/agendamentos/cliente-agendamento.fxml");

        ClienteAgendamentoController controller = loader.getController();

        controller.setLavaJato(lavaJato);
    }

    public void setDados(LavaJato lj) {
        this.lavaJato = lj;

        lblNome.setText(lj.getNome());

        Endereco endereco = lj.getEndereco();
        lblEndereco.setText(endereco.getRua() + ", " + endereco.getNumero() + "\n" + endereco.getCidade() + "\n" + endereco.getEstado());

        String star = "★";
        lblAvaliacao.setText(star + " 0.0 (sem avaliações)");
        
        // Limpa as listas padrões do FXML
        vboxServicos.getChildren().clear();

        // Adiciona os serviços dinamicamente
        for (Servico s : lj.getServicosOferecidos()) {
            Label item = new Label("• " + s.getNome());
            item.setWrapText(true);
            item.getStyleClass().add("item-lista");
            vboxServicos.getChildren().add(item);
        }
    }
}
package br.com.washii.presentation.agendamentos;


import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.CategoriaVeiculo;
import br.com.washii.presentation.core.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NegocioCardController extends BaseController{

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
    private VBox vboxVeiculos;

    @FXML
    void onAgendarAction(ActionEvent event) {
        sceneManager.loadCenterBorderPane("/br/com/washii/view/agendamentos/cliente-agendamento.fxml");
    }

    public void setDados(LavaJato lj) {
        lblNome.setText(lj.getNome());
        // Exemplo de endereço formatado

        Endereco endereco = lj.getEndereco();
        lblEndereco.setText(endereco.getRua() + ", " + endereco.getNumero() + "\n" + endereco.getCidade() + "\n" + endereco.getEstado());

        String star = "★";
        lblAvaliacao.setText(star + " 0.0 (sem avaliações)");
        
        // Limpa as listas padrões do FXML
        vboxServicos.getChildren().clear();
        vboxVeiculos.getChildren().clear();

        // Adiciona os serviços dinamicamente
        for (Servico s : lj.getServicosOferecidos()) {
            Label item = new Label("• " + s.getNome());
            item.getStyleClass().add("item-lista");
            vboxServicos.getChildren().add(item);
        }
        
        // Adiciona os veículos
        for (CategoriaVeiculo v : lj.getCategoriasAceitas()) {
            Label item = new Label("• " + v.toString().toLowerCase());
            item.getStyleClass().add("item-lista");
            vboxVeiculos.getChildren().add(item);
        }
    }
}
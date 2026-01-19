package br.com.washii.presentation.screens.home;

import java.io.IOException;
import java.util.List;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.presentation.components.cards.NegocioCardController;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class HomeClienteController extends BaseController{

    private UsuarioService usuarioService;

    @FXML
    private Button btnFiltrar;

    @FXML
    private ComboBox<CategoriaServico> cmbServico;

    @FXML
    private TextField txtLocalizacao;

    @FXML
    private TextField txtPreco;

    @FXML
    private FlowPane gridCards;


    public HomeClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @FXML
    public void initialize() {
        configurarChoiceBox();
    }

    @FXML
    private void configurarChoiceBox() {
        if (cmbServico != null) {
            cmbServico.setItems(FXCollections.observableArrayList(CategoriaServico.values()));

            cmbServico.setConverter(new StringConverter<CategoriaServico>() {
                @Override
                public String toString(CategoriaServico categoria) {
                    return (categoria == null) ? "Selecione..." : categoria.getNome();
                }

                @Override
                public CategoriaServico fromString(String string) {
                    return null; 
                }
            });
        }
    }

    @FXML
    private void handleLimparFiltro() {
        txtLocalizacao.clear();
        txtPreco.clear();
        cmbServico.setValue(null);
        
        }


    public void carregarNegocios(){
        List<Negocio> negocios = usuarioService.listarTodosNegocios();


        for (Negocio negocio : negocios) {
            if (negocio instanceof LavaJato lavaJato){
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/components/cards/negocio-card.fxml"));
                    VBox card = loader.load();

                    NegocioCardController controller = loader.getController();
            
                    controller.setSceneManager(this.sceneManager); 
                    controller.setDados(lavaJato);

                    gridCards.getChildren().add(card);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

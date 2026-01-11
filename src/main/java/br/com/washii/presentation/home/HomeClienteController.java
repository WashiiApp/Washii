package br.com.washii.presentation.home;

import java.io.IOException;
import java.util.List;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.presentation.agendamentos.NegocioCardController;
import br.com.washii.presentation.core.BaseController;
import br.com.washii.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

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

    public void carregarNegocios(){
        List<Negocio> negocios = usuarioService.listarTodosNegocios();


        for (Negocio negocio : negocios) {
            if (negocio instanceof LavaJato lavaJato){
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/agendamentos/negocio-card.fxml"));
                    VBox card = loader.load();

                    // 2. Obtém o Controller do Card que acabou de ser carregado
                    NegocioCardController controller = loader.getController();
            
                    // 3. Injeta o SceneManager (se o BaseController exigir) e os dados
                    controller.setSceneManager(this.sceneManager); 
                    controller.setDados(lavaJato);

                    // 4. Adiciona o card pronto ao container da tela principal
                    gridCards.getChildren().add(card);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

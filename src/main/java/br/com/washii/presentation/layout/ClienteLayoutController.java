package br.com.washii.presentation.layout;

import java.util.Arrays;
import java.util.List;
import br.com.washii.presentation.core.SceneManager;
import br.com.washii.presentation.home.HomeClienteController;
import br.com.washii.service.AutenticacaoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ClienteLayoutController extends LayoutController {

    // O JavaFX injeta automaticamente o controller do <fx:include>
    @FXML private HomeClienteController homeClienteController;

    @FXML
    private HBox navHome, navAgendamentos, navPerfil;

    private List<HBox> allNavContainer;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        // 1. Seta no próprio Layout
        super.setSceneManager(sceneManager);
        
        // 2. Repassa para a Home que foi incluída
        if (homeClienteController != null) {
            homeClienteController.setSceneManager(sceneManager);
            homeClienteController.carregarNegocios();
        }
    }

    public ClienteLayoutController(AutenticacaoService autenticacaoService) {
        super(autenticacaoService);
    }

    @FXML
    void initialize() {
        setBoasVindas();

        allNavContainer = Arrays.asList(navHome, navAgendamentos, navPerfil);
    }

    @FXML
    void handleNavClick(MouseEvent event){
        HBox containerClicked = (HBox) event.getSource();

        for (HBox nav : allNavContainer) {
            nav.getStyleClass().remove("nav-container-active");
        }

        containerClicked.getStyleClass().add("nav-container-active");

        if (containerClicked == navHome) {
            FXMLLoader loader =sceneManager.loadCenterBorderPane("/br/com/washii/view/home/home-cliente.fxml");

            HomeClienteController controller = loader.getController();
            controller.carregarNegocios();

        } else if (containerClicked == navAgendamentos) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/agendamentos/meus-agendamentos-cliente.fxml");

        } else if (containerClicked == navPerfil) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/perfil/perfil-usuario.fxml");
        }
    }
}
package br.com.washii.presentation.layout;

import java.util.Arrays;
import java.util.List;

import br.com.washii.presentation.core.SceneManager;
import br.com.washii.presentation.home.HomeNegocioController;
import br.com.washii.service.AutenticacaoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class NegocioLayoutController extends LayoutController {

    @FXML
    private HomeNegocioController homeNegocioController;

    @FXML
    private HBox navHome, navAgendamentos, navHistorico, navServico, navRelatorio, navPerfil;

    private List<HBox> allNavContainer;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        // 1. Seta no próprio Layout
        super.setSceneManager(sceneManager);
        
        // 2. Repassa para a Home que foi incluída
        if (homeNegocioController != null) {
            homeNegocioController.setSceneManager(sceneManager);
        }
    }

    public NegocioLayoutController(AutenticacaoService autenticacaoService) {
        super(autenticacaoService);
    }

    @FXML
    void initialize() {
        setBoasVindas();

        allNavContainer = Arrays.asList(navHome, navAgendamentos, navHistorico, navServico, navRelatorio, navPerfil);
    }

    @FXML
    void handleNavClick(MouseEvent event){
        HBox containerClicked = (HBox) event.getSource();

        for (HBox nav : allNavContainer) {
            if(nav.getStyleClass().remove("nav-container-active")){
            }
        }

        containerClicked.getStyleClass().add("nav-container-active");

        if (containerClicked == navHome) {
            FXMLLoader loader = sceneManager.loadCenterBorderPane("/br/com/washii/view/home/home-negocio.fxml");
            HomeNegocioController controller = loader.getController();
            controller.carregarDados();

        } else if (containerClicked == navAgendamentos) {
            sceneManager.loadCenterBorderPane(null);
            
        } else if (containerClicked == navServico) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/servico/gestao-servicos.fxml");
        } else if (containerClicked == navPerfil) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/perfil/perfil-negocio.fxml");
        }
    }
}

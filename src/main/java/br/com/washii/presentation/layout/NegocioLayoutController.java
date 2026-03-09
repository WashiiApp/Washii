package br.com.washii.presentation.layout;

import java.util.Map;
import br.com.washii.presentation.core.SceneManager;
import br.com.washii.presentation.screens.home.HomeNegocioController;
import br.com.washii.service.AutenticacaoService;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class NegocioLayoutController extends LayoutController {

    @FXML private HomeNegocioController homeNegocioController;
    @FXML private HBox navHome, navAgendamentos, navServico, navRelatorio, navPerfil;
    private HBox selectedContainer;
    private Map<HBox, String> navigationMap;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        super.setSceneManager(sceneManager);

        setSceneManagerOnHomeNegocioController(sceneManager);
    }

    private void setSceneManagerOnHomeNegocioController(SceneManager sceneManager) {
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

        selectedContainer = navHome;

        initializeNavigationMap();
    }

    private void initializeNavigationMap() {
        navigationMap = Map.of(
            navHome, "/br/com/washii/view/home/home-negocio.fxml",
            navAgendamentos, "/br/com/washii/view/agendamentos/meus-agendamentos-negocio.fxml",
            navServico, "/br/com/washii/view/servico/gestao-servicos.fxml",
            navRelatorio, "/br/com/washii/view/relatorio/relatorio-negocio.fxml",
            navPerfil, "/br/com/washii/view/perfil/perfil-negocio.fxml"
        );
    }

    @FXML
    void handleNavClick(MouseEvent event){
        
        removeStyleSelectedContainer();

        updateSelectedContainer(event);

        addStyleSelectedContainer();

        navigateToSelectedContainer();
    }

    private void removeStyleSelectedContainer() {
        selectedContainer.getStyleClass().remove("nav-container-active");
    }

    private void updateSelectedContainer(MouseEvent event) {
        selectedContainer = (HBox) event.getSource();
    }

    private void addStyleSelectedContainer() {
        selectedContainer.getStyleClass().add("nav-container-active");
    }

    private void navigateToSelectedContainer(){
        String pathView = navigationMap.get(selectedContainer);

        if (pathView == null) {
            throw new IllegalStateException("Container não mapeado: " + selectedContainer);
        }

        sceneManager.loadCenterBorderPane(pathView);
    }
}

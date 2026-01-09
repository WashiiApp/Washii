package br.com.washii.presentation.layout;

import java.util.Arrays;
import java.util.List;
import br.com.washii.service.AutenticacaoService;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ClienteLayoutController extends LayoutController {

    @FXML
    private HBox navHome, navAgendamentos, navHistorico, navPerfil;

    private List<HBox> allNavContainer;

    public ClienteLayoutController(AutenticacaoService autenticacaoService) {
        super(autenticacaoService);
    }

    @FXML
    void initialize() {
        setBoasVindas();

        allNavContainer = Arrays.asList(navHome, navAgendamentos, navHistorico, navPerfil);
    }

    @FXML
    void handleNavClick(MouseEvent event){
        HBox containerClicked = (HBox) event.getSource();
        HBox antecessor = null;

        for (HBox nav : allNavContainer) {
            if(nav.getStyleClass().remove("nav-container-active")){
                antecessor = nav;
            }
        }

        containerClicked.getStyleClass().add("nav-container-active");

        if (containerClicked == navHome && containerClicked != antecessor) {
            sceneManager.loadCenterBorderPane(null);
        } else if (containerClicked == navAgendamentos && containerClicked != antecessor) {
            sceneManager.loadCenterBorderPane(null);
        } else if (containerClicked == navHistorico && containerClicked != antecessor) {
            sceneManager.loadCenterBorderPane(null);
        } else if (containerClicked == navPerfil && containerClicked != antecessor) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/perfil/perfil-usuario.fxml");
        }
    }

    
}
package br.com.washii.presentation.layout;

import java.util.Arrays;
import java.util.List;

import br.com.washii.service.AutenticacaoService;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class NegocioLayoutController extends LayoutController {

    @FXML
    private HBox navHome, navAgendamentos, navHistorico, navServico, navRelatorio, navPerfil;

    private List<HBox> allNavContainer;

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
            sceneManager.loadCenterBorderPane(null);
        }
    }
}

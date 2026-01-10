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

        for (HBox nav : allNavContainer) {
            nav.getStyleClass().remove("nav-container-active");
        }

        containerClicked.getStyleClass().add("nav-container-active");

        if (containerClicked == navHome) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/homescene/home-tela.fxml");
        } else if (containerClicked == navAgendamentos) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/agendamentos/negocio-card.fxml");
        } else if (containerClicked == navHistorico) {
            sceneManager.loadCenterBorderPane(null);
        } else if (containerClicked == navPerfil) {
            sceneManager.loadCenterBorderPane("/br/com/washii/view/perfil/perfil-usuario.fxml");
        }
    }
}
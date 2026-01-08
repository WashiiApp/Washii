package br.com.washii.presentation.servico;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.service.ServicoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

public class GestaoServicosController {

    private ServicoService servicoService;

    @FXML
    private ChoiceBox<CategoriaServico> cbCategoria;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colDescricao;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colPreco;

    @FXML
    private TableView<?> tblServicos;

    @FXML
    private TextFlow avisoContainer;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    void initialize(){
        // Preenche o ChoiceBox
        cbCategoria.setItems(FXCollections.observableArrayList(CategoriaServico.values()));

        cbCategoria.setConverter(new StringConverter<CategoriaServico>() {
            @Override
            public String toString(CategoriaServico categoria) {
                return (categoria == null) ? "Selecione..." : categoria.getNome();
            }

            @Override
            public CategoriaServico fromString(String string) {
                return null; // Não necessário para ChoiceBox de seleção simples
            }
    });
    }

    public GestaoServicosController(ServicoService service){
        this.servicoService = service;
    }

    @FXML
    void onAdicionarServico(ActionEvent event) {
        limparCampoAviso();

        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();
        String precoStr = txtPreco.getText(); 
        CategoriaServico categoriaServico = cbCategoria.getValue();

        if (!(Sessao.getInstance().getUsuarioLogado() instanceof Negocio)){
            exibirAvisoErro("Usuário não é do tipo Negocio. \nLogo, não pode criar um servico");
            return;
        }
        
        Negocio negocio = (Negocio) Sessao.getInstance().getUsuarioLogado();

        if (nome.isBlank() || descricao.isBlank() || precoStr.isBlank() || (categoriaServico == null)){
            exibirAvisoErro("Peencha todos os campos, por favor.");
            return;
        }

        double preco = Double.parseDouble(precoStr.replace(",", "."));

        Servico servico = new Servico(nome, descricao, categoriaServico, preco, negocio);

        try {
            //servicoService.salvarServico(servico);

            exibirAvisoSucesso("Servico cadastrado com sucesso!");
        } catch (NegocioException e) {
            exibirAvisoErro(e.getMessage());
        } catch (Exception e) {
            exibirAvisoErro("Ocorreu um erro inesperado: " + e.getCause());
            e.printStackTrace();
        }
    }

    @FXML
    void onLimpar(ActionEvent event) {
        limparCampoAviso();

        txtNome.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        cbCategoria.setValue(null);

    }

    private void exibirAvisoErro(String msg) {
        avisoContainer.setVisible(true);
        avisoContainer.getStyleClass().clear();

        avisoContainer.getStyleClass().add("error-container");

        Text erro = new Text(msg);
        avisoContainer.getChildren().add(erro);
    }

    private void exibirAvisoSucesso(String msg){
        avisoContainer.setVisible(true);
        avisoContainer.getStyleClass().clear();

        avisoContainer.getStyleClass().add("success-container");

        Text aviso = new Text(msg);
        avisoContainer.getChildren().add(aviso);
    }

    private void limparCampoAviso(){
        avisoContainer.getStyleClass().clear();
        avisoContainer.setVisible(false);
        avisoContainer.getChildren().clear();
    }

}

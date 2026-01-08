package br.com.washii.presentation.servico;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.exceptions.NegocioException;
import br.com.washii.infra.session.Sessao;
import br.com.washii.presentation.utils.AvisoUtils;
import br.com.washii.service.ServicoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

public class GestaoServicosController {

    private ServicoService servicoService;

    @FXML
    private ChoiceBox<CategoriaServico> cbCategoria;

    @FXML
    private TableColumn<Servico, CategoriaServico> colCategoria;

    @FXML
    private TableColumn<Servico, String> colDescricao;

    @FXML
    private TableColumn<Servico, String> colNome;

    @FXML
    private TableColumn<Servico, Double> colPrecoBase;

    @FXML
    private TableView<Servico> tblServicos;

    @FXML
    private TextFlow avisoContainerNS; // container de erro do Novo Serviço

    @FXML
    private TextFlow avisoContainerSC; // container de erro do Serviços Cadastrados

    @FXML
    private TextArea txtDescricao;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnEditar;

    private ObservableList<Servico> listaServicos = FXCollections.observableArrayList();

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

        // 1. Ligar as colunas aos atributos da classe Servico
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaServico"));
        colPrecoBase.setCellValueFactory(new PropertyValueFactory<>("precoBase"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        // 2. Ligar a lista à tabela
        tblServicos.setItems(listaServicos);

        // Padronização da celula do preco 
        colPrecoBase.setCellFactory(tc -> new TableCell<Servico, Double>() {
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", preco));
                }
            }
        });


    }

    public GestaoServicosController(ServicoService service){
        this.servicoService = service;

        atualizarListaServicos();
    }

    @FXML
    void onAdicionarServico(ActionEvent event) {

        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();
        String precoStr = txtPreco.getText(); 
        CategoriaServico categoriaServico = cbCategoria.getValue();

        if (!(Sessao.getInstance().getUsuarioLogado() instanceof Negocio)){
            AvisoUtils.exibirAvisoErro(avisoContainerNS, "Usuário não é do tipo Negocio. \nLogo, não pode criar um servico");
            return;
        }
        
        Negocio negocio = (Negocio) Sessao.getInstance().getUsuarioLogado();

        if (nome.isBlank() || descricao.isBlank() || precoStr.isBlank() || (categoriaServico == null)){
            AvisoUtils.exibirAvisoAlerta(avisoContainerNS, "Peencha todos os campos, por favor");
            return;
        }

        double preco = Double.parseDouble(precoStr.replace(",", "."));

        Servico servico = new Servico(nome, descricao, categoriaServico, preco, negocio);

        try {
            servicoService.lista.add(servico);


            AvisoUtils.exibirAvisoSucesso(avisoContainerNS, "Serviço cadastrado com sucesso");

            atualizarListaServicos();

        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());
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

    @FXML
    void onRemover(ActionEvent event){

    }

    @FXML
    void onEditar(ActionEvent event) {

    }

    private void limparCampoAviso(){
        avisoContainerNS.getChildren().clear();
    }

    private void atualizarListaServicos(){
        //List<Servico> lista = servicoService.listarServicos((Negocio) Sessao.getInstance().getUsuarioLogado());

        if (tblServicos != null){
            tblServicos.getItems().clear();
        }
        
        listaServicos.addAll(servicoService.lista);
    }

}

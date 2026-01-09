package br.com.washii.presentation.servico;

import java.util.List;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button btnAdicionar;

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnEditar;

    private ObservableList<Servico> listaServicos = FXCollections.observableArrayList();

    private Servico servicoEmEdicao = null;

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

        // Padronização da celula do preço 
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

        if (servicoEmEdicao != null) {
            atualizarServico();
            return;
        }

        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();
        String precoStr = txtPreco.getText(); 
        CategoriaServico categoriaServico = cbCategoria.getValue();

        if (!(Sessao.getInstance().getUsuarioLogado() instanceof Negocio)){
            AvisoUtils.exibirAvisoErro(avisoContainerNS, "Usuário não é do tipo Negocio. \nLogo, não pode criar um serviço");
            return;
        }
        
        Negocio negocio = (Negocio) Sessao.getInstance().getUsuarioLogado();

        if (nome.isBlank() || descricao.isBlank() || precoStr.isBlank() || (categoriaServico == null)){
            AvisoUtils.exibirAvisoAlerta(avisoContainerNS, "Peencha todos os campos, por favor");
            return;
        }

        double preco;

        try{
            preco = Double.parseDouble(precoStr.replace(",", "."));
        } catch (NumberFormatException e){
            AvisoUtils.exibirAvisoErro(avisoContainerNS, "Preço inválido");
            return;
        }
            
        Servico servico = new Servico(nome, descricao, categoriaServico, preco, negocio);

        try {
            servicoService.salvarServico(servico);

            AvisoUtils.exibirAvisoSucesso(avisoContainerNS, "Serviço cadastrado com sucesso");

            AvisoUtils.limparCampoAviso(avisoContainerNS, 5);

            atualizarListaServicos();
            onLimpar(event);

        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());       
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onLimpar(ActionEvent event) {
        limparCampos();
        AvisoUtils.limparCampoAviso(avisoContainerNS, 0);

        if (servicoEmEdicao != null) {
            servicoEmEdicao = null;
            btnAdicionar.setText("+ Adicionar");
            btnLimpar.setText("Limpar");
        }
    }

    @FXML
    void onRemover(ActionEvent event){
        Servico servicoSelecionado = tblServicos.getSelectionModel().getSelectedItem();

        if (servicoSelecionado == null) {
            AvisoUtils.exibirAvisoAlerta(avisoContainerSC, "Selecione um serviço na tabela para remover.");
            return;
        }

        // 2. Criar um alerta de confirmação nativo do JavaFX
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Deseja realmente excluir o serviço: " + servicoSelecionado.getNome() + "?");
        alert.setContentText("Esta ação não pode ser desfeita.");

        if (alert.showAndWait().get() == ButtonType.OK){
            try {
                servicoService.removerServico(servicoSelecionado);

                atualizarListaServicos();

                AvisoUtils.exibirAvisoSucesso(avisoContainerSC, "Serviço removido com sucesso!");

                AvisoUtils.limparCampoAviso(avisoContainerSC, 3);

            } catch (NegocioException e) {
                AvisoUtils.exibirAvisoErro(avisoContainerSC, e.getMessage());
            } catch (Exception e) {
                AvisoUtils.exibirAvisoErro(avisoContainerSC,"Houve um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onEditar(ActionEvent event) {
        servicoEmEdicao = tblServicos.getSelectionModel().getSelectedItem();

        if (servicoEmEdicao == null) {
            AvisoUtils.exibirAvisoAlerta(avisoContainerSC, "Selecione um serviço para editar.");
            return;
        }

        txtNome.setText(servicoEmEdicao.getNome());
        txtDescricao.setText(servicoEmEdicao.getDescricao());
        txtPreco.setText(String.valueOf(servicoEmEdicao.getPrecoBase()));
        cbCategoria.setValue(servicoEmEdicao.getCategoriaServico());

        btnAdicionar.setText("Atualizar");
        btnLimpar.setText("Cancelar");

    }

    private void atualizarServico() {
        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();
        String precoStr = txtPreco.getText(); 
        CategoriaServico categoriaServico = cbCategoria.getValue();

        if (nome.isBlank() || descricao.isBlank() || precoStr.isBlank() || (categoriaServico == null)){
            AvisoUtils.exibirAvisoAlerta(avisoContainerNS, "Peencha todos os campos, por favor");
            return;
        }

        double preco;

        try{
            preco = Double.parseDouble(precoStr.replace(",", "."));
        } catch (NumberFormatException e){
            AvisoUtils.exibirAvisoErro(avisoContainerNS, "Preço inválido");
            return;
        }

        servicoEmEdicao.setNome(nome);
        servicoEmEdicao.setDescricao(descricao);
        servicoEmEdicao.setPrecoBase(preco);
        servicoEmEdicao.setCategoriaServico(categoriaServico);

        try{
            servicoService.atualizarServico(servicoEmEdicao);

            onLimpar(null);
            atualizarListaServicos();

            btnAdicionar.setText("+ Adicionar");
            btnLimpar.setText("Limpar");

            servicoEmEdicao = null;

            AvisoUtils.exibirAvisoSucesso(avisoContainerNS, "Serviço atualizado com sucesso");

            AvisoUtils.limparCampoAviso(avisoContainerNS, 5);

        } catch (NegocioException e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());
        } catch (Exception e) {
            AvisoUtils.exibirAvisoErro(avisoContainerNS, e.getMessage());
            e.printStackTrace();
        }
        
    }

    private void atualizarListaServicos(){
        List<Servico> lista = servicoService.listarServicos((Negocio) Sessao.getInstance().getUsuarioLogado());

        if (tblServicos != null){
            tblServicos.getItems().clear();
        }
        
        listaServicos.addAll(lista);
    }

    private void limparCampos(){
        txtNome.setText("");
        txtDescricao.setText("");
        txtPreco.setText("");
        cbCategoria.setValue(null);
    }
}
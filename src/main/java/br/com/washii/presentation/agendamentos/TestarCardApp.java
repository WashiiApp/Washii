package br.com.washii.presentation.agendamentos;

import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.enums.CategoriaVeiculo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestarCardApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Carrega o FXML (certifique-se de que o caminho está correto conforme seu projeto)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/washii/view/agendamentos/negocio-card.fxml"));
        VBox card = loader.load();

        // 2. Obtém o Controller
        NegocioCardController controller = loader.getController();

        // 3. Cria dados fictícios (Mock)
        LavaJato mockLavaJato = criarLavaJatoMock();

        // 4. Alimenta o card com os dados
        controller.setDados(mockLavaJato);

        // 5. Exibe a janela
        Scene scene = new Scene(card);
        primaryStage.setTitle("Washii - Teste de Card");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private LavaJato criarLavaJatoMock() {
        LavaJato lj = new LavaJato();
        lj.setNome("Lava Jato Brilho Supremo");
        
        Endereco end = new Endereco();
        end.setRua("Rua das Oliveiras");
        end.setCidade("Bananeiras");
        end.setNumero("400");
        end.setEstado("Paraiba");
        lj.setEndereco(end);

        List<Servico> servicos = new ArrayList<>();
        servicos.add(new Servico("lava vidro", "sem descrição", CategoriaServico.HIGIENIZACAO, 25.0, lj));
        servicos.add(new Servico("lavagem completa", "sem descrição", CategoriaServico.HIGIENIZACAO, 25.0, lj));
        servicos.add(new Servico("lava pneu", "sem descrição", CategoriaServico.HIGIENIZACAO, 25.0, lj));
        lj.setServicosOferecidos(servicos);

        Set<CategoriaVeiculo> categorias = new HashSet<CategoriaVeiculo>();
        categorias.add(CategoriaVeiculo.CARRO);
        categorias.add(CategoriaVeiculo.MOTO);
        categorias.add(CategoriaVeiculo.SUV);
        lj.setCategoriasAceitas(categorias);

        return lj;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package br.com.washii.persistence;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.repository.ServicoRepository;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.infra.database.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoPersistence implements ServicoRepository {

    @Override
    public void salvar(Servico entidade) {
        String sql = """
                INSERT INTO servico (nome, descricao, tipo, precobase, id_Negocio)
                VALUES (?,?,?::categoriaServico,?,?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement smtm = conn.prepareStatement(sql)) {

            smtm.setString(1, entidade.getNome());
            smtm.setString(2, entidade.getDescricao());
            smtm.setString(3, entidade.getCategoriaServico().name());
            smtm.setDouble(4, entidade.getPrecoBase());
            smtm.setLong(5,entidade.getNegocio().getId());

            smtm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Servico entidade) {
        String sql = """
                UPDATE servico
                SET nome = ?, descricao = ?, tipo = ?::categoriaServico, precobase = ?
                WHERE id = ?
        """;

        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement smtm = conn.prepareStatement(sql)) {

            smtm.setString(1, entidade.getNome());
            smtm.setString(2, entidade.getDescricao());
            smtm.setString(3, entidade.getCategoriaServico().name());
            smtm.setDouble(4, entidade.getPrecoBase());
            smtm.setLong(5, entidade.getId());
            smtm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remover(Long id) {
        String sql = """
                DELETE FROM servico WHERE id = ?
        """;

        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement smtm = conn.prepareStatement(sql)) {

            smtm.setLong(1, id);
            smtm.executeUpdate();

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Servico> buscarPorId(Long id) {
        String sql = """
    SELECT 
        s.id AS servico_id,
        s.nome,
        s.descricao,
        s.tipo,
        s.precobase,

        n.id AS negocio_id,
        n.cnpj,
        n.razao_social
    FROM servico s
    LEFT JOIN negocio n ON n.id = s.id_negocio
    WHERE s.id = ?
""";


        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("servico_id"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setCategoriaServico(
                        CategoriaServico.valueOf(rs.getString("tipo"))
                );
                servico.setPrecoBase(rs.getDouble("precobase"));

                if (rs.getObject("negocio_id") != null) {
                    Negocio negocio = new Negocio() {};
                    negocio.setId(rs.getLong("negocio_id"));
                    negocio.setCnpj(rs.getString("cnpj"));
                    negocio.setRazaoSocial(rs.getString("razao_social"));
                    servico.setNegocio(negocio);
                }

                return Optional.of(servico);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar serviço", e);
        }
    }



    @Override
    public List<Servico> listarTodos() {
        String sql = """
        SELECT id, nome, descricao, tipo, precobase, id_negocio
        FROM servico
    """;

        List<Servico> servicos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("id"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setCategoriaServico(
                        CategoriaServico.valueOf(rs.getString("tipo"))
                );
                servico.setPrecoBase(rs.getDouble("precobase"));

                Negocio negocio = new Negocio() {}; // classe anônima
                negocio.setId(rs.getLong("id_negocio"));
                servico.setNegocio(negocio);

                servicos.add(servico);
            }

            return servicos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar serviços", e);
        }
    }

    @Override
    public List<Servico> listarPorNegocio(Long negocioId) {

        String sql = """
        SELECT id, nome, descricao, tipo, precobase
        FROM servico
        WHERE id_negocio = ?
        ORDER BY id
    """;

        List<Servico> servicos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, negocioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("id"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setCategoriaServico(
                        CategoriaServico.valueOf(rs.getString("tipo"))
                );
                servico.setPrecoBase(rs.getDouble("precobase"));

                // referência de negocio
                Negocio negocio = new Negocio() {}; // classe anônima
                negocio.setId(negocioId);
                servico.setNegocio(negocio);

                servicos.add(servico);
            }

            return servicos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar serviços por negócio", e);
        }
    }
}

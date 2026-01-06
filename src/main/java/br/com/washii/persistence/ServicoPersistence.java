package br.com.washii.persistence;

import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.repository.ServicoRepository;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.infra.database.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ServicoPersistence implements ServicoRepository {


    @Override
    public List<Servico> listarPorNegocio(Long negocioId) {
        return List.of();
    }

    @Override
    public void salvar(Servico entidade) {
        String sql = """
                INSERT INTO servico (nome, descricao, tipo, precobase, idNegocio)
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
        }}

    @Override
    public void atualizar(Servico entidade) {
        String sql = """
                UPDATE servico
                SET nome = ?, descricao = ?, tipo = ?::categoriaServico, precobase = ?, idNegocio = ?
                WHERE id = ?
        """;

        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement smtm = conn.prepareStatement(sql)) {

            smtm.setString(1, entidade.getNome());
            smtm.setString(2, entidade.getDescricao());
            smtm.setString(3, entidade.getCategoriaServico().name());
            smtm.setDouble(4, entidade.getPrecoBase());
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
    public Optional<Servico> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Servico> listarTodos() {
        return List.of();
    }
}

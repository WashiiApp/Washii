package br.com.washii.persistence;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.repository.AgendamentoRepository;
import br.com.washii.infra.database.DatabaseConfig;

import java.awt.datatransfer.Clipboard;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.sql.*;

public class AgendamentoPersistence implements AgendamentoRepository {
    @Override
    public List<Agendamento> listarPorPeriodoENegocio(LocalDate inicio, LocalDate fim, Long negocioId) {
        return List.of();
    }

    @Override
    public List<Agendamento> listarPorCliente(Long clienteId) {
        return List.of();
    }

    @Override
    public void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus) {

    }

    @Override
    public int contarAgendamento(Long negocioId, LocalDate data, LocalTime hora) {
        return 0;
    }

    @Override
    public void salvar(Agendamento entidade) {
        String sqlAgendamento = """
        INSERT INTO agendamento (data, hora, status, id_cliente, id_veiculo, id_negocio)
        VALUES (?, ?, ?::status_agendamento, ?, ?, ?)
    """;

        String sqlServico = """
        INSERT INTO agendamento_servico (id_agendamento, id_servico)
        VALUES (?, ?)
    """;

        try (Connection conn = DatabaseConfig.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement stmtAg = conn.prepareStatement(
                    sqlAgendamento, Statement.RETURN_GENERATED_KEYS
            );

            stmtAg.setDate(1, Date.valueOf(entidade.getData()));
            stmtAg.setTime(2, Time.valueOf(entidade.getHora()));
            stmtAg.setString(3, entidade.getStatus().name());
            stmtAg.setLong(4, entidade.getCliente().getId());
            stmtAg.setLong(5, entidade.getVeiculo().getId());
            stmtAg.setLong(6, entidade.getNegocio().getId());

            stmtAg.executeUpdate();

            ResultSet rs = stmtAg.getGeneratedKeys();
            if (rs.next()) {
                entidade.setId(rs.getLong(1));
            }

            PreparedStatement stmtServ = conn.prepareStatement(sqlServico);

            for (Servico servico : entidade.getServicos()) {
                stmtServ.setLong(1, entidade.getId());
                stmtServ.setLong(2, servico.getId());
                stmtServ.addBatch();
            }

            stmtServ.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar agendamento", e);
        }
    }

    @Override
    public void atualizar(Agendamento entidade) {

    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM agendamento WHERE id = ?";

        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar agendamento", e);
        }
    }

    @Override
    public Optional<Agendamento> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Agendamento> listarTodos() {
        return List.of();
    }
}

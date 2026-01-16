package br.com.washii.persistence;

import br.com.washii.domain.entities.*;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.repository.AgendamentoRepository;
import br.com.washii.infra.database.DatabaseConfig;

import java.awt.datatransfer.Clipboard;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;

public class AgendamentoPersistence implements AgendamentoRepository {
    @Override
        public List<Agendamento> listarPorPeriodoENegocio(
                LocalDate inicio, LocalDate fim, Long negocioId) {

            String sql = """
        SELECT id, data, hora, status, id_cliente, id_veiculo
        FROM agendamento
        WHERE id_negocio = ?
          AND data BETWEEN ? AND ?
        ORDER BY data, hora
    """;

            List<Agendamento> agendamentos = new ArrayList<>();

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, negocioId);
                stmt.setDate(2, Date.valueOf(inicio));
                stmt.setDate(3, Date.valueOf(fim));

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Agendamento ag = new Agendamento();
                    ag.setId(rs.getLong("id"));
                    ag.setData(rs.getDate("data").toLocalDate());
                    ag.setHora(rs.getTime("hora").toLocalTime());
                    ag.setStatus(StatusAgendamento.valueOf(rs.getString("status")));

                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getLong("id_cliente"));
                    ag.setCliente(cliente);

                    Veiculo veiculo = new Veiculo();
                    veiculo.setId(rs.getLong("id_veiculo"));
                    ag.setVeiculo(veiculo);

                    Negocio negocio = new Negocio() {};
                    negocio.setId(negocioId);
                    ag.setNegocio(negocio);

                    agendamentos.add(ag);
                }

                return agendamentos;

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao listar agendamentos por período", e);
            }
        }

    @Override
    public List<Agendamento> listarPorCliente(Long clienteId) {
            String sql = """
        SELECT id, data, hora, status, id_veiculo, id_negocio
        FROM agendamento
        WHERE id_cliente = ?
        ORDER BY data DESC, hora DESC
    """;

            List<Agendamento> agendamentos = new ArrayList<>();

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, clienteId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Agendamento ag = new Agendamento();
                    ag.setId(rs.getLong("id"));
                    ag.setData(rs.getDate("data").toLocalDate());
                    ag.setHora(rs.getTime("hora").toLocalTime());
                    ag.setStatus(StatusAgendamento.valueOf(rs.getString("status")));

                    Cliente cliente = new Cliente();
                    cliente.setId(clienteId);
                    ag.setCliente(cliente);

                    Veiculo veiculo = new Veiculo();
                    veiculo.setId(rs.getLong("id_veiculo"));
                    ag.setVeiculo(veiculo);

                    Negocio negocio = new Negocio() {
                    };
                    negocio.setId(rs.getLong("id_negocio"));
                    ag.setNegocio(negocio);

                    agendamentos.add(ag);
                }

                return agendamentos;

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao listar agendamentos do cliente", e);
            }
        }

    @Override
    public void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus) {

        String sql = """
        UPDATE agendamento
        SET status = ?::status_agendamento
        WHERE id = ?
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus.name());
            stmt.setLong(2, agendamentoId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do agendamento", e);
        }
    }


    @Override
    public int contarAgendamento(Long negocioId, LocalDate data, LocalTime hora) {
        String sql = """
        SELECT COUNT(*) 
        FROM agendamento
        WHERE id_negocio = ?
          AND data = ?
          AND hora = ?
          AND status <> 'CANCELADO'
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, negocioId);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(hora));

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar agendamentos", e);
        }
    }

    @Override
    public void salvar(Agendamento entidade) {
        String sqlBuscarVeiculo = """
        SELECT id FROM veiculo WHERE placa = ?
    """;

        String sqlInserirVeiculo = """
        INSERT INTO veiculo (placa, categoria, id_cliente)
        VALUES (?, ?::CategoriaVeiculo, ? )
    """;

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

            Veiculo veiculo = entidade.getVeiculo();
            Long idVeiculo = null;

            try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBuscarVeiculo)) {
                stmtBusca.setString(1, veiculo.getPlaca());


                ResultSet rs = stmtBusca.executeQuery();
                if (rs.next()) {
                    idVeiculo = rs.getLong("id");
                }
            }

            if (idVeiculo == null) {
                try (PreparedStatement stmtInsertVeiculo =
                             conn.prepareStatement(sqlInserirVeiculo, Statement.RETURN_GENERATED_KEYS)) {

                    stmtInsertVeiculo.setString(1, veiculo.getPlaca());
                    stmtInsertVeiculo.setString(2, veiculo.getCategoriaVeiculo().name());
                    stmtInsertVeiculo.setLong(3, entidade.getCliente().getId());

                    stmtInsertVeiculo.executeUpdate();

                    ResultSet rs = stmtInsertVeiculo.getGeneratedKeys();
                    if (rs.next()) {
                        idVeiculo = rs.getLong(1);
                    }
                }
            }

            veiculo.setId(idVeiculo);

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
        String sqlAgendamento = """
        UPDATE agendamento
        SET data = ?,
            hora = ?,
            status = ?::status_agendamento,
            id_cliente = ?,
            id_veiculo = ?,
            id_negocio = ?
        WHERE id = ?
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmtAg = conn.prepareStatement(sqlAgendamento);) {


            stmtAg.setDate(1, Date.valueOf(entidade.getData()));
            stmtAg.setTime(2, Time.valueOf(entidade.getHora()));
            stmtAg.setString(3, entidade.getStatus().name());
            stmtAg.setLong(4, entidade.getCliente().getId());
            stmtAg.setLong(5, entidade.getVeiculo().getId());
            stmtAg.setLong(6, entidade.getNegocio().getId());
            stmtAg.setLong(7, entidade.getId());

            stmtAg.executeUpdate();

        }

     catch (SQLException e) {
        throw new RuntimeException("Erro ao atualizar agendamento", e);
    }

    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM agendamento WHERE id = ?";

        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover agendamento", e);
        }
    }

    @Override
    public Optional<Agendamento> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Agendamento> listarTodos() {
        String sql = """
        SELECT id, data, hora, status, id_cliente, id_veiculo, id_negocio
        FROM agendamento
        ORDER BY data, hora
    """;

        List<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento ag = new Agendamento();
                ag.setId(rs.getLong("id"));
                ag.setData(rs.getDate("data").toLocalDate());
                ag.setHora(rs.getTime("hora").toLocalTime());
                ag.setStatus(StatusAgendamento.valueOf(rs.getString("status")));

                Cliente c = new Cliente();
                c.setId(rs.getLong("id_cliente"));
                ag.setCliente(c);

                Veiculo v = new Veiculo();
                v.setId(rs.getLong("id_veiculo"));
                ag.setVeiculo(v);

                Negocio n = new Negocio() {};
                n.setId(rs.getLong("id_negocio"));
                ag.setNegocio(n);

                agendamentos.add(ag);
            }

            return agendamentos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar agendamentos", e);
        }
    }
}

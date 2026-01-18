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
        SELECT id, data, hora, status_agendamento, id_cliente, id_veiculo, id_negocio
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
                Agendamento ag = montarAgendamentoBasico(rs);

                Long idNegocio = rs.getLong("id_negocio");

                ag.setNegocio(
                        buscarNegocioCompleto(conn, idNegocio)
                );

                ag.setServicos(
                        buscarServicosDoAgendamento(conn, ag.getId())
                );

                agendamentos.add(ag);
            }

            return agendamentos;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar agendamentos por período e negócio", e
            );
        }
    }


    @Override
    public List<Agendamento> listarPorCliente(Long idUsuario) {

        String sql = """
        SELECT 
            a.id,
            a.data,
            a.hora,
            a.status_agendamento,
            a.id_cliente,
            a.id_veiculo,
            a.id_negocio
        FROM agendamento a
        WHERE a.id_cliente = ?
        ORDER BY a.data DESC, a.hora DESC
    """;

        List<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection()) {

            Long idCliente = buscarIdClientePorIdUsuario(conn, idUsuario);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, idCliente);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Agendamento ag = montarAgendamentoBasico(rs);

                    // Cliente
                    Cliente cliente = new Cliente();
                    cliente.setId(idCliente);
                    ag.setCliente(cliente);

                    // Negócio
                    Long idNegocio = rs.getLong("id_negocio");
                    ag.setNegocio(
                            buscarNegocioCompleto(conn, idNegocio)
                    );

                    // Veículo
                    Veiculo veiculo = new Veiculo();
                    veiculo.setId(rs.getLong("id_veiculo"));
                    ag.setVeiculo(veiculo);

                    // Serviços
                    ag.setServicos(
                            buscarServicosDoAgendamento(conn, ag.getId())
                    );

                    agendamentos.add(ag);
                }

            }

            return agendamentos;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar agendamentos do cliente", e
            );
        }
    }


    @Override
    public void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus) {

        String sql = """
        UPDATE agendamento
        SET status_agendamento = ?::status_agendamento
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
          AND status_agendamento IN ('EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO', 'AGENDADO','NAO_COMPARECEU')
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
        INSERT INTO agendamento (data, hora, status_agendamento, id_cliente, id_veiculo, id_negocio)
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

            // buscando id
            Long idCliente = buscarIdClientePorIdUsuario(
                    conn,
                    entidade.getCliente().getId()
            );

            if (idVeiculo == null) {
                try (PreparedStatement stmtInsertVeiculo =
                             conn.prepareStatement(sqlInserirVeiculo, Statement.RETURN_GENERATED_KEYS)) {

                    stmtInsertVeiculo.setString(1, veiculo.getPlaca());
                    stmtInsertVeiculo.setString(2, veiculo.getCategoriaVeiculo().name());
                    stmtInsertVeiculo.setLong(3, idCliente);

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
            stmtAg.setLong(4, idCliente);
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
            status_agendamento = ?::status_agendamento,
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
        SELECT id, data, hora, status_agendamento, id_cliente, id_veiculo, id_negocio
        FROM agendamento
        ORDER BY data, hora
    """;

        List<Agendamento> agendamentos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento ag = montarAgendamentoBasico(rs);

                Cliente c = new Cliente();
                c.setId(rs.getLong("id_cliente"));
                ag.setCliente(c);

                Long idNegocio = rs.getLong("id_negocio");
                ag.setNegocio(
                        buscarNegocioCompleto(conn, idNegocio)
                );

                Veiculo v = new Veiculo();
                v.setId(rs.getLong("id_veiculo"));
                ag.setVeiculo(v);

                ag.setServicos(
                        buscarServicosDoAgendamento(conn, ag.getId())
                );

                agendamentos.add(ag);
            }

            return agendamentos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar agendamentos", e);
        }
    }

    // buscar o id correto (usuario)
    private Long buscarIdClientePorIdUsuario(Connection conn, Long idUsuario) throws SQLException {
        String sql = "SELECT id FROM cliente WHERE id_usuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        }
        return null;
    }

    private List<Servico> buscarServicosDoAgendamento(Connection conn, Long idAgendamento) throws SQLException {
        String sql = """
        SELECT s.id, s.nome, s.preco_base
        FROM servico s
        JOIN agendamento_servico a ON a.id_servico = s.id
        WHERE a.id_agendamento = ?
    """;

        List<Servico> servicos = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idAgendamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Servico s = new Servico();
                s.setId(rs.getLong("id"));
                s.setNome(rs.getString("nome"));
                s.setPrecoBase(rs.getDouble("preco_base"));
                servicos.add(s);
            }
        }
        return servicos;
    }


    private Negocio buscarNegocioCompleto(Connection conn, Long idNegocio) throws SQLException {
        String sql = """
        SELECT 
            n.id_negocio,
            n.nome,
            e.rua,
            e.numero,
            e.cidade,
            e.estado
        FROM negocio n
        JOIN usuario u ON u.id = n.id_usuario
        JOIN endereco e ON e.id = u.id_endereco
        WHERE n.id_negocio = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idNegocio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Endereco end = new Endereco();
                end.setRua(rs.getString("rua"));
                end.setNumero(rs.getString("numero"));
                end.setCidade(rs.getString("cidade"));
                end.setEstado(rs.getString("estado"));

                Negocio neg = new Negocio() {};
                neg.setId(rs.getLong("id_negocio")); // 🔴 AQUI ERA O ERRO
                neg.setNome(rs.getString("nome"));
                neg.setEndereco(end);

                return neg;
            }
        }
        return null;
    }



    private Agendamento montarAgendamentoBasico(ResultSet rs) throws SQLException {
        Agendamento ag = new Agendamento();
        ag.setId(rs.getLong("id"));
        ag.setData(rs.getDate("data").toLocalDate());
        ag.setHora(rs.getTime("hora").toLocalTime());
        ag.setStatus(
                StatusAgendamento.valueOf(
                        rs.getString("status_agendamento")
                )
        );
        return ag;
    }
    }

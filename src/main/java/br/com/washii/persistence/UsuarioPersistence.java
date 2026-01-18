package br.com.washii.persistence;

import br.com.washii.domain.entities.*;
import br.com.washii.domain.repository.UsuarioRepository;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.infra.database.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class UsuarioPersistence implements UsuarioRepository {

    @Override
    public void salvar(Usuario usuario) {
        String sql = """
                INSERT INTO USUARIO (EMAIL, SENHA, NOME, TIPO, ID_ENDERECO)
                VALUES (?, ?, ?, ?::tipo_usuario, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            salvarEndereco(usuario);

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getTipoUsuario().name());
            stmt.setLong(5, usuario.getEndereco().getId());
            String tipo = usuario.getTipoUsuario().name();

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getLong(1));
            }

            if ("NEGOCIO".equals(tipo)) {
                String sqlNegocio = """
                    INSERT INTO negocio (id_usuario, cnpj, razao_social, inicio_expediente, fim_expediente)
                    VALUES (?, ?, ?, ?, ?)
                """;

                PreparedStatement stmtNegocio = conn.prepareStatement(sqlNegocio);
                stmtNegocio.setLong(1, usuario.getId());
                stmtNegocio.setString(2, null);
                stmtNegocio.setString(3, null);
//                LavaJato lavajato = new LavaJato(); -> antigo
                if (usuario instanceof LavaJato lavajato) {
                    stmtNegocio.setTime(4, Time.valueOf(lavajato.getInicioExpediente()));
                    stmtNegocio.setTime(5, Time.valueOf(lavajato.getFimExpediente()));
                }

                stmtNegocio.executeUpdate();

            }

            else {
                String sqlCliente = """
                        INSERT INTO cliente (id_usuario, telefone)
                        VALUES (?, ?)
                        """;

                PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente);
                stmtCliente.setLong(1, usuario.getId());
                stmtCliente.setString(2, null);

                stmtCliente.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }

    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = """
            UPDATE usuario
            SET email = ?, senha = ?, nome = ?, tipo = ?::tipo_usuario, id_endereco = ?
            WHERE id = ?
        """;

        String sqlNegocio = """
                UPDATE negocio
                SET cnpj = ?, razao_social = ?, inicio_expediente = ?, fim_expediente = ?
                WHERE id_usuario = ?
                """;

        String sqlCliente = """
                UPDATE cliente
                SET telefone = ?
                WHERE id_usuario = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection()) {

            TipoUsuario tipoUsuario = usuario.getTipoUsuario();

            conn.setAutoCommit(false);

            atualizarEndereco(conn, usuario.getEndereco());

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sql)) {

                stmtUsuario.setString(1, usuario.getEmail());
                stmtUsuario.setString(2, usuario.getSenha());
                stmtUsuario.setString(3, usuario.getNome());
                stmtUsuario.setString(4, usuario.getTipoUsuario().name());
                stmtUsuario.setLong(5, usuario.getEndereco().getId());
                stmtUsuario.setLong(6, usuario.getId());

                stmtUsuario.executeUpdate();
            }
            if (tipoUsuario.equals(TipoUsuario.NEGOCIO)) {
                if (usuario instanceof Negocio negocio) {

                    try (PreparedStatement stmtNegocio = conn.prepareStatement(sqlNegocio)) {

                        stmtNegocio.setString(1, negocio.getCnpj());
                        stmtNegocio.setString(2, negocio.getRazaoSocial());
                        stmtNegocio.setTime(3, Time.valueOf(negocio.getInicioExpediente()));
                        stmtNegocio.setTime(4, Time.valueOf(negocio.getFimExpediente()));
                        stmtNegocio.setLong(5, usuario.getId()); // id_usuario

                        stmtNegocio.executeUpdate();
                    }
                }
            }
            if (tipoUsuario.equals(TipoUsuario.CLIENTE)) {
                if (usuario instanceof Cliente cliente) {
                    try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                        stmtCliente.setString(1, cliente.getTelefone());
                        stmtCliente.setLong(2, usuario.getId());

                        stmtCliente.executeUpdate();
                        }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = """
    SELECT 
        u.id,
        u.email,
        u.senha,
        u.nome,
        u.tipo,

        e.id AS endereco_id,
        e.cep,
        e.pais,
        e.estado,
        e.bairro,
        e.rua,
        e.cidade,
        e.referencia,
        e.numero,

        c.telefone,
        n.cnpj,
        n.razao_social

    FROM usuario u
    JOIN endereco e ON e.id = u.id_endereco
    LEFT JOIN cliente c ON c.id_usuario = u.id
    LEFT JOIN negocio n ON n.id_usuario = u.id
    WHERE u.id = ?
""";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            String tipo = rs.getString("tipo");
            Usuario usuario;

            if ("CLIENTE".equals(tipo)) {
                Cliente cliente = new Cliente();
                Optional<Long> idClienteOpt = buscarIdClientePorIdUsuario(rs.getLong("id"));

                idClienteOpt.ifPresent(cliente::setId);

                cliente.setTelefone(rs.getString("telefone"));
                usuario = cliente;

            } else if ("NEGOCIO".equals(tipo)) {
                LavaJato lavaJato = new LavaJato();
                lavaJato.setCnpj(rs.getString("cnpj"));
                lavaJato.setRazaoSocial(rs.getString("razao_social"));
                usuario = lavaJato;

//                Negocio negocio = new Negocio();
//                negocio.setCnpj(rs.getString("cnpj"));
//                usuario = negocio;

            } else {
                throw new IllegalStateException("Tipo de usuário inválido");
            }


            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setNome(rs.getString("nome"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(tipo));

            Endereco endereco = new Endereco();
            endereco.setId(rs.getLong("endereco_id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setPais(rs.getString("pais"));
            endereco.setEstado(rs.getString("estado"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setRua(rs.getString("rua"));
            endereco.setCidade(rs.getString("cidade"));
            endereco.setReferencia(rs.getString("referencia"));
            endereco.setNumero(rs.getString("numero"));
            usuario.setEndereco(endereco);

            return Optional.of(usuario);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
    }

    public Optional<Long> buscarIdClientePorIdUsuario(Long idUsuario) {
        String sql = """
        SELECT id
        FROM cliente
        WHERE id_usuario = ?
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getLong("id"));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar id do cliente", e);
        }
    }



    @Override
    public List<Usuario> listarTodos() {
        String sql = """
    SELECT 
        u.id,
        u.email,
        u.senha,
        u.nome,
        u.tipo,

        e.id AS endereco_id,
        e.cep,
        e.pais,
        e.estado,
        e.bairro,
        e.rua,
        e.cidade,
        e.referencia,
        e.numero,

        c.telefone,
        n.cnpj,
        n.razao_social

    FROM usuario u
    JOIN endereco e ON e.id = u.id_endereco
    LEFT JOIN cliente c ON c.id_usuario = u.id
    LEFT JOIN negocio n ON n.id_usuario = u.id
    """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String tipoStr = rs.getString("tipo");
                Usuario usuario;

                if ("CLIENTE".equals(tipoStr)) {
                    Cliente cliente = new Cliente();
                    cliente.setTelefone(rs.getString("telefone"));
                    usuario = cliente;

                } else if ("NEGOCIO".equals(tipoStr)) {
                    LavaJato lavaJato = new LavaJato();
                    lavaJato.setCnpj(rs.getString("cnpj"));
                    lavaJato.setRazaoSocial(rs.getString("razao_social"));
                    usuario = lavaJato;

//                    Negocio negocio = new Negocio();
//                    negocio.setCnpj(rs.getString("cnpj"));
//                    usuario = negocio;

                } else {
                    throw new IllegalStateException("Tipo de usuário inválido");
                }

                usuario.setId(rs.getLong("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setNome(rs.getString("nome"));
                usuario.setTipoUsuario(TipoUsuario.valueOf(tipoStr));

                Endereco endereco = new Endereco();
                endereco.setId(rs.getLong("endereco_id"));
                endereco.setCep(rs.getString("cep"));
                endereco.setPais(rs.getString("pais"));
                endereco.setEstado(rs.getString("estado"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setRua(rs.getString("rua"));
                endereco.setCidade(rs.getString("cidade"));
                endereco.setNumero(rs.getString("numero"));
                endereco.setReferencia(rs.getString("referencia"));

                usuario.setEndereco(endereco);

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }

        return usuarios;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {

        String sql = """
    SELECT 
        u.id,
        u.email,
        u.senha,
        u.nome,
        u.tipo,

        e.id AS endereco_id,
        e.cep,
        e.pais,
        e.estado,
        e.bairro,
        e.rua,
        e.cidade,
        e.referencia,

        c.telefone,
        n.cnpj,
        n.razao_social,
        n.inicio_expediente,
        n.fim_expediente

    FROM usuario u
    JOIN endereco e ON e.id = u.id_endereco
    LEFT JOIN cliente c ON c.id_usuario = u.id
    LEFT JOIN negocio n ON n.id_usuario = u.id
    WHERE u.email = ?
""";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            String tipoStr = rs.getString("tipo");
            Usuario usuario;

            if ("CLIENTE".equals(tipoStr)) {
                Cliente cliente = new Cliente();
                cliente.setTelefone(rs.getString("telefone"));
                usuario = cliente;

            } else if ("NEGOCIO".equals(tipoStr)) {
                LavaJato lavaJato = new LavaJato();
                lavaJato.setCnpj(rs.getString("cnpj"));
                lavaJato.setRazaoSocial(rs.getString("razao_social"));
                lavaJato.setInicioExpediente(rs.getTime("inicio_expediente").toLocalTime());
                lavaJato.setFimExpediente(rs.getTime("fim_expediente").toLocalTime());
                usuario = lavaJato;
//                Negocio negocio = new Negocio();
//                negocio.setCnpj(rs.getString("cnpj"));
//                usuario = negocio;

            } else {
                throw new IllegalStateException("Tipo de usuário inválido");
            }

            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setNome(rs.getString("nome"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(tipoStr));

            Endereco endereco = new Endereco();
            endereco.setId(rs.getLong("endereco_id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setPais(rs.getString("pais"));
            endereco.setEstado(rs.getString("estado"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setRua(rs.getString("rua"));
            endereco.setCidade(rs.getString("cidade"));
            endereco.setReferencia(rs.getString("referencia"));

            usuario.setEndereco(endereco);

            return Optional.of(usuario);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
    }

    @Override
    public List<Negocio> listarTodosLavaJatos() {

        String sql = """
        SELECT
            u.id AS usuario_id,
        
            n.cnpj,
            n.razao_social,
            n.inicio_expediente,
            n.fim_expediente,
        
            u.nome,
            u.email,
        
            e.id AS endereco_id,
            e.cep,
            e.estado,
            e.bairro,
            e.rua,
            e.cidade,
            e.numero
        FROM usuario u
        JOIN negocio n ON n.id_usuario = u.id
        JOIN endereco e ON e.id = u.id_endereco
        WHERE u.tipo = 'NEGOCIO'
        ORDER BY u.id;
    """;

        List<Negocio> negocios = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ServicoPersistence servicoRepo = new ServicoPersistence();

            while (rs.next()) {

                LavaJato lavajato = new LavaJato();

                // setando lavajato
                lavajato.setId(rs.getLong("usuario_id"));

                lavajato.setNome(rs.getString("nome"));
                lavajato.setEmail(rs.getString("email"));
                lavajato.setCnpj(rs.getString("cnpj"));
                lavajato.setRazaoSocial(rs.getString("razao_social"));

                lavajato.setInicioExpediente(
                        rs.getTime("inicio_expediente").toLocalTime()
                );
                lavajato.setFimExpediente(
                        rs.getTime("fim_expediente").toLocalTime()
                );

                // setando endereço
                Endereco endereco = new Endereco();
                endereco.setId(rs.getLong("endereco_id"));
                endereco.setCep(rs.getString("cep"));
                endereco.setEstado(rs.getString("estado"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setRua(rs.getString("rua"));
                endereco.setCidade(rs.getString("cidade"));
                endereco.setNumero(rs.getString("numero"));

                lavajato.setEndereco(endereco);

                // buscando pelo os serviços pelo o id do lavajato
                lavajato.setServicosOferecidos(
                        servicoRepo.listarPorNegocio(lavajato.getId())
                );

                negocios.add(lavajato);
            }

            return negocios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar lavajatos", e);
        }
    }

    // salvar o endereço
    public void salvarEndereco(Usuario usuario) {
        String sqlEndereco = """
        INSERT INTO endereco (cep, pais, estado, bairro, rua, referencia, cidade, numero)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING id
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmtEndereco = conn.prepareStatement(sqlEndereco)) {

            if (usuario.getEndereco() == null) {
                throw new IllegalStateException("Usuário precisa de endereço");
            }

            if (usuario.getEndereco().getId() == null) {

                    Endereco e = usuario.getEndereco();

                    stmtEndereco.setString(1, e.getCep());
                    stmtEndereco.setString(2, e.getPais());
                    stmtEndereco.setString(3, e.getEstado());
                    stmtEndereco.setString(4, e.getBairro());
                    stmtEndereco.setString(5, e.getRua());
                    stmtEndereco.setString(6, e.getReferencia());
                    stmtEndereco.setString(7, e.getCidade());
                    stmtEndereco.setString(8, e.getNumero());

                    ResultSet rs = stmtEndereco.executeQuery();
                    if (rs.next()) {
                        e.setId(rs.getLong("id"));
                    }
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void atualizarEndereco(Connection conn, Endereco endereco) throws SQLException {

        String sqlEndereco = """
        UPDATE endereco
        SET cep = ?, pais = ?, estado = ?, bairro = ?, rua = ?, referencia = ?, cidade = ?, numero = ?
        WHERE id = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sqlEndereco)) {

            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getPais());
            stmt.setString(3, endereco.getEstado());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getRua());
            stmt.setString(6, endereco.getReferencia());
            stmt.setString(7, endereco.getCidade());
            stmt.setString(8, endereco.getNumero());
            stmt.setLong(9, endereco.getId());


            stmt.executeUpdate();
        }
    }
}

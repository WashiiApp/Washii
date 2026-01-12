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

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getTipoUsuario().name());
            stmt.setLong(5, usuario.getEndereco().getId());
            stmt.setLong(6, usuario.getId());

            stmt.executeUpdate();

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
        e.referencia,

        c.telefone,
        n.cnpj

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
                cliente.setTelefone(rs.getString("telefone"));
                usuario = cliente;

            } else if ("NEGOCIO".equals(tipo)) {
                LavaJato lavaJato = new LavaJato();
                lavaJato.setCnpj(rs.getString("cnpj"));
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
            endereco.setReferencia(rs.getString("referencia"));
            usuario.setEndereco(endereco);

            return Optional.of(usuario);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
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
        e.referencia,

        c.telefone,
        n.cnpj

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
        e.referencia,

        c.telefone,
        n.cnpj

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
            endereco.setReferencia(rs.getString("referencia"));

            usuario.setEndereco(endereco);

            return Optional.of(usuario);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
    }

    @Override
    public List<Negocio> listarTodosNegocios() {

        String sql = """
        SELECT id, cnpj, razao_social, inicio_expediente, fim_expediente
        FROM negocio
        ORDER BY id
    """;

        List<Negocio> negocios = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LavaJato lavaJato = new LavaJato();

                lavaJato.setId(rs.getLong("id"));
                lavaJato.setCnpj(rs.getString("cnpj"));
                lavaJato.setRazaoSocial(rs.getString("razao_social"));
                if (lavaJato instanceof LavaJato lavajato) {
                    stmt.setTime(4, Time.valueOf(lavajato.getInicioExpediente()));
                    stmt.setTime(5, Time.valueOf(lavajato.getFimExpediente()));
                }

                negocios.add(lavaJato);
            }

            return negocios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar negócios", e);
        }
    }

    // salvar o endereço
    public void salvarEndereco(Usuario usuario) {
        String sqlEndereco = """
        INSERT INTO endereco (cep, pais, estado, bairro, rua, referencia)
        VALUES (?, ?, ?, ?, ?, ?)
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

                    ResultSet rs = stmtEndereco.executeQuery();
                    if (rs.next()) {
                        e.setId(rs.getLong("id"));
                    }
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
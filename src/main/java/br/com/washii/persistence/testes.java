package br.com.washii.persistence;

import br.com.washii.domain.entities.*;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.repository.UsuarioRepository;
import br.com.washii.infra.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testes {

    public static void main(String[] args) {

        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();

//        try {
//            Long enderecoId;
//
//            String sqlEndereco = """
//                INSERT INTO endereco (cep, pais, estado, bairro, rua, referencia)
//                VALUES (?, ?, ?, ?, ?, ?)
//                RETURNING id
//            """;
//
//            try (Connection conn = DatabaseConfig.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(sqlEndereco)) {
//
//                stmt.setString(1, "12345-000");
//                stmt.setString(2, "Brasil");
//                stmt.setString(3, "SP");
//                stmt.setString(4, "Centro");
//                stmt.setString(5, "Rua Teste");
//                stmt.setString(6, "Perto da praça");
//
//                ResultSet rs = stmt.executeQuery();
//                rs.next();
//                enderecoId = rs.getLong("id");
//            }

//            // MONTAR OBJETO ENDEREÇO
//            Endereco endereco = new Endereco();
//            endereco.setId(enderecoId);
//
//            // CRIAR USUÁRIO
//            Cliente cliente = new Cliente();
//            cliente.setNome("Junior777");
//            cliente.setEmail("juniorzera@gmail.com");
//            cliente.setSenha("12345623dasa32");
//            cliente.setTipoUsuario(TipoUsuario.CLIENTE);
//            cliente.setEndereco(endereco);
//            cliente.setTelefone("845555555");
//
//            // SALVA USUÁRIO
//            usuarioRepository.salvar(cliente);
//
//            System.out.println(" Usuário salvo com sucesso!");

            // BUSCA POR EMAIL
            usuarioRepository.buscarPorEmail("teste@email.com")
                    .ifPresentOrElse(
                            u -> System.out.println("encontrou: " + u),
                            () -> System.out.println("not")
                    );

            usuarioRepository.buscarPorId(2L)
                    .ifPresentOrElse(u -> System.out.println("encontrou: " + u),
                            () -> System.out.println("not")
                    );

        System.out.println(usuarioRepository.listarTodos());


//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

package br.com.washii.domain.repository;

import java.util.Optional;

import br.com.washii.domain.entities.Usuario;

public interface UsuarioRepository extends Repository<Usuario, Long> {
    // Validação da senha será feita no service atraves do Usuario.getSenha()
    Optional<Usuario> buscarPorEmail(String email);
}

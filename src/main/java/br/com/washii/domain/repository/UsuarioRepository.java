package br.com.washii.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Usuario;

public interface UsuarioRepository extends Repository<Usuario, Long> {
    // Validação da senha será feita no service atraves do Usuario.getSenha()
    Optional<Usuario> buscarPorEmail(String email);
    List<Negocio> listarTodosLavaJatos();
}

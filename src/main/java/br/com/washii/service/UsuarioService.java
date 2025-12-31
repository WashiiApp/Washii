package br.com.washii.domain.services;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repositories.UsuarioRepository;

/**
 * Camada de Serviço responsável pelas regras de negócio do Usuário
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Injeção do Repository pelo construtor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Salva um novo usuário no sistema
     */
    public void salvarNovoUsuario(Usuario user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }

        usuarioRepository.salvar(user);
    }

    /**
     * Atualiza os dados de um usuário existente
     */
    public void atualizarUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para atualização");
        }

        usuarioRepository.atualizar(user);
    }

    /**
     * Remove um usuário do sistema
     */
    public void removerUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para remoção");
        }

        usuarioRepository.remover(user);
    }
}

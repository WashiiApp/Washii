package br.com.washii.service;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.exceptions.EmailJaCadastradoException;
import br.com.washii.domain.repository.UsuarioRepository;
import br.com.washii.infra.security.SenhaUtils;

import java.util.Optional;
import java.util.stream.Stream;

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
        Optional<Usuario>usuario = usuarioRepository.buscarPorEmail(user.getEmail());
        if (usuario.isPresent()){
            throw new EmailJaCadastradoException();
        }

        String  SenhaCrua = user.getSenha();
        String SenhaCripto = SenhaUtils.hashSenha(SenhaCrua);
        user.setSenha(SenhaCripto);

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

        usuarioRepository.remover(user.getId());
    }
}

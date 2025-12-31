package br.com.washii.domain.services;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repositories.UsuarioRepository;

import java.util.Optional;

/**
 * Classe responsável pela autenticação de usuários.
 * Implementa as regras de login, logout e recuperação de senha.
 */
public class AutenticacaoService {

    // Dependência conforme o diagrama UML
    private UsuarioRepository persistence;

    // Construtor
    public AutenticacaoService(UsuarioRepository persistence) {
        this.persistence = persistence;
    }

    /**
     * Realiza o login do usuário com base no email e senha.
     */
    public Usuario realizarLogin(String email, String senha) {
        Optional<Usuario> usuarioOpt = persistence.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha inválida.");
        }

        return usuario;
    }

    /**
     * Realiza o logout do usuário.
     * (Em sistemas simples, apenas controle de sessão)
     */
    public void realizarLogout() {
        System.out.println("Logout realizado com sucesso.");
    }

    /**
     * Simula a recuperação de senha via e-mail.
     */
    public void recuperarSenha(String email) {
        Optional<Usuario> usuarioOpt = persistence.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("E-mail não cadastrado.");
        }

        System.out.println("Link de recuperação de senha enviado para: " + email);
    }
}

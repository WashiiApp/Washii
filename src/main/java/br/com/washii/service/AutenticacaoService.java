package br.com.washii.service;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repository.UsuarioRepository;
import java.util.Optional;
import br.com.washii.domain.exceptions.EmailNaoCadastradoException;
import br.com.washii.domain.exceptions.SenhaInvalidaException;
import br.com.washii.domain.session.Sessao;


/**
 * Classe responsável pela autenticação de usuários.
 * Implementa as regras de login, logout e recuperação de senha.
 */
public class AutenticacaoService {


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
            throw new EmailNaoCadastradoException(email);
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getSenha().equals(senha)) {
            throw new  SenhaInvalidaException();

        }

        Sessao.getInstance().iniciarSessao(usuario);

        return usuario;
    }

    /**
     * Realiza o logout do usuário.
     * (Em sistemas simples, apenas controle de sessão)
     */
    public void realizarLogout() {
       Sessao.getInstance().encerrarSessao();
        System.out.println("Logout realizado com sucesso.");

    }

    /**
     * Simula a recuperação de senha via e-mail.
     */
    public void recuperarSenha(String email) {
        Optional<Usuario> usuarioOpt = persistence.buscarPorEmail(email);

//        if (usuarioOpt.isEmpty()) {
//            throw new IllegalArgumentException("E-mail não cadastrado.");
//        }
//
//        System.out.println("Link de recuperação de senha enviado para: " + email);
//    } CHAMAR A EXCEÇÃO DO EMAIL CADASTRADO.
}}

package br.com.washii.service;

import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repository.UsuarioRepository;
import java.util.Optional;
import br.com.washii.domain.exceptions.EmailNaoCadastradoException;
import br.com.washii.domain.exceptions.SenhaInvalidaException;
import br.com.washii.infra.security.SenhaCrypt;
import br.com.washii.infra.session.Sessao;



 //Classe responsável pela autenticação de usuários. Implementa as regras de login, logout e recuperação de senha.

public class AutenticacaoService {


    private UsuarioRepository persistence;

    // Construtor
    public AutenticacaoService(UsuarioRepository persistence) {
        this.persistence = persistence;
    }


     //Realiza o login do usuário com base no email e senha.

    public Usuario realizarLogin(String email, String senha) {
        Optional<Usuario> usuarioOpt = persistence.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new EmailNaoCadastradoException(email);
        }

        Usuario usuario = usuarioOpt.get();

        if (!SenhaCrypt.verificarSenha(senha, usuario.getSenha())) {
            throw new  SenhaInvalidaException();

        }

        return usuario;
    }


     //Realiza o logout do usuário. (Em sistemas simples, apenas controle de sessão)

    public void realizarLogout() {
       Sessao.getInstance().encerrarSessao();

    }
}

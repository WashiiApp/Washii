package br.com.washii.service;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.exceptions.EmailJaCadastradoException;
import br.com.washii.domain.repository.UsuarioRepository;
import br.com.washii.infra.security.SenhaCrypt;
import java.util.List;
import java.util.Optional;



 //Camada de Serviço responsável pelas regras de negócio do Usuário

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Injeção do Repository pelo construtor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarNovoUsuario(Usuario user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        Optional<Usuario>usuario = usuarioRepository.buscarPorEmail(user.getEmail());
        if (usuario.isPresent()){
            throw new EmailJaCadastradoException();
        }

        String  SenhaCrua = user.getSenha();
        String SenhaCripto = SenhaCrypt.hashSenha(SenhaCrua);
        user.setSenha(SenhaCripto);

        usuarioRepository.salvar(user);
    }

    public void atualizarUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para atualização");
        }

        usuarioRepository.atualizar(user);
    }

    public void removerUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para remoção");
        }

        usuarioRepository.remover(user.getId());
    }

    public Usuario buscarUsuarioPorId(Long id){
        Optional<Usuario> userOpt = usuarioRepository.buscarPorId(id);

        if (userOpt.isEmpty()){
            throw new IllegalArgumentException("Esse usuario não existe");
        }
        return userOpt.get();
    }

    public List<Negocio> listarTodosNegocios(){
        return usuarioRepository.listarTodosLavaJatos();
    }
}

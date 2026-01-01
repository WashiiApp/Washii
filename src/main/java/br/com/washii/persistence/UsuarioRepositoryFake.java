package br.com.washii.persistence;

import java.util.List;
import java.util.Optional;

import br.com.washii.domain.entities.Cliente;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.repository.UsuarioRepository;


public class UsuarioRepositoryFake implements UsuarioRepository {
    @Override
    public void salvar(Usuario usuario) {
        // Em vez de ir ao banco, apenas imprime no console
        System.out.println("FINGINDO salvar usuário no banco: " + usuario.toString());
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        // TODO Auto-generated method stub
        if (email.equals("ch")) {
            return Optional.ofNullable(new Cliente("João", "ch", "111", new Endereco(), TipoUsuario.CLIENTE));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        // Retorna um usuário fixo para teste
        return Optional.ofNullable(new Cliente());
    }


    @Override
    public void atualizar(Usuario entidade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public void remover(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remover'");
    }

    @Override
    public List<Usuario> listarTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarTodos'");
    }


}
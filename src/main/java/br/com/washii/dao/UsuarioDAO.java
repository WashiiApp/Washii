package br.com.washii.dao;

import br.com.washii.domain.entities.Usuario;

public interface UsuarioDAO extends BaseDAO<Usuario> {
    boolean buscarPorEmail(String email);
    Usuario validarCredenciais(String email, String senha);
}

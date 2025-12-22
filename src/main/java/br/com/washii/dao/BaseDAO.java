package br.com.washii.dao;

import java.util.List;

public interface BaseDAO<T> {
    void salvar(T entidade);
    void atualizar(T entidade);
    void excluir(int id);
    T buscarPorId(int id);
    List<T> listarTodos();
}

package br.com.washii.dao;

import java.util.List;

public interface DAO<T> {
    void salvar(T entidade) throws Exception;
    void atualizar(T entidade) throws Exception;
    void excluir(int id) throws Exception;
    T buscarPorId(int id) throws Exception;
    List<T> listarTodos() throws Exception;
}

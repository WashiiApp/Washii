package br.com.washii.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void salvar(T entidade);
    void atualizar(T entidade);
    void remover(ID id);
    Optional<T> buscarPorId(ID id);
    List<T> listarTodos();
}

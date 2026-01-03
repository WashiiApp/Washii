package br.com.washii.persistence;

import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

public class ServicoPersistence implements ServicoRepository {


    @Override
    public List<Servico> listarPorNegocio(Long negocioId) {
        return List.of();
    }

    @Override
    public void salvar(Servico entidade) {

    }

    @Override
    public void atualizar(Servico entidade) {

    }

    @Override
    public void remover(Long aLong) {
    }

    @Override
    public Optional<Servico> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Servico> listarTodos() {
        return List.of();
    }
}

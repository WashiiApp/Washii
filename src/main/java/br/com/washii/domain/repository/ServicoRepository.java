package br.com.washii.domain.repository;

import java.util.List;

import br.com.washii.domain.entities.Servico;

public interface ServicoRepository extends Repository<Servico, Long> {
    List<Servico> listarPorNegocio(Long negocioId);
}

package br.com.washii.dao;

import java.util.List;

import br.com.washii.domain.entities.Servico;

public interface ServicoDAO extends BaseDAO<Servico> {
    List<Servico> listarPorNegocio(Long negocioId);
}

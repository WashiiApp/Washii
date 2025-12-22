package br.com.washii.dao;

import br.com.washii.domain.Servico;

import java.util.List;

public interface ServicoDAO extends BaseDAO<Servico> {
    List<Servico> listarPorNegocio(Long negocioId);
}

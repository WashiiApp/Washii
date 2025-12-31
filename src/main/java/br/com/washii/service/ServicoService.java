package br.com.washii.domain.services;

import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.repositories.ServicoRepository;

/**
 * Classe responsável pelas regras de negócio relacionadas aos serviços.
 */
public class ServicoService {

    // Dependência de persistência (Repository)
    private ServicoRepository persistence;

    // Construtor
    public ServicoService(ServicoRepository persistence) {
        this.persistence = persistence;
    }

    /**
     * Salva um novo serviço.
     */
    public void salvarServico(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não pode ser nulo.");
        }
        persistence.salvar(servico);
    }

    /**
     * Remove um serviço existente.
     */
    public void removerServico(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não pode ser nulo.");
        }
        persistence.remover(servico);
    }

    /**
     * Atualiza os dados de um serviço.
     */
    public void atualizarServico(Servico servico) {
        if (servico == null || servico.getId() == null) {
            throw new IllegalArgumentException("Serviço inválido para atualização.");
        }
        persistence.atualizar(servico);
    }
}

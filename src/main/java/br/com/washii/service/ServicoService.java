package br.com.washii.service;

import java.util.List;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.repository.ServicoRepository;

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
        persistence.remover(servico.getId());
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

    public List<Servico> listarServicos(Negocio negocio) {
        if (negocio == null) {
            throw new IllegalArgumentException("Negocio não pode ser nulo.");
        }

        return persistence.listarPorNegocio(negocio.getId());
    }
}

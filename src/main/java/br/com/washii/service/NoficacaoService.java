package br.com.washii.domain.services;

import br.com.washii.domain.entities.Notificacao;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repositories.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio das notificações
 */
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    // Injeção do repository
    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Envia uma notificação para um usuário
     */
    public void enviar(Usuario destinatario, String titulo, String msg) {
        if (destinatario == null) {
            throw new IllegalArgumentException("Destinatário não pode ser nulo");
        }

        Notificacao notificacao = new Notificacao(
                titulo,
                msg,
                LocalDateTime.now(),
                false,
                destinatario
        );

        notificacaoRepository.salvar(notificacao);
    }

    /**
     * Lista todas as notificações de um usuário
     */
    public List<Notificacao> listarPorUsuario(Usuario usuario) {
        return notificacaoRepository.buscarPorUsuario(usuario);
    }

    /**
     * Marca todas as notificações do usuário como lidas
     */
    public void limparNotificacoes(Usuario usuario) {
        List<Notificacao> notificacoes = notificacaoRepository.buscarPorUsuario(usuario);

        for (Notificacao n : notificacoes) {
            n.setLida(true);
            notificacaoRepository.atualizar(n);
        }
    }

    /**
     * Conta quantas notificações não lidas o usuário possui
     */
    public int contarNaoLidas(Usuario usuario) {
        return notificacaoRepository.buscarPorUsuario(usuario).stream()
                .filter(n -> !n.isLida())
                .mapToInt(n -> 1)
                .sum();
    }
}

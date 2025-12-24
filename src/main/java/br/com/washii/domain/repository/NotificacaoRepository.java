package br.com.washii.domain.repository;

import java.util.List;

import br.com.washii.domain.entities.Notificacao;

public interface NotificacaoRepository extends Repository<Notificacao, Long> {
    List<Notificacao> listarPorUsuario(Long usuarioId);
    void marcarComoLida(Long notificacaoId);
    int contarNaoLidas(Long usuarioId);
}

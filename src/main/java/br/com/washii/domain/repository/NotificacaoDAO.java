package br.com.washii.domain.repository;

import java.util.List;

import br.com.washii.domain.entities.Notificacao;

public interface NotificacaoDAO extends BaseDAO<Notificacao> {
    List<Notificacao> listarPorUsuario(Long usuarioId);
    void marcarComoLida(Long notificacaoId);
    int contarNaoLidas(Long usuarioId);
}

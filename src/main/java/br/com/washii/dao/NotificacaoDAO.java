package br.com.washii.dao;

import br.com.washii.domain.Notificacao;

import java.util.List;

public interface NotificacaoDAO extends BaseDAO<Notificacao> {
    List<Notificacao> listarPorUsuario(Long usuarioId);
    void marcarComoLida(Long notificacaoId);
    int contarNaoLidas(Long usuarioId);
}

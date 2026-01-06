package br.com.washii.persistence;

import br.com.washii.domain.entities.Notificacao;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.repository.NotificacaoRepository;

import java.util.List;
import java.util.Optional;

public class NotificacaoPersistence implements NotificacaoRepository {
    @Override
    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return List.of();
    }

    @Override
    public void marcarComoLida(Long notificacaoId) {

    }

    @Override
    public int contarNaoLidas(Long usuarioId) {
        return 0;
    }

    @Override
    public List<Notificacao> buscarPorUsuario(Usuario usuario) {
        return List.of();
    }

    @Override
    public void salvar(Notificacao entidade) {

    }

    @Override
    public void atualizar(Notificacao entidade) {

    }

    @Override
    public void remover(Long aLong) {

    }

    @Override
    public Optional<Notificacao> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Notificacao> listarTodos() {
        return List.of();
    }
}

package br.com.washii.core;

import br.com.washii.domain.Usuario;
import java.util.Objects;

/**
 * Gerencia a sessão do usuário ativo na aplicação Washii.
 * Implementa o padrão Singleton (Bill Pugh) para garantir uma única instância
 * global e segura para multithreading.
 * * @author Grupo Washii
 */
public class Sessao {

    /**
     * Armazena o usuário autenticado.
     * O uso de 'volatile' garante a visibilidade imediata de alterações entre diferentes Threads.
     */
    private volatile Usuario usuarioLogado;

    /**
     * Construtor privado para impedir a instanciação direta de fora da classe.
     */
    private Sessao() {}

    /**
     * Classe interna estática (Holder) responsável por instanciar o Singleton.
     * O Java garante que esta classe só é carregada no primeiro acesso ao método getInstance().
     */
    private static class SessaoHolder {
        private static final Sessao INSTANCE = new Sessao();
    }

    /**
     * Retorna a instância única da Sessão.
     * @return Sessao A instância global do gerenciador de sessão.
     */
    public static Sessao getInstance() {
        return SessaoHolder.INSTANCE;
    }

    /**
     * Define o usuário autenticado e inicia a sessão no sistema.
     * Geralmente chamado pela camada de Service após a validação de credenciais.
     * * @param usuario O objeto Usuario (Cliente ou Negocio) vindo do banco de dados.
     * @throws NullPointerException se o usuário fornecido for nulo.
     */
    public void iniciarSessao(Usuario usuario) {
        this.usuarioLogado = Objects.requireNonNull(usuario, "Usuário não pode ser nulo ao iniciar sessão");
    }

    /**
     * Finaliza a sessão atual, removendo o usuário da memória.
     * Equivale à operação de Logout.
     */
    public void encerrarSessao() {
        this.usuarioLogado = null;
    }

    /**
     * Verifica se existe um usuário autenticado no momento.
     * @return boolean True se houver um usuário logado, False caso contrário.
     */
    public boolean isLogado() {
        return usuarioLogado != null;
    }

    /**
     * Obtém os dados do usuário atualmente autenticado.
     * @return Usuario O objeto do usuário logado ou null se não houver sessão ativa.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
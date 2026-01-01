package br.com.washii.service;

public interface ServiceFactory {
    AgendamentoService criarAgendamentoService();
    AutenticacaoService criarAutenticacaoService();
    NotificacaoService criarNotificacaoService();
    ServicoService criarServicoService();
    UsuarioService criarUsuarioService();
}

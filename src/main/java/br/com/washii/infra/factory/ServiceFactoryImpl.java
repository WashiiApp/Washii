package br.com.washii.infra.factory;

import br.com.washii.persistence.AgendamentoPersistence;
import br.com.washii.persistence.NotificacaoPersistence;
import br.com.washii.persistence.ServicoPersistence;
import br.com.washii.persistence.UsuarioPersistence;
import br.com.washii.service.AgendamentoService;
import br.com.washii.service.AutenticacaoService;
import br.com.washii.service.NotificacaoService;
import br.com.washii.service.ServiceFactory;
import br.com.washii.service.ServicoService;
import br.com.washii.service.UsuarioService;

public class ServiceFactoryImpl implements ServiceFactory {
    // Aqui será gerado toda a lógica de criação de services
    // Ou seja, será definido as implementações de repository e 
    // injetados na instanciação dos services

    private final static UsuarioPersistence USUARIO_PERSISTENCE = new UsuarioPersistence();
    private final static ServicoPersistence SERVICO_PERSISTENCE = new ServicoPersistence();
    private final static AgendamentoPersistence AGENDAMENTO_PERSISTENCE = new AgendamentoPersistence();
    private final static NotificacaoPersistence NOTIFICACAO_PERSISTENCE = new NotificacaoPersistence();

    
    @Override
    public UsuarioService criarUsuarioService() {
        return new UsuarioService(USUARIO_PERSISTENCE);
    }

    @Override
    public AutenticacaoService criarAutenticacaoService() {
        return new AutenticacaoService(USUARIO_PERSISTENCE);
    }

    @Override
    public ServicoService criarServicoService() {
        return new ServicoService(SERVICO_PERSISTENCE);
    }

    @Override
    public AgendamentoService criarAgendamentoService() {
        return new AgendamentoService(AGENDAMENTO_PERSISTENCE);
    }

    @Override
    public NotificacaoService criarNotificacaoService() {
        return new NotificacaoService(NOTIFICACAO_PERSISTENCE);
    }
}

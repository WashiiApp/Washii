package br.com.washii.infra.factory;

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

    @Override
    public UsuarioService criarUsuarioService() {
        return new UsuarioService(new UsuarioPersistence());
    }

    @Override
    public AutenticacaoService criarAutenticacaoService() {
        return new AutenticacaoService(new UsuarioPersistence());
    }

    @Override
    public AgendamentoService criarAgendamentoService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarAgendamentoService'");
    }

    

    @Override
    public NotificacaoService criarNotificacaoService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarNotificacaoService'");
    }

    @Override
    public ServicoService criarServicoService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarServicoService'");
    }
}

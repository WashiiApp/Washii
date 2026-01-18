package br.com.washii.infra.factory;

import br.com.washii.presentation.layout.ClienteLayoutController;
import br.com.washii.presentation.layout.NegocioLayoutController;
import br.com.washii.presentation.screens.acesso.CadastroController;
import br.com.washii.presentation.screens.acesso.LoginController;
import br.com.washii.presentation.screens.agendamentos.ClienteAgendamentoController;
import br.com.washii.presentation.screens.agendamentos.MeusAgendamentosClienteController;
import br.com.washii.presentation.screens.home.HomeClienteController;
import br.com.washii.presentation.screens.home.HomeNegocioController;
import br.com.washii.presentation.screens.perfil.PerfilClienteController;
import br.com.washii.presentation.screens.perfil.PerfilNegocioController;
import br.com.washii.presentation.screens.relatorio.RelatorioNegocioController;
import br.com.washii.presentation.screens.servico.GestaoServicosController;
import br.com.washii.service.ServiceFactory;

public class ControllerFactory {
    // Aqui centraliza a lógica de criação dos controllers
    // Controllers esses que  será ijetado  service nos seus construtores
    // services que são criado pelo ServiceFactory

    // Essa classe será usado no SceneManager

    private final ServiceFactory serviceFactory;

    public ControllerFactory(ServiceFactory serviceFactory) {
        if (serviceFactory == null){
            throw new IllegalArgumentException("ServiceFactory não pode ser null");
        }
        this.serviceFactory = serviceFactory;
    }

    public Object criar(Class<?> clazz) {
        // Centralizamos aqui a "receita" de cada Controller
        if (clazz == CadastroController.class) {
            return new CadastroController(serviceFactory.criarUsuarioService());
        }
        
        if (clazz == LoginController.class) {
            return new LoginController(serviceFactory.criarAutenticacaoService());
        }

        if (clazz == ClienteLayoutController.class){
            return new ClienteLayoutController(serviceFactory.criarAutenticacaoService());
        }

        if (clazz == NegocioLayoutController.class) {
            return new NegocioLayoutController(serviceFactory.criarAutenticacaoService());
        }

        if (clazz == GestaoServicosController.class) {
            return new GestaoServicosController(serviceFactory.criarServicoService());
        }

        if (clazz == HomeClienteController.class) {
            return new HomeClienteController(serviceFactory.criarUsuarioService());
        }

        if (clazz == HomeNegocioController.class) {
            return new HomeNegocioController(serviceFactory.criarAgendamentoService());
        }

        if (clazz == ClienteAgendamentoController.class) {
            return new ClienteAgendamentoController(serviceFactory.criarAgendamentoService(), serviceFactory.criarUsuarioService());
        }

        if (clazz == PerfilClienteController.class) {
            return new PerfilClienteController(serviceFactory.criarUsuarioService());
        }

        if (clazz == PerfilNegocioController.class) {
            return new PerfilNegocioController(serviceFactory.criarUsuarioService());
        }

        if (clazz == MeusAgendamentosClienteController.class) {
            return new MeusAgendamentosClienteController(serviceFactory.criarAgendamentoService());
        }

        if (clazz == RelatorioNegocioController.class) {
            return new RelatorioNegocioController(serviceFactory.criarAgendamentoService());
        }

        // Se o controller não tiver dependências, usamos o construtor padrão
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar controller: " + clazz.getName(), e);
        }
    }
}
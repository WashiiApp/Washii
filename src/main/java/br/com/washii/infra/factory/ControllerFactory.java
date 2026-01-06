package br.com.washii.infra.factory;

import br.com.washii.presentation.acesso.CadastroController;
import br.com.washii.presentation.acesso.LoginController;
import br.com.washii.presentation.layout.ClienteLayoutController;
import br.com.washii.presentation.layout.NegocioLayoutController;
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

        // Se o controller não tiver dependências, usamos o construtor padrão
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar controller: " + clazz.getName(), e);
        }
    }
}
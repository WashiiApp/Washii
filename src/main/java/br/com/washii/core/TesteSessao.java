package br.com.washii.core;

import br.com.washii.domain.Cliente;
import br.com.washii.domain.enums.TipoUsuario;

public class TesteSessao {
    public static void main(String[] args) {
        // 1. Tentar pegar a sessão e ver se está vazia
        Sessao sessao = Sessao.getInstance();
        System.out.println("Usuário logado ao iniciar: " + sessao.getUsuarioLogado()); // Deve ser null

        // 2. Simular a criação de um usuário (como se viesse do banco)
        Cliente clienteSimulado = new Cliente();
        clienteSimulado.setNome("João Silva");
        clienteSimulado.setEmail("joao@email.com");
        clienteSimulado.setTipoUsuario(TipoUsuario.CLIENTE);

        // 3. Iniciar a sessão
        sessao.iniciarSessao(clienteSimulado);
        System.out.println("Após login: " + Sessao.getInstance().getUsuarioLogado().getNome());

        // 4. Testar a Unicidade (Singleton)
        // Vamos pegar "outra" instância e ver se ela tem o mesmo usuário
        Sessao outraReferencia = Sessao.getInstance();
        if (outraReferencia == sessao) {
            System.out.println("Sucesso: É a mesma instância de memória!");
        }
        System.out.println("Nome na outra referência: " + outraReferencia.getUsuarioLogado().getNome());

        // 5. Testar Logout
        sessao.encerrarSessao();
        System.out.println("Após logout, está logado? " + sessao.isLogado());
    }
}
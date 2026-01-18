package br.com.washii.persistence;

import br.com.washii.persistence.AgendamentoPersistence;
import br.com.washii.domain.entities.Agendamento;

import java.time.LocalDate;
import java.util.List;

public class TesteAgendamentoPersistence {

    public static void main(String[] args) {

        AgendamentoPersistence repo = new AgendamentoPersistence();

        List<Agendamento> agendamentos = repo.listarPorCliente(38L);
        // 1L = id_usuario (ajuste se necessário)

        System.out.println("TOTAL: " + agendamentos.size());

        for (Agendamento ag : agendamentos) {
            System.out.println("---------------");
            System.out.println("ID: " + ag.getId());
            System.out.println("Status: " + ag.getStatus());
            System.out.println("Data: " + ag.getData());
            System.out.println("Hora: " + ag.getHora());

            System.out.println("Negócio: " + ag.getNegocio().getNome());
            System.out.println("Endereço: " +
                    ag.getNegocio().getEndereco().getRua()
            );

            System.out.println("Serviços:");
            ag.getServicos().forEach(s ->
                    System.out.println("- " + s.getNome() + " R$ " + s.getPrecoBase())
            );

            System.out.println("TOTAL R$: " + ag.calcularValorTotal());
        }
    }
}

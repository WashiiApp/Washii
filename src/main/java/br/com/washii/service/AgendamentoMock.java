package br.com.washii.service;

import br.com.washii.domain.entities.*;
import br.com.washii.domain.enums.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoMock {

    public static List<Agendamento> gerarListaMock() {
        List<Agendamento> lista = new ArrayList<>();

        // 1. Criando um Negócio (Lava-Jato fictício)
        Negocio washCenter = new LavaJato(); // Assumindo que a classe Negocio existe
        washCenter.setId(1L);

        // --- AGENDAMENTO 1: EM ANDAMENTO (PENDENTE) ---
        Cliente c1 = new Cliente();
        c1.setNome("Marcos Oliveira");
        Veiculo v1 = new Veiculo(CategoriaVeiculo.CARRO, "ABC-1234");
        
        Agendamento ag1 = new Agendamento(c1, washCenter, LocalDate.now(), LocalTime.now().plusMinutes(30), v1);
        ag1.setId(101L);
        ag1.setStatus(StatusAgendamento.AGENDADO);
        ag1.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        ag1.addServico(new Servico("Pretinho", "Pneus brilhando", CategoriaServico.OUTROS, 10.0, washCenter));
        lista.add(ag1);
        return lista;
    }
}
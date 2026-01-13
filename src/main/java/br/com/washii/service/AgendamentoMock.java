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

        // --- AGENDAMENTO 2: PRÓXIMO (AGENDADO) ---
        Cliente c2 = new Cliente();
        c2.setNome("Ana Beatriz");
        Veiculo v2 = new Veiculo(CategoriaVeiculo.SUV, "XYZ-9876");

        Agendamento ag2 = new Agendamento(c2, washCenter, LocalDate.now(), LocalTime.now().plusHours(2), v2);
        ag2.setId(102L);
        ag2.setStatus(StatusAgendamento.EM_ANDAMENTO);
        ag2.addServico(new Servico("Polimento Farol", "Restauração", CategoriaServico.POLIMENTO, 150.0, washCenter));
        lista.add(ag2);

        // --- AGENDAMENTO 3: FINALIZADO (CONCLUIDO) ---
        Cliente c3 = new Cliente();
        c3.setNome("Ricardo Santos");
        Veiculo v3 = new Veiculo(CategoriaVeiculo.PICKUP, "KMT-5544");

        Agendamento ag3 = new Agendamento(c3, washCenter, LocalDate.now(), LocalTime.now().plusHours(5), v3);
        ag3.setId(103L);
        ag3.setStatus(StatusAgendamento.AGENDADO);
        ag3.addServico(new Servico("Ducha", "Lavagem rápida", CategoriaServico.LAVAGEM_SIMPLES, 40.0, washCenter));
        lista.add(ag3);

        // --- AGENDAMENTO 4: OUTRO AGENDADO ---
        Cliente c4 = new Cliente();
        c4.setNome("Juliana Paes");
        Veiculo v4 = new Veiculo(CategoriaVeiculo.SUV, "BR-2E25");

        Agendamento ag4 = new Agendamento(c4, washCenter, LocalDate.now(), LocalTime.now().plusHours(4), v4);
        ag4.setId(104L);
        ag4.setStatus(StatusAgendamento.CONCLUIDO);
        ag4.addServico(new Servico("Higienização", "Ozônio", CategoriaServico.HIGIENIZACAO, 200.0, washCenter));
        lista.add(ag4);

        return lista;
    }
}
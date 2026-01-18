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
        washCenter.setNome("WashCenter");
        Endereco end = new Endereco("23323232", "Paraiba", "Bananeiras", "Rua dos tolos", "centro", "332", "Brasil");
        washCenter.setEndereco(end);
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

        Agendamento ag2 = new Agendamento(c1, washCenter, LocalDate.now().minusDays(1), LocalTime.now().plusMinutes(40), v1);
        ag2.setId(101L);
        ag2.setStatus(StatusAgendamento.CONCLUIDO);
        ag2.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        ag2.addServico(new Servico("Pretinho", "Pneus brilhando", CategoriaServico.OUTROS, 10.0, washCenter));
        lista.add(ag2);

        Agendamento ag3 = new Agendamento(c1, washCenter, LocalDate.now(), LocalTime.now().plusMinutes(40), v1);
        ag3.setId(101L);
        ag3.setStatus(StatusAgendamento.CONCLUIDO);
        ag3.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        lista.add(ag3);

        Agendamento ag4 = new Agendamento(c1, washCenter, LocalDate.now().minusDays(3), LocalTime.now().plusMinutes(40), v1);
        ag4.setId(101L);
        ag4.setStatus(StatusAgendamento.CANCELADO);
        ag4.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        ag4.addServico(new Servico("Pretinho", "Pneus brilhando", CategoriaServico.OUTROS, 10.0, washCenter));
        lista.add(ag4);

        Agendamento ag5 = new Agendamento(c1, washCenter, LocalDate.now(), LocalTime.now().plusMinutes(40), v1);
        ag5.setId(101L);
        ag5.setStatus(StatusAgendamento.NAO_COMPARECEU);
        ag5.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        lista.add(ag5);

        Agendamento ag6 = new Agendamento(c1, washCenter, LocalDate.now().minusDays(5), LocalTime.now().plusMinutes(40), v1);
        ag5.setId(101L);
        ag5.setStatus(StatusAgendamento.NAO_COMPARECEU);
        ag5.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        lista.add(ag6);

        Agendamento ag7 = new Agendamento(c1, washCenter, LocalDate.now().minusDays(5), LocalTime.now().plusMinutes(40), v1);
        ag5.setId(101L);
        ag5.setStatus(StatusAgendamento.NAO_COMPARECEU);
        ag5.addServico(new Servico("Lavagem Completa", "Interna e externa", CategoriaServico.LAVAGEM_DETALHADA, 80.0, washCenter));
        lista.add(ag7);


        return lista;
    }
}
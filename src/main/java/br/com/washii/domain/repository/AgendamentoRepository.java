package br.com.washii.domain.repository;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends Repository<Agendamento, Long> {
    List<Agendamento> listarPorPeriodoENegocio(LocalDate inicio, LocalDate fim, Long negocioId);
    List<Agendamento> listarPorCliente(Long clienteId);
    void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus);
    int contarAgendamento(Long negocioId, LocalDate data, LocalTime hora);
}

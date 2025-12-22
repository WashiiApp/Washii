package br.com.washii.dao;

import br.com.washii.domain.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoDAO extends BaseDAO<Agendamento> {
    List<Agendamento> listarPorDataENegocio(LocalDate data, Long negocioId);
    List<Agendamento> listarPorCliente(Long clienteId);
    void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus);
    int contarAgendamento(Long negocioId, LocalDate data, LocalTime hora);
}

package br.com.washii.persistence;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.repository.AgendamentoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class AgendamentoPersistence implements AgendamentoRepository {
    @Override
    public List<Agendamento> listarPorPeriodoENegocio(LocalDate inicio, LocalDate fim, Long negocioId) {
        return List.of();
    }

    @Override
    public List<Agendamento> listarPorCliente(Long clienteId) {
        return List.of();
    }

    @Override
    public void atualizarStatus(Long agendamentoId, StatusAgendamento novoStatus) {

    }

    @Override
    public int contarAgendamento(Long negocioId, LocalDate data, LocalTime hora) {
        return 0;
    }

    @Override
    public void salvar(Agendamento entidade) {

    }

    @Override
    public void atualizar(Agendamento entidade) {

    }

    @Override
    public void remover(Long aLong) {

    }

    @Override
    public Optional<Agendamento> buscarPorId(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Agendamento> listarTodos() {
        return List.of();
    }
}

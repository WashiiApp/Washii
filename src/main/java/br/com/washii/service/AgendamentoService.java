package br.com.washii.service;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.repository.AgendamentoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendamentoService {

    private AgendamentoRepository persistence;

    public AgendamentoService(AgendamentoRepository persistence) {
        this.persistence = persistence;
    }

    public void solicitarAgendamento(Agendamento a) {
        // Lógica para solicitar agendamento
    }

    public void cancelarAgendamento(Agendamento a) {
        // Lógica para cancelar agendamento
    }

    public List<Agendamento> listarPorData(LocalDate data, Negocio negocio) {
        // Lógica para listar agendamentos por data e negócio
        return null;
    }

    public List<Negocio> listarHistoricoUsuario(Usuario user) {
        // Lógica para listar o histórico de negócios do usuário
        return null;
    }

    public List<LocalTime> listarHorariosDisponiveis(LocalDate data, Negocio negocio) {
        // Lógica para listar horários disponíveis
        return null;
    }

    public boolean validarDisponibilidade(LocalDate data, LocalTime hora, Negocio negocio) {
        // Lógica para validar se um horário está disponível
        return false;
    }

    public void atualizarStatus(StatusAgendamento novoStatus, Agendamento agendamento) {
        // Lógica para atualizar o status do agendamento
    }
}